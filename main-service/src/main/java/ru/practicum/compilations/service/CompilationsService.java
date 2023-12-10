package ru.practicum.compilations.service;

import java.util.List;
import ru.practicum.compilations.dto.CompilationsDto;

public interface CompilationsService {
    List<CompilationsDto> getCompilationsOfEvent(boolean pinned, int from, int size);

    CompilationsDto getCompilationsOfEventById(long compId);
}