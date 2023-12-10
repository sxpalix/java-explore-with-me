package ru.practicum.compilations.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
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

@Service
@Transactional
@AllArgsConstructor
public class CompilationsAdminServiceImpl implements CompilationsAdminService {
    private final CompilationsRepository repository;
    private final EventsRepository eventsRepository;

    @Transactional
    public CompilationsDto postCompilations(CompilationCreateDto dto) {
        List<Event> events = new ArrayList<>();
        if (dto.getEvents() != null && !dto.getEvents().isEmpty()) {
            events = this.eventsRepository.findAllById(dto.getEvents());
        }

        if (dto.getPinned() == null) {
            dto.setPinned(false);
        }

        Compilations compilation = Compilations.builder()
                .title(dto.getTitle())
                .pinned(dto.getPinned())
                .events(events)
                .build();
        return CompilationsMapper.toDto(repository.save(compilation));
    }

    @Transactional
    public void deleteCompilation(long compId) {
        Optional<Compilations> compilations = this.repository.findById(compId);
        if (compilations.isEmpty()) {
            throw new NotFoundException(String.format("Compilations with id= %d not found", compId));
        } else {
            this.repository.deleteById(compId);
        }
    }

    @Transactional
    public CompilationsDto patchCompilation(long compId, CompilationsUpdateDto dto) {
        Compilations compilations = repository.findById(compId).orElseThrow(() ->
            new NotFoundException(String.format("Compilations with id= %d not found", compId)));

        Optional.ofNullable(dto.getTitle()).ifPresent(compilations::setTitle);
        Optional.ofNullable(dto.getPinned()).ifPresent(compilations::setPinned);

        if (dto.getEvents() != null && !dto.getEvents().isEmpty()) {
            compilations.setEvents(eventsRepository.findAllById(dto.getEvents()));
        }

        return CompilationsMapper.toDto(repository.save(compilations));
    }
}
