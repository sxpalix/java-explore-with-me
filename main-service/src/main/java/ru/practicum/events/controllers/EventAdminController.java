package ru.practicum.events.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventUpdateDto;
import ru.practicum.events.model.State;
import ru.practicum.events.service.interfaces.AdminEventService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping({"/admin/events"})
@Validated
@AllArgsConstructor
@Slf4j
public class EventAdminController {
    private final AdminEventService service;

    @GetMapping
    public List<EventFullDto> getAllEventByParameters(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<State> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("\nGET [http://localhost:8080/admin/events?users={}&states={}" +
                "&categories={}&rangeStart={}&rangeEnd={}&from={}&size={}] : " +
                "запрос от администратора на просмотр событий по фильтрам\n",
                users, states, categories, rangeStart, rangeEnd, from, size);
        return this.service.getAllEventByParameters(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping({"/{eventId}"})
    public EventFullDto patchEventByAdmin(@PathVariable long eventId, @RequestBody @Valid EventUpdateDto dto) {
        log.info("\nPATCH [http://localhost:8080/admin/events/{}] : " +
                "запрос на обновление события с ID {} администратором \n{}\n", eventId, eventId, dto);
        return this.service.patchEventByAdmin(eventId, dto);
    }
}
