package ru.practicum.events.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.events.dto.EventUpdateDto;
import ru.practicum.events.model.Event;
import ru.practicum.exception.NotFoundException;
import ru.practicum.locations.dto.LocationDto;
import ru.practicum.locations.dto.LocationMapper;
import ru.practicum.locations.model.Location;
import ru.practicum.locations.repository.LocationRepository;

@Service
@AllArgsConstructor
public class UpdateHelper {
    private final CategoriesRepository categoriesRepository;
    private final LocationRepository locationRepository;

    public Event updateEvent(Event event, EventUpdateDto dto) {
        if (dto.getAnnotation() != null) event.setAnnotation(dto.getAnnotation());

        if (dto.getCategory() != null) event.setCategory(this.getCategoryById(dto.getCategory()));
        if (dto.getDescription() != null) event.setDescription(dto.getDescription());
        if (dto.getEventDate() != null) event.setEventDate(dto.getEventDate());

        if (dto.getLocation() != null &&
                dto.getLocation().equals(LocationMapper.toLocationDto(event.getLocation())))
            event.setLocation(this.createNewLocation(dto.getLocation()));

        if (dto.getPaid() != null) event.setPaid(dto.getPaid());
        if (dto.getParticipantLimit() != null) event.setParticipantLimit(dto.getParticipantLimit());
        if (dto.getRequestModeration() != null) event.setRequestModeration(dto.getRequestModeration());
        if (dto.getTitle() != null) event.setTitle(dto.getTitle());

        return event;
    }

    public Category getCategoryById(long categoryId) {
        return categoriesRepository.findById(categoryId).orElseThrow(() ->
            new NotFoundException(String.format("User with id= %s was not found", categoryId)));
    }

    public Location createNewLocation(LocationDto dto) {
        return locationRepository.save(LocationMapper.toLocation(dto));
    }
}
