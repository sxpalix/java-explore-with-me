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

    @Query("SELECT new ru.practicum.model.dto.ViewStats(s.app, s.uri, count(s.uri))" +
            "from Stats s where s.timestamp between :start and :end and s.uri in :uris" +
            " group by s.app, s.uri order by count(s.uri) desc")
    List<ViewStats> findAllStatsByTimeAndUrisWhenUniqueFalse(@Param("start") LocalDateTime start,
                                                             @Param("end") LocalDateTime end,
                                                             @Param("uris") List<String> uris);

    @Query("SELECT new ru.practicum.model.dto.ViewStats(s.app, s.uri, count(distinct s.ip))" +
            "from Stats s " +
            "where s.timestamp between :start and :end " +
            "and s.uri in :uris "+
            " group by s.app, s.uri order by count(distinct s.ip) desc")
    List<ViewStats> findAllStatsByTimeAndUrisWhenUniqueTrue(@Param("start") LocalDateTime start,
                                                            @Param("end") LocalDateTime end,
                                                            @Param("uris") List<String> uris);
}
