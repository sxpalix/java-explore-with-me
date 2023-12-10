package ru.practicum.compilations.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.compilations.model.Compilations;

@UtilityClass
public class CompilationsMapper {
    public static CompilationsDto toDto(Compilations compilations) {
        return CompilationsDto.builder()
                .id(compilations.getId())
                .title(compilations.getTitle())
                .pinned(compilations.getPinned())
                .events(compilations.getEvents())
                .build();
    }
}
