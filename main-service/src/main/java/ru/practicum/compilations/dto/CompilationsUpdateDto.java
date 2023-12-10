package ru.practicum.compilations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Size;
import java.util.List;

@Validated
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CompilationsUpdateDto {
    List<Long> events;
    Boolean pinned;
    @Size(min = 1, max = 50)
    String title;
}
