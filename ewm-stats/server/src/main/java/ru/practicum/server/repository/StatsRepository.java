package ru.practicum.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.server.model.Stats;
import ru.practicum.model.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Stats, Long> {

    @Query("SELECT new ru.practicum.model.dto.ViewStats(s.app, s.uri, COUNT(s.ip)) " +
            "FROM Stats AS s " +
            "WHERE s.timestamp BETWEEN (:start) AND (:end) " +
            "AND (COALESCE(:uris, null) IS NULL OR s.uri IN (:uris)) " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY 3 DESC ")
    List<ViewStats> findAllStatsByTimeAndUrisWhenUniqueFalse(@Param("start") LocalDateTime start,
                                                             @Param("end") LocalDateTime end,
                                                             @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.model.dto.ViewStats(s.app, s.uri, COUNT(distinct s.ip)) " +
            "FROM Stats AS s " +
            "WHERE s.timestamp BETWEEN (:start) AND (:end) " +
            "AND (COALESCE(:uris, null) IS NULL OR s.uri IN (:uris)) " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY 3 DESC ")
    List<ViewStats> findAllStatsByTimeAndUrisWhenUniqueTrue(@Param("start") LocalDateTime start,
                                                            @Param("end") LocalDateTime end,
                                                            @Param("uris") List<String> uris);
}
