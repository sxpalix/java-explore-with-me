package ru.practicum.compilations.service;

import java.util.List;

import ru.practicum.compilations.dto.CompilationCreateDto;
import ru.practicum.compilations.dto.CompilationsDto;
import ru.practicum.compilations.dto.CompilationsUpdateDto;

public interface CompilationsService {
    List<CompilationsDto> getCompilationsOfEvent(boolean pinned, int from, int size);

    CompilationsDto getCompilationsOfEventById(long compId);

    CompilationsDto createCompilations(CompilationCreateDto dto);

    void deleteCompilation(long compId);

    CompilationsDto updateCompilation(long compId, CompilationsUpdateDto dto);
}