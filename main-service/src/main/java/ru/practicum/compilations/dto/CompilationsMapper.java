package ru.practicum.compilations.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.compilations.model.Compilations;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.mapper.EventMapper;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationsMapper {
    public static CompilationsDto toDto(Compilations compilations) {
        List<EventShortDto> shortEventDto = compilations.getEvents().stream()
                .map(EventMapper::toEventShortDto).collect(Collectors.toList());
        return CompilationsDto.builder()
                .id(compilations.getId())
                .title(compilations.getTitle())
                .pinned(compilations.getPinned())
                .events(shortEventDto)
                .build();
    }
}
