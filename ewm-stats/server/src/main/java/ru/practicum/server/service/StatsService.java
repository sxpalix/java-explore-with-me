package ru.practicum.server.service;

import ru.practicum.model.dto.StatsHitDto;
import ru.practicum.model.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    StatsHitDto postHit(StatsHitDto dto);

    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
