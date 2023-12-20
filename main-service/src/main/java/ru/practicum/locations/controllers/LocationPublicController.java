package ru.practicum.locations.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.locations.dto.LocationDto;
import ru.practicum.locations.service.LocationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/locations")
public class LocationPublicController {
    private final LocationService service;

    @GetMapping
    public List<LocationDto> getAllLocations(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                             @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Get-запрос: получение всех добавленных админом локаций");
        return service.getAllLocations(from, size);
    }

    @GetMapping("/{locId}")
    public LocationDto getLocationById(@PathVariable @Positive long locId) {
        log.info("Get-запрос: получение добавленной админом локации по id {}", locId);
        return service.getLocationById(locId);
    }
}
