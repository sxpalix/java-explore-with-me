package ru.practicum.events.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.dto.EventDto;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.EventUpdateDto;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.State;
import ru.practicum.events.model.StateAction;
import ru.practicum.events.repository.EventsRepository;
import ru.practicum.events.service.UpdateHelper;
import ru.practicum.events.service.interfaces.EventService;
import ru.practicum.exception.DateTimeException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.RequestException;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventsRepository repository;
    private final UpdateHelper helper;
    private final UserRepository userRepository;

    @Transactional
    public EventFullDto postEvents(long userId, EventDto eventCreateDto) {
        if (eventCreateDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2L))) {
            throw new DateTimeException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: " + eventCreateDto.getEventDate());
        }

        if (eventCreateDto.getPaid() == null) {
            eventCreateDto.setPaid(false);
        }

        if (eventCreateDto.getParticipantLimit() == null) {
            eventCreateDto.setParticipantLimit(0L);
        }

        if (eventCreateDto.getRequestModeration() == null) {
            eventCreateDto.setRequestModeration(true);
        }

        Event eventToCreate = EventMapper.fromEventDtoToEvent(eventCreateDto);
        eventToCreate.setCategory(helper.getCategoryById(eventCreateDto.getCategory()));
        eventToCreate.setInitiator(getUserById(userId));
        eventToCreate.setLocation(helper.createNewLocation(eventCreateDto.getLocation()));
        eventToCreate.setCreatedOn(LocalDateTime.now());
        eventToCreate.setState(State.PENDING);
        return EventMapper.toFullDto(repository.save(eventToCreate));
    }

    public List<EventShortDto> getEventCreatedByUser(long userId, int from, int size) {
        this.getUserById(userId);
        return repository.findAllByInitiator_Id(userId, PageRequest.of(from > 0 ? from / size : 0, size))
                .stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    public EventFullDto getEventByIdAndUserId(long userId, long eventId) {
        this.getUserById(userId);
        Optional<Event> event = this.repository.findEventByIdAndInitiator_Id(eventId, userId);
        if (event.isEmpty()) {
            throw new NotFoundException(String.format("Event with id= %d was not found", eventId));
        } else {
            return EventMapper.toFullDto(event.get());
        }
    }

    @Transactional
    public EventFullDto updateEventByUser(long userId, long eventId, EventUpdateDto dto) {
        this.getUserById(userId);
        Optional<Event> eventToUpdate = this.repository.findById(eventId);
        if (eventToUpdate.isEmpty()) {
            throw new NotFoundException(String.format("Event with id= %d was not found", eventId));
        }

        Event event = eventToUpdate.get();
        if (event.getState().equals(State.PUBLISHED)) {
            throw new RequestException("cannot change an event with a status PUBLISHED");
        }
        Event updatedEvent = this.helper.updateEvent(event, dto);
        if (dto.getStateAction() != null) {
            StateAction state = StateAction.valueOf(dto.getStateAction());
            switch (state) {
                case SEND_TO_REVIEW:
                    updatedEvent.setState(State.PENDING);
                    break;
                case CANCEL_REVIEW:
                    updatedEvent.setState(State.CANCELED);
            }
        }
        return EventMapper.toFullDto(repository.save(updatedEvent));
    }

    private User getUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("User with id= %s was not found", userId)));
    }
}
