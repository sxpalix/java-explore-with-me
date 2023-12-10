package ru.practicum.compilations.service;

import ru.practicum.compilations.dto.CompilationCreateDto;
import ru.practicum.compilations.dto.CompilationsDto;
import ru.practicum.compilations.dto.CompilationsUpdateDto;

public interface CompilationsAdminService {
    CompilationsDto postCompilations(CompilationCreateDto dto);

    void deleteCompilation(long compId);

    CompilationsDto patchCompilation(long compId, CompilationsUpdateDto dto);
}
