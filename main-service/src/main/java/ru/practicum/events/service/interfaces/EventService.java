package ru.practicum.events.service.interfaces;

import ru.practicum.events.dto.EventDto;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.EventUpdateDto;
import ru.practicum.events.model.SortBy;
import ru.practicum.events.model.State;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventFullDto createEvents(long userId, EventDto eventCreateDto);

    List<EventShortDto> getEventCreatedByUser(long userId, int from, int size);

    EventFullDto getEventByIdAndUserId(long userId, long eventId);

    EventFullDto updateEventByUser(long userId, long eventId, EventUpdateDto dto);

    List<EventShortDto> getEventsByParam(String text,
                                         List<Long> categories,
                                         Boolean paid,
                                         LocalDateTime rangeStart,
                                         boolean onlyAvailable,
                                         LocalDateTime rangeEnd,
                                         SortBy sort,
                                         int from,
                                         int size);

    EventFullDto getById(long id, String uri);

    List<EventFullDto> getAllEventByParameters(List<Long> users,
                                               List<State> states,
                                               List<Long> categories,
                                               LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd,
                                               int from,
                                               int size);

    EventFullDto updateEventByAdmin(long eventId, EventUpdateDto dto);
}