package ru.practicum.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Data
@Validated
public class StatsHitDto {
    @NotBlank(message = "Параметр app не должен быть пустым")
    String app;
    @NotBlank(message = "Параметр uri не должен быть пустым")
    String uri;
    @NotBlank(message = "Параметр ip не должен быть пустым")
    String ip;
    LocalDateTime timestamp;
}
