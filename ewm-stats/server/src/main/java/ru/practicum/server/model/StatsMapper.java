package ru.practicum.server.model;

import lombok.experimental.UtilityClass;
import ru.practicum.model.dto.StatsHitDto;


@UtilityClass
public class StatsMapper {
    public StatsHitDto toStatsDto(Stats stats) {
        return StatsHitDto.builder()
                .app(stats.getApp())
                .uri(stats.getUri())
                .ip(stats.getIp())
                .timestamp(stats.getTimestamp())
                .build();
    }

    public Stats toStats(StatsHitDto dto) {
        return Stats.builder()
                .app(dto.getApp())
                .uri(dto.getUri())
                .ip(dto.getIp())
                .timestamp(dto.getTimestamp())
                .build();
    }
}
