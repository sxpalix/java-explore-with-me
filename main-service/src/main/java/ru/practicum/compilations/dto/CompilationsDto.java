package ru.practicum.compilations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.events.dto.EventShortDto;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CompilationsDto {
    private Long id;
    private String title;
    private Boolean pinned;
    private List<EventShortDto> events;
}
