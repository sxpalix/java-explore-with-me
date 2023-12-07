package ru.practicum.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewStats {

    @NotBlank(message = "Параметр app не должен быть пустым")
    private String app;

    @NotBlank(message = "Параметр uri не должен быть пустым")
    private String uri;
    private Long hits;
}
