package ru.practicum.server.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.ValidationException;
import ru.practicum.server.model.Stats;
import ru.practicum.model.dto.StatsHitDto;
import ru.practicum.server.model.StatsMapper;
import ru.practicum.model.dto.ViewStats;
import ru.practicum.server.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;


@Service
@AllArgsConstructor
@Transactional
public class StatsServiceImpl implements StatsService {
    private final StatsRepository repository;

    @Override
    @Transactional
    public StatsHitDto postHit(StatsHitDto dto) {
        Stats stats = StatsMapper.toStats(dto);
        return StatsMapper.toStatsDto(repository.save(stats));
    }

    @Override
    @Transactional
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (end.isBefore(start) || start.isAfter(end)) {
            throw new ValidationException("Время старта должно быть раньше времени окончания хитов");
        }

        if (unique) return repository.findAllStatsByTimeAndUrisWhenUniqueTrue(start, end, uris);
         else return repository.findAllStatsByTimeAndUrisWhenUniqueFalse(start, end, uris);
    }
}
