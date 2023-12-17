package ru.practicum.locations.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.locations.model.Location;


@UtilityClass
public class LocationMapper {
    public static LocationCreationDto fromLocationToCreateDto(Location location) {
        return LocationCreationDto.builder()
                .radius(location.getRadius())
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }

    public static LocationEventDto fromLocationToEventDto(Location location) {
        return LocationEventDto.builder()
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }

    public static LocationDto fromLocationToDto(Location location) {
        return LocationDto.builder()
                .id(location.getId())
                .radius(location.getRadius())
                .name(location.getName())
                .lat(location.getLat())
                .lon(location.getLon())
                .build();
    }

    public static Location fromLocationCreateToLocation(LocationCreationDto dto) {
        return Location.builder()
                .radius(dto.getRadius())
                .lat(dto.getLat())
                .lon(dto.getLon())
                .build();
    }

    public static Location fromLocationDtoToLocation(LocationDto dto) {
        return Location.builder()
                .name(dto.getName())
                .radius(dto.getRadius())
                .lat(dto.getLat())
                .lon(dto.getLon())
                .build();
    }
}
