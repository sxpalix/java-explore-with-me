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
import ru.practicum.requests.dto.RequestMapper;
import ru.practicum.requests.model.ParticipationRequest;
import ru.practicum.requests.model.ParticipationRequestState;
import ru.practicum.requests.repository.RequestsRepository;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class PrivateRequestServiceImpl implements PrivateRequestsService {
    private final RequestsRepository repository;
    private final UserRepository userRepository;
    private final EventsRepository eventsRepository;

    public List<ParticipationRequestDto> getRequestsByUserId(long userId) {
        this.getUserById(userId);
        return repository.findAllByRequester_Id(userId)
                .stream().map(RequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ParticipationRequestDto postRequest(long userId, long eventId) {
        User user = getUserById(userId);

        Optional<Event> event = this.eventsRepository.findById(eventId);
        if (event.isEmpty()) throw new NotFoundException(String.format("Event with id=%d was not found", eventId));

        if (!event.get().getState().equals(State.PUBLISHED))
            throw new RequestException("The request cannot be published because the event isn't published");

        if ((event.get()).getInitiator().getId() == userId)
            throw new RequestException("The event initiator cannot add a request to participate in his event");

        long confirmedRequests = (event.get()).getRequests().stream().filter((requestx) ->
                requestx.getStatus().equals(ParticipationRequestState.CONFIRMED)).count();

        if ((event.get()).getParticipantLimit() != 0L && confirmedRequests >= (event.get()).getParticipantLimit())
            throw new RequestException("Request limit exceeded");

        ParticipationRequest request = ParticipationRequest
                .builder()
                .requester(user)
                .event(event.get())
                .created(LocalDateTime.now())
                .build();

        if ((event.get()).getRequestModeration() && (event.get()).getParticipantLimit() != 0L)
            request.setStatus(ParticipationRequestState.PENDING);
        else request.setStatus(ParticipationRequestState.CONFIRMED);

        return RequestMapper.toDto(repository.save(request));


    }

    @Transactional
    public ParticipationRequestDto updateRequest(long userId, long requestId) {
        getUserById(userId);
        Optional<ParticipationRequest> requestToUpdate = this.repository.findByRequester_IdAndId(userId, requestId);
        if (requestToUpdate.isEmpty()) {
            throw new NotFoundException(String.format("Request with id=%d was not found", requestId));
        } else {
            ParticipationRequest request = requestToUpdate.get();
            request.setStatus(ParticipationRequestState.CANCELED);
            return RequestMapper.toDto(repository.save(request));
        }
    }

    private User getUserById(long userId) {
        Optional<User> user = this.userRepository.findById(userId);
        if (user.isEmpty())
            throw new NotFoundException(String.format("User with id=%d was not found", userId));
        return user.get();
    }
}
