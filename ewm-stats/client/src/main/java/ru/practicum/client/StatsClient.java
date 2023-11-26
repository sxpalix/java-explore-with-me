package ru.practicum.client;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.model.dto.StatsHitDto;
import ru.practicum.model.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class StatsClient {
    private final WebClient client;

    public ResponseEntity<StatsHitDto> postHit(StatsHitDto dto) {
        return client.post()
                .uri(String.join("", "/hit"))
                .bodyValue(dto)
                .retrieve()
                .toEntity(StatsHitDto.class)
                .doOnSuccess(s -> log.info("web client. successful transition of dto = [app = {}, uri = {}, ip = {}, timestamp = {}]",
                        dto.getApp(), dto.getUri(), dto.getIp(), dto.getTimestamp()))
                .doOnError(s -> log.info("Error create hitStats"))
                .block();
    }

    public ResponseEntity<List<ViewStats>> findStats(LocalDateTime start,
                                                     LocalDateTime end,
                                                     List<String> uris,
                                                     Boolean unique) {
        return this.client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(String.join("", "/hit"))
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .queryParam("uris", uris)
                        .queryParam("unique", unique)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntityList(ViewStats.class)
                .doOnSuccess(s -> log.info("web client. successful transition of dto = [start = {}, end = {}, uris = {}, unique = {}]",
                        start, end, uris, unique))
                .doOnError(s -> log.info("web client. error during transition"))
                .block();

    }
}
