package ru.practicum.requests.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventsRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.RequestException;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.dto.PatchRequestDto;
import ru.practicum.requests.dto.PatchResponseDto;
import ru.practicum.requests.dto.RequestMapper;
import ru.practicum.requests.model.ParticipationRequest;
import ru.practicum.requests.model.ParticipationRequestState;
import ru.practicum.requests.repository.RequestsRepository;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserRequestServiceImpl implements UserRequestService {
    private final RequestsRepository repository;
    private final UserRepository userRepository;
    private final EventsRepository eventsRepository;

    public List<ParticipationRequestDto> getParticipateRequestsByEventCurrentUser(long userId, long eventId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Event> event = eventsRepository.findEventByIdAndInitiator_Id(eventId, userId);

        if (user.isEmpty())
            throw new NotFoundException(String.format("User with id=%d not found", userId));
        if (event.isEmpty())
            throw new NotFoundException(String.format("Event with id=%d not found", eventId));

        return repository.findAllByEvent_Id(eventId).stream().map(RequestMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public PatchResponseDto patchParticipateRequestsByEventCurrentUser(long userId, long eventId, PatchRequestDto request) {
        Optional<Event> event = eventsRepository.findEventByIdAndInitiator_Id(eventId, userId);
        if (event.isEmpty())
            throw new NotFoundException(String.format("User with id=%d or Event with id=%d not found", userId, eventId));

        List<ParticipationRequest> confirmedRequests = repository
                .findByEvent_IdAndStatus(eventId, ParticipationRequestState.CONFIRMED);

        long limit = (event.get()).getParticipantLimit();
        long confirmed = confirmedRequests.size();
        long mayBeApprove = limit - confirmed;
        if (confirmed >= limit)
            throw new RequestException("Participation limit reached");

        PatchResponseDto resultUpdate = PatchResponseDto.builder()
                .confirmedRequests(new ArrayList<>())
                .rejectedRequests(new ArrayList<>()).build();

        List<ParticipationRequest> toApprove = repository.findAllByEvent_IdAndIdIn(eventId, request.getRequestIds());

        for (ParticipationRequest req: toApprove) {
            if (!req.getStatus().equals(ParticipationRequestState.PENDING)) {
                throw new RequestException("Chose request where status PENDING");
            }

            if (mayBeApprove <= 0L) {
                req.setStatus(ParticipationRequestState.REJECTED);
                resultUpdate.getRejectedRequests().add(RequestMapper.toDto(req));
            } else {
                switch (request.getStatus()) {
                    case CONFIRMED:
                        req.setStatus(ParticipationRequestState.CONFIRMED);
                        resultUpdate.getConfirmedRequests().add(RequestMapper.toDto(req));
                        --mayBeApprove;
                        break;
                    case REJECTED:
                        req.setStatus(ParticipationRequestState.REJECTED);
                        resultUpdate.getRejectedRequests().add(RequestMapper.toDto(req));
                }
            }
        }
        repository.saveAll(toApprove);
        return resultUpdate;
    }
}
