package ru.practicum.compilations.controllers;
import javax.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.compilations.dto.CompilationCreateDto;
import ru.practicum.compilations.dto.CompilationsDto;
import ru.practicum.compilations.dto.CompilationsUpdateDto;
import ru.practicum.compilations.service.CompilationsService;

@RestController
@RequestMapping({"/admin/compilations"})
@Slf4j
@AllArgsConstructor
public class CompilationsAdminController {
    private final CompilationsService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationsDto createCompilations(@RequestBody @Valid CompilationCreateDto dto) {
        log.info("\nPOST [http://localhost:8080/admin/compilations] : запрос на создание подборки событий {}\n", dto);
        return service.createCompilations(dto);
    }

    @DeleteMapping({"/{compId}"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilationsById(@PathVariable int compId) {
        log.info("\nDELETE [http://localhost:8080/admin/compilations/{}] : запрос на удаление подборки событий с ID {}\n",
                compId, compId);
        service.deleteCompilation(compId);
    }

    @PatchMapping({"/{compId}"})
    public CompilationsDto updateCompilation(@PathVariable long compId, @RequestBody @Valid CompilationsUpdateDto dto) {
        log.info("\nPATCH [http://localhost:8080/admin/compilations/{}] : запрос на обновление подборки событий {} с ID {}\n",
                compId, dto, compId);
        return service.updateCompilation(compId, dto);
    }
}
