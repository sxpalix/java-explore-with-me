package ru.practicum.compilations.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.compilations.dto.CompilationsDto;
import ru.practicum.compilations.service.CompilationsService;

import java.util.List;

@RestController
@RequestMapping({"/compilations"})
@Validated
@Slf4j
@AllArgsConstructor
public class CompilationsController {
    private final CompilationsService service;

    @GetMapping
    public List<CompilationsDto> getCompilationsOfEvent(@RequestParam(defaultValue = "false") boolean pinned, @RequestParam(defaultValue = "0") int from, @RequestParam(defaultValue = "10") int size) {
        log.info("\nGET [http://localhost:8080/compilations] : запрос на просмотр подборок событий\n");
        return this.service.getCompilationsOfEvent(pinned, from, size);
    }

    @GetMapping({"/{compId}"})
    public CompilationsDto getCompilationsOfEventById(@PathVariable long compId) {
        log.info("\nGET [http://localhost:8080/compilations/{}] : запрос для подборки событий по ID {}\n", compId, compId);
        return this.service.getCompilationsOfEventById(compId);
    }
}