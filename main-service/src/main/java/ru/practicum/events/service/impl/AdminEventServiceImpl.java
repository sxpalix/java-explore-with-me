package ru.practicum.events.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventUpdateDto;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.State;
import ru.practicum.events.model.StateAction;
import ru.practicum.events.repository.EventsRepository;
import ru.practicum.events.service.UpdateHelper;
import ru.practicum.events.service.interfaces.AdminEventService;
import ru.practicum.exception.DateTimeException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.RequestException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {
    private final EventsRepository repository;
    private final UpdateHelper helper;

    @Transactional
    public List<EventFullDto> getAllEventByParameters(List<Long> users,
                                                      List<State> states,
                                                      List<Long> categories,
                                                      LocalDateTime rangeStart,
                                                      LocalDateTime rangeEnd,
                                                      int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
        }

        if (rangeEnd == null) {
            rangeEnd = LocalDateTime.now().plusYears(100L);
        }

        return repository.findAllByParameters(users, states, categories, rangeStart, rangeEnd, pageable).stream()
                .map(EventMapper::toFullDto).collect(Collectors.toList());
    }

    @Transactional
    public EventFullDto patchEventByAdmin(long eventId, EventUpdateDto dto) {
        Optional<Event> oldEvent = this.repository.findById(eventId);
        if (oldEvent.isEmpty()) {
            throw new NotFoundException("Event not found");
        }

        Event event = oldEvent.get();

        if (dto.getEventDate() != null && dto.getEventDate().isBefore(LocalDateTime.now().plusHours(2L))) {
            throw new DateTimeException(
                    "The start date of the modified event must be " +
                            "no earlier than an hour from the publication date.");
        }

        if (dto.getStateAction() != null) {
            if (dto.getStateAction().equals(StateAction.PUBLISH_EVENT.toString()) &&
                    !event.getState().equals(State.PENDING)) {
                throw new RequestException(
                        "Публикация события не возможна, не верное состояние: " + event.getState());
            }

            if (dto.getStateAction().equals(StateAction.REJECT_EVENT.toString()) &&
                    event.getState().equals(State.PUBLISHED)) {
                throw new RequestException("Отклонение события не возможна, не верное состояние: "
                        + event.getState());
            }
        }

        Event updated = helper.updateEvent(event, dto);
        if (dto.getStateAction() != null) {
            switch (StateAction.valueOf(dto.getStateAction())) {
                case PUBLISH_EVENT:
                    event.setState(State.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                    break;
                case REJECT_EVENT:
                    event.setState(State.CANCELED);
            }
        }

        return EventMapper.toFullDto(repository.save(updated));
    }
}

