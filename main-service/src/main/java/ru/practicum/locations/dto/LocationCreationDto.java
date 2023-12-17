package ru.practicum.locations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LocationCreationDto {
    private long id;
    private float lat;
    private float lon;
    private float radius;
}
