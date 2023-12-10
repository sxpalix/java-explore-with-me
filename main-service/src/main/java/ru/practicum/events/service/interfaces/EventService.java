package ru.practicum.events.service.interfaces;

import ru.practicum.events.dto.EventDto;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.EventUpdateDto;

import java.util.List;

public interface EventService {
    EventFullDto postEvents(long userId, EventDto eventCreateDto);

    List<EventShortDto> getEventCreatedByUser(long userId, int from, int size);

    EventFullDto getEventByIdAndUserId(long userId, long eventId);

    EventFullDto updateEventByUser(long userId, long eventId, EventUpdateDto dto);
}