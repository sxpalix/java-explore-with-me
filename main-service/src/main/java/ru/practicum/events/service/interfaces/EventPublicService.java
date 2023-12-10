package ru.practicum.events.service.interfaces;

import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.model.SortBy;

import java.time.LocalDateTime;
import java.util.List;

public interface EventPublicService {
    List<EventShortDto> getEventsByParam(String text,
                                         List<Long> categories,
                                         Boolean paid,
                                         LocalDateTime rangeStart,
                                         boolean onlyAvailable,
                                         LocalDateTime rangeEnd,
                                         SortBy sort,
                                         int from,
                                         int size);

    EventFullDto getById(@PathVariable long id, String uri);
}