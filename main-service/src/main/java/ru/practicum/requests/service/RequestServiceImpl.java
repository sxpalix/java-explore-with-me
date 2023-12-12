package ru.practicum.requests.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.State;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestsRepository repository;
    private final UserRepository userRepository;
    private final EventsRepository eventsRepository;

    public List<ParticipationRequestDto> getParticipateRequestsByEventCurrentUser(long userId, long eventId) {
        getEventById(eventId);
        getUserById(userId);

        return repository.findAllByEvent_Id(eventId).stream().map(RequestMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public PatchResponseDto updateParticipateRequestsByEventCurrentUser(long userId, long eventId, PatchRequestDto request) {
        Event event = eventsRepository.findEventByIdAndInitiator_Id(eventId, userId).orElseThrow(() ->
                new NotFoundException(String.format("User with id=%d or Event with id=%d not found", userId, eventId)));

        List<ParticipationRequest> confirmedRequests = repository
                .findByEvent_IdAndStatus(eventId, ParticipationRequestState.CONFIRMED);

        long limit = (event).getParticipantLimit();
        long confirmed = confirmedRequests.size();
        long mayBeApprove = limit - confirmed;
        if (confirmed >= limit)
            throw new RequestException("Participation limit reached");

        PatchResponseDto resultUpdate = PatchResponseDto.builder()
                .confirmedRequests(new ArrayList<>())
                .rejectedRequests(new ArrayList<>()).build();

        List<ParticipationRequest> toApprove = repository.findAllByEvent_IdAndIdIn(eventId, request.getRequestIds());

        for (ParticipationRequest req : toApprove) {
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

    public List<ParticipationRequestDto> getRequestsByUserId(long userId) {
        getUserById(userId);
        return repository.findAllByRequester_Id(userId)
                .stream().map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ParticipationRequestDto createRequest(long userId, long eventId) {
        User user = getUserById(userId);

        Event event = getEventById(eventId);

        if (!event.getState().equals(State.PUBLISHED))
            throw new RequestException("The request cannot be published because the event isn't published");

        if (event.getInitiator().getId() == userId)
            throw new RequestException("The event initiator cannot add a request to participate in his event");

        long confirmedRequests = event.getRequests().stream().filter((requestx) ->
                requestx.getStatus().equals(ParticipationRequestState.CONFIRMED)).count();

        if (event.getParticipantLimit() != 0L && confirmedRequests >= event.getParticipantLimit())
            throw new RequestException("Request limit exceeded");

        ParticipationRequest request = ParticipationRequest
                .builder()
                .requester(user)
                .event(event)
                .created(LocalDateTime.now())
                .build();

        if (event.getRequestModeration() && event.getParticipantLimit() != 0L)
            request.setStatus(ParticipationRequestState.PENDING);
        else request.setStatus(ParticipationRequestState.CONFIRMED);

        return RequestMapper.toDto(repository.save(request));


    }

    @Transactional
    public ParticipationRequestDto updateRequest(long userId, long requestId) {
        getUserById(userId);
        ParticipationRequest request = repository.findByRequester_IdAndId(userId, requestId).orElseThrow(() ->
                new NotFoundException(String.format("Request with id=%d was not found", requestId)));

        request.setStatus(ParticipationRequestState.CANCELED);
        return RequestMapper.toDto(repository.save(request));
    }

    private User getUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("User with id=%d was not found", userId)));
    }

    private Event getEventById(long eventId) {
        return eventsRepository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id=%d was not found", eventId)));
    }
}
