package ru.practicum.locations.service;

import ru.practicum.locations.dto.LocationDto;

import java.util.List;

public interface LocationService {

    LocationDto createLocation(LocationDto dto);

    LocationDto updateLocation(long locId, LocationDto dto);

    List<LocationDto> getAllLocations(int from, int size);

    LocationDto getLocationById(long locId);

    void deleteLocation(long locId);
}
