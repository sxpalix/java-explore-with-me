package ru.practicum.compilations.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.compilations.dto.CompilationsDto;
import ru.practicum.compilations.dto.CompilationsMapper;
import ru.practicum.compilations.model.Compilations;
import ru.practicum.compilations.repository.CompilationsRepository;
import ru.practicum.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompilationsServiceImpl implements CompilationsService {
    private final CompilationsRepository repository;

    public List<CompilationsDto> getCompilationsOfEvent(boolean pinned, int from, int size) {
        return repository.findAllByPinned(pinned, PageRequest.of(from, size))
                .stream()
                .map(CompilationsMapper::toDto)
                .collect(Collectors.toList());
    }

    public CompilationsDto getCompilationsOfEventById(long compId) {
        Compilations compilation = repository.findById(compId).orElseThrow(() ->
            new NotFoundException(String.format("Compilation with id=%d not found", compId)));
        return CompilationsMapper.toDto(compilation);
    }
}
