package ru.practicum.events.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventDto;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.EventUpdateDto;
import ru.practicum.events.service.interfaces.EventService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping({"/users/{userId}/events"})
@Validated
@Slf4j
@AllArgsConstructor
public class EventPrivateController {
    private final EventService service;

    @GetMapping
    public List<EventShortDto> getEventsByUserId(@PathVariable long userId,
                                                 @RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "10") int size) {
        log.info("\nGET [http://localhost:8080/users/{}/events] : запрос на просмотр событий, " +
                "добавленных пользователем с ID {}\n", userId, userId);
        return this.service.getEventCreatedByUser(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto postEvents(@PathVariable long userId, @RequestBody @Valid EventDto eventDto) {
        log.info("\nPOST [http://localhost:8080/users/{}/events] : " +
                "запрос на добавление события {} от пользователя с ID {}\n", userId, eventDto, userId);
        return this.service.postEvents(userId, eventDto);
    }

    @GetMapping({"/{eventId}"})
    public EventFullDto getEventByUserAndId(@PathVariable long userId, @PathVariable long eventId) {
        log.info("\nGET [http://localhost:8080/users/{}/events/{}] : запрос на просмотр события {}, " +
                "добавленного пользователем с ID {}\n", userId, eventId, userId, eventId);
        return this.service.getEventByIdAndUserId(userId, eventId);
    }

    @PatchMapping({"/{eventId}"})
    public EventFullDto patchEventByUserAndId(@PathVariable long userId,
                                              @PathVariable long eventId,
                                              @RequestBody @Valid EventUpdateDto eventDto) {
        log.info("\nPATCH [http://localhost:8080/users/{}/events/{}/requests] : " +
                "запрос на обновление статусов запросов на участие в событии {}\n", userId, eventId, eventDto);
        return this.service.updateEventByUser(userId, eventId, eventDto);
    }
}
