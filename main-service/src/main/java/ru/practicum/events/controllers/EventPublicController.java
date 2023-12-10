package ru.practicum.events.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.StatsClient;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.model.SortBy;
import ru.practicum.events.service.interfaces.EventPublicService;
import ru.practicum.model.dto.StatsHitDto;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping({"/events"})
@Validated
public class EventPublicController {
    private final EventPublicService service;

    @Value("${STAT_SERVER_URL:http://localhost:9090}")
    private String statClientUrl;
    private StatsClient client;

    @PostConstruct
    private void init() {
        this.client = new StatsClient(this.statClientUrl);
    }

    @GetMapping
    public List<EventShortDto> getEventsByParam(
            @RequestParam(defaultValue = "") @Size(max = 2000) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "false") boolean onlyAvailable, @RequestParam(defaultValue = "VIEWS") SortBy sort,
            @RequestParam(defaultValue = "0") int from, @RequestParam(defaultValue = "10") int size,
            HttpServletRequest httpServletRequest) {
        log.info("\nGET [http://localhost:8080/events] : запрос на просмотр событий по фильтрам\n");
        this.client.postHit(StatsHitDto.builder()
                .app("ewm-main-server")
                .ip(httpServletRequest.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .uri(httpServletRequest.getRequestURI())
                .build());
        return this.service.getEventsByParam(text, categories, paid, rangeStart, onlyAvailable, rangeEnd, sort, from, size);
    }

    @GetMapping({"/{id}"})
    public EventFullDto getById(@PathVariable long id, HttpServletRequest httpServletRequest) {
        log.info("\nGET [http://localhost:8080/events/{}] : запрос на просмотр события по ID {}\n", id, id);
        this.client.postHit(StatsHitDto.builder()
                .app("ewm-main-server")
                .ip(httpServletRequest.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .uri(httpServletRequest.getRequestURI())
                .build());
        return this.service.getById(id, httpServletRequest.getRequestURI());
    }
}
