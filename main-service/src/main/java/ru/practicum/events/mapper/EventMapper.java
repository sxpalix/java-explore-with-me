package ru.practicum.events.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.categories.dto.CategoryMapper;
import ru.practicum.events.dto.EventDto;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.model.Event;
import ru.practicum.locations.dto.LocationMapper;
import ru.practicum.requests.model.ParticipationRequestState;
import ru.practicum.users.dto.UserMapper;

import java.time.format.DateTimeFormatter;

@UtilityClass
public class EventMapper {
    public static EventFullDto toFullDto(Event event) {
        EventFullDto dto = EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .createdOn(event.getCreatedOn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .id(event.getId())
                .initiator(UserMapper.fromUSerToShortDto(event.getInitiator()))
                .location(LocationMapper.toLocationDto(event.getLocation())).paid(event.getPaid())
                .participantLimit(event.getParticipantLimit()).requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle()).build();
        if (event.getPublishedOn() != null) {
            dto.setPublishedOn(event.getPublishedOn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }

        if (event.getRequests() != null) {
            dto.setConfirmedRequests(event.getRequests().stream().filter((request) ->
                request.getStatus().equals(ParticipationRequestState.CONFIRMED)).count());
        }

        return dto;
    }

    public static Event fromEventDtoToEvent(EventDto dto) {
        return Event.builder()
                .annotation(dto.getAnnotation())
                .description(dto.getDescription())
                .eventDate(dto.getEventDate())
                .paid(dto.getPaid())
                .participantLimit(dto.getParticipantLimit())
                .requestModeration(dto.getRequestModeration())
                .title(dto.getTitle()).build();
    }

    public static EventShortDto toEventShortDto(Event event) {
        EventShortDto dto = EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.fromUSerToShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .build();
        if (!event.getRequests().isEmpty()) {
            dto.setConfirmedRequests(event.getRequests()
                    .stream().filter((request) -> request.getStatus()
                            .equals(ParticipationRequestState.CONFIRMED)).count());
        }

        return dto;
    }
}
