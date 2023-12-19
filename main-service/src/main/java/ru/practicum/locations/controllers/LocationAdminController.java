package ru.practicum.locations.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.locations.dto.LocationDto;
import ru.practicum.locations.service.LocationService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/locations")
public class LocationAdminController {
    private final LocationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LocationDto addNewLocation(@RequestBody @Valid LocationDto locationDto) {
        log.info("Post-запрос: добавление админом новой локации - {}", locationDto);
        return service.createLocation(locationDto);
    }

    @PatchMapping("/{locId}")
    public LocationDto updatePublicLocation(@PathVariable long locId,
                                            @RequestBody LocationDto updatedLocationDto) {
        updatedLocationDto.setId(locId);
        log.info("Patch-запрос: обновление публичной локации, новые данные - {}", updatedLocationDto);
        return service.updateLocation(locId, updatedLocationDto);
    }

    @DeleteMapping("/{locId}")
    public void deleteLocationById(@PathVariable long locId) {
        log.info("Delete-запрос: удаление локации {} админом", locId);
        service.deleteLocation(locId);
    }
}
