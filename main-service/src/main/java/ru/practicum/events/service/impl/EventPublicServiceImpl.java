package ru.practicum.events.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.client.StatsClient;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.SortBy;
import ru.practicum.events.model.State;
import ru.practicum.events.repository.EventsRepository;
import ru.practicum.events.service.interfaces.EventPublicService;
import ru.practicum.exception.DateTimeException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.dto.ViewStats;
import ru.practicum.requests.model.ParticipationRequestState;
import ru.practicum.utils.Constants;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventPublicServiceImpl implements EventPublicService {
    private final EventsRepository repository;

    @Value("${STAT_SERVER_URL:http://localhost:9090}")
    private String statClientUrl;
    private StatsClient client;

    @PostConstruct
    private void init() {
        this.client = new StatsClient(this.statClientUrl);
    }

    public List<EventShortDto> getEventsByParam(String text,
                                                List<Long> categories,
                                                Boolean paid,
                                                LocalDateTime rangeStart,
                                                boolean onlyAvailable,
                                                LocalDateTime rangeEnd,
                                                SortBy sort, int from, int size) {
        List<Event> events = new ArrayList<>();
        if (rangeStart == null) {
            rangeStart = LocalDateTime.now();
        }

        if (rangeEnd == null) {
            rangeEnd = LocalDateTime.now().plusYears(1000L);
        }

        if (rangeEnd.isBefore(rangeStart)) {
            throw new DateTimeException("Start should be before end.");
        } else {
            switch (sort) {
                case VIEWS:
                    events = repository
                            .getEventsByParametersSortByViews(
                                    text, categories, paid, rangeStart, rangeEnd, PageRequest.of(from, size)).toList();
                    break;
                case EVENT_DATE:
                    events = repository
                            .getEventsByParametersSortByDate(
                                    text, categories, paid, rangeStart, rangeEnd, PageRequest.of(from, size)).toList();
            }

            if (onlyAvailable) {
                events = events.stream().filter((event) ->
                    event.getRequests().stream().filter((sizeEvent) ->
                        sizeEvent.getStatus().equals(ParticipationRequestState.CONFIRMED))
                            .count() < event.getParticipantLimit() || !event.getRequestModeration())
                        .collect(Collectors.toList());
            }

            return events.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
        }
    }

    public EventFullDto getById(long id, String uri) {
        Optional<Event> event = this.repository.findById(id);
        if (event.isEmpty()) {
            throw new NotFoundException(String.format("Event with id= %d not found", id));
        } else if (!event.get().getState().equals(State.PUBLISHED)) {
            throw new NotFoundException(String.format("Event with id= %d not found", id));
        } else {
            List<ViewStats> stats = client.findStats(Constants.MIN_TIME.format(Constants.DATE_TIME_FORMAT_PATTERN),
                    LocalDateTime.now().format(Constants.DATE_TIME_FORMAT_PATTERN), List.of(uri), true);
            EventFullDto dto = EventMapper.toFullDto(event.get());
            dto.setViews(stats.isEmpty() ? 0L : (stats.get(0)).getHits());
            return dto;
        }
    }
}
