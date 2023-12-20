package ru.practicum.events.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.locations.dto.LocationCreationDto;
import ru.practicum.requests.repository.RequestsRepository;
import ru.practicum.utils.Constants;
import ru.practicum.categories.model.Category;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.client.StatsClient;
import ru.practicum.events.dto.EventDto;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.EventUpdateDto;
import ru.practicum.events.mapper.EventMapper;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.SortBy;
import ru.practicum.events.model.State;
import ru.practicum.events.model.StateAction;
import ru.practicum.events.repository.EventsRepository;
import ru.practicum.events.service.interfaces.EventService;
import ru.practicum.exception.DateTimeException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.RequestException;
import ru.practicum.locations.dto.LocationMapper;
import ru.practicum.locations.model.Location;
import ru.practicum.locations.repository.LocationRepository;
import ru.practicum.model.dto.ViewStats;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventsRepository repository;
    private final UserRepository userRepository;
    private final CategoryRepository categoriesRepository;
    private final LocationRepository locationRepository;
    private final RequestsRepository requestsRepository;

    @Value("${STAT_SERVER_URL:http://localhost:9090}")
    private String statClientUrl;
    private StatsClient client;

    @PostConstruct
    private void init() {
        client = new StatsClient(statClientUrl);
    }

    @Transactional
    public EventFullDto createEvents(long userId, EventDto eventCreateDto) {
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
        if (eventCreateDto.getLocation().getId() != 0L)
            eventToCreate.setLocation(locationRepository.findById(eventCreateDto.getLocation().getId()).get());
            else eventToCreate.setLocation(this.createNewLocation(eventCreateDto.getLocation()));

        eventToCreate.setCategory(getCategoryById(eventCreateDto.getCategory()));
        eventToCreate.setInitiator(getUserById(userId));
        eventToCreate.setCreatedOn(LocalDateTime.now());
        eventToCreate.setState(State.PENDING);
        eventToCreate.setConfirmedRequests(0L);
        return EventMapper.toFullDto(repository.save(eventToCreate));
    }

    public List<EventShortDto> getEventCreatedByUser(long userId, int from, int size) {
        getUserById(userId);
        return repository.findAllByInitiator_Id(userId, PageRequest.of(from > 0 ? from / size : 0, size))
                .stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    public EventFullDto getEventByIdAndUserId(long userId, long eventId) {
        getUserById(userId);
        Event event = repository.findEventByIdAndInitiator_Id(eventId, userId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id= %d was not found", eventId)));
        return EventMapper.toFullDto(repository.save(event));
    }

    @Transactional
    public EventFullDto updateEventByUser(long userId, long eventId, EventUpdateDto dto) {
        getUserById(userId);
        Event event = repository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id= %d was not found", eventId)));

        if (event.getState().equals(State.PUBLISHED)) {
            throw new RequestException("cannot change an event with a status PUBLISHED");
        }
        Event updatedEvent = returnUpdatedEvent(event, dto);
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
        }

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
                            event.getConfirmedRequests() < event.getParticipantLimit() || !event.getRequestModeration())
                    .collect(Collectors.toList());
        }

        return events.stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
    }

    public EventFullDto getById(long id, String uri) {
        Event event = repository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Event with id= %d was not found", id)));

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new NotFoundException(String.format("Event with id= %d not found", id));
        }

        List<ViewStats> stats = client.findStats(Constants.MIN_TIME.format(Constants.DATE_TIME_FORMAT_PATTERN),
                LocalDateTime.now().format(Constants.DATE_TIME_FORMAT_PATTERN), List.of(uri), true);
        EventFullDto dto = EventMapper.toFullDto(event);
        dto.setViews(stats.isEmpty() ? 0L : (stats.get(0)).getHits());
        return dto;

    }

    private User getUserById(long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(String.format("User with id= %s was not found", userId)));
    }

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
    public EventFullDto updateEventByAdmin(long eventId, EventUpdateDto dto) {
        Event event = repository.findById(eventId).orElseThrow(() ->
                new NotFoundException(String.format("Event with id= %d was not found", eventId)));

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

        Event updated = returnUpdatedEvent(event, dto);
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

    public List<EventShortDto> getEventByLocation(float distance, float lat, float lon) {
        if ((double)lat == 0.0) {
            throw new RequestException("Lat mustn't be 0.0");
        } else if ((double)lon == 0.0) {
            throw new RequestException("Lon mustn't be 0.0");
        } else {
            return repository.findEventsByLocationZone(lat, lon, distance)
                    .stream().map(EventMapper::toEventShortDto).collect(Collectors.toList());
        }
    }

    private Event returnUpdatedEvent(Event event, EventUpdateDto dto) {
        if (dto.getAnnotation() != null) event.setAnnotation(dto.getAnnotation());

        if (dto.getCategory() != null) event.setCategory(getCategoryById(dto.getCategory()));
        if (dto.getDescription() != null) event.setDescription(dto.getDescription());
        if (dto.getEventDate() != null) event.setEventDate(dto.getEventDate());

        if (dto.getLocation() != null &&
                dto.getLocation().equals(LocationMapper.fromLocationToCreateDto(event.getLocation()))) {
            event.setLocation(this.createNewLocation(dto.getLocation()));
        }

        if (dto.getPaid() != null) event.setPaid(dto.getPaid());
        if (dto.getParticipantLimit() != null) event.setParticipantLimit(dto.getParticipantLimit());
        if (dto.getRequestModeration() != null) event.setRequestModeration(dto.getRequestModeration());
        if (dto.getTitle() != null) event.setTitle(dto.getTitle());

        return event;
    }

    private Category getCategoryById(long categoryId) {
        return categoriesRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException(String.format("User with id= %s was not found", categoryId)));
    }

    private Location createNewLocation(LocationCreationDto dto) {
        return locationRepository.save(LocationMapper.fromLocationCreateToLocation(dto));
    }
}
