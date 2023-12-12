package ru.practicum.compilations.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilations.dto.CompilationCreateDto;
import ru.practicum.compilations.dto.CompilationsDto;
import ru.practicum.compilations.dto.CompilationsMapper;
import ru.practicum.compilations.dto.CompilationsUpdateDto;
import ru.practicum.compilations.model.Compilations;
import ru.practicum.compilations.repository.CompilationsRepository;
import ru.practicum.events.model.Event;
import ru.practicum.events.repository.EventsRepository;
import ru.practicum.exception.NotFoundException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompilationsServiceImpl implements CompilationsService {
    private final CompilationsRepository repository;
    private final EventsRepository eventsRepository;

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

    @Transactional
    public CompilationsDto createCompilations(CompilationCreateDto dto) {
        List<Event> events = new ArrayList<>();
        if (dto.getEvents() != null && !dto.getEvents().isEmpty()) {
            events = eventsRepository.findAllById(dto.getEvents());
        }

        if (dto.getPinned() == null) {
            dto.setPinned(false);
        }

        Compilations compilation = Compilations.builder()
                .title(dto.getTitle())
                .pinned(dto.getPinned())
                .events(new HashSet<>(events))
                .build();
        return CompilationsMapper.toDto(repository.save(compilation));
    }

    @Transactional
    public void deleteCompilation(long compId) {
        if (!repository.existsById(compId))
            throw new NotFoundException(String.format("Compilations with id= %d not found", compId));

        repository.deleteById(compId);
    }

    @Transactional
    public CompilationsDto updateCompilation(long compId, CompilationsUpdateDto dto) {
        Compilations compilations = repository.findById(compId).orElseThrow(() ->
                new NotFoundException(String.format("Compilations with id= %d not found", compId)));

        Optional.ofNullable(dto.getTitle()).ifPresent(compilations::setTitle);
        Optional.ofNullable(dto.getPinned()).ifPresent(compilations::setPinned);

        if (dto.getEvents() != null && !dto.getEvents().isEmpty()) {
            compilations.setEvents(new HashSet<>(eventsRepository.findAllById(dto.getEvents())));
        }

        return CompilationsMapper.toDto(repository.save(compilations));
    }
}
