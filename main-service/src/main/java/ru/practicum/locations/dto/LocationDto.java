package ru.practicum.locations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LocationDto {
    private long id;
    @Size(min = 1, max = 100)
    private String name;
    private float lat;
    private float lon;
    private float radius;
}
