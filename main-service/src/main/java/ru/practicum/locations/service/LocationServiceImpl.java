package ru.practicum.locations.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.RequestException;
import ru.practicum.locations.dto.LocationDto;
import ru.practicum.locations.dto.LocationMapper;
import ru.practicum.locations.model.Location;
import ru.practicum.locations.repository.LocationRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository repository;

    public LocationDto createLocation(LocationDto dto) {
        if (repository.findByLatAndLonAndRadius(dto.getLat(), dto.getLon(), dto.getRadius()).isPresent())
            throw new RequestException("Локация уже существует");
        Location location = LocationMapper.fromLocationDtoToLocation(dto);

        return LocationMapper.fromLocationToDto(repository.save(location));
    }

    public LocationDto updateLocation(long locId, LocationDto dto) {
        Location location = getLocation(locId);

        if (dto.getName() != null) location.setName(dto.getName());
        if (dto.getLat() != 0.0) location.setLat(dto.getLat());
        if (dto.getLon() != 0.0) location.setLon(dto.getLon());
        if (dto.getRadius() != 0.0) location.setRadius(dto.getRadius());

        return LocationMapper.fromLocationToDto(repository.save(location));
    }

    public List<LocationDto> getAllLocations(int from, int size) {
        Pageable page = PageRequest.of(from, size);
        List<Location> locations = repository.findByNameIsNotNull(page);
        return locations.stream().map(LocationMapper::fromLocationToDto).collect(Collectors.toList());
    }

    public LocationDto getLocationById(long locId) {
        Location location = getLocation(locId);
        return LocationMapper.fromLocationToDto(location);
    }

    @Override
    public void deleteLocation(long locId) {
        getLocation(locId);
        repository.deleteById(locId);
    }

    private Location getLocation(long locId) {
        return repository.findById(locId)
                .orElseThrow(() -> new NotFoundException(String.format("Локация с id= %d not found", locId)));
    }
}
