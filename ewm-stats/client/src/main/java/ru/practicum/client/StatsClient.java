package ru.practicum.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.model.dto.StatsHitDto;
import ru.practicum.model.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
public class StatsClient {
    private final WebClient client;

    public StatsClient(String baseUrl) {
        client = WebClient.builder().baseUrl(baseUrl).build();
    }

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

    public List<ViewStats> findStats(String start,
                                     String end,
                                     List<String> uris,
                                     Boolean unique) {
        String urisString = String.join(",", uris);

        return client.get()
                .uri("/stats?start={start}&end={end}&uris={uris}&unique={unique}", start, end, urisString, unique)
                .retrieve()
                .bodyToFlux(ViewStats.class)
                .collectList()
                .block();
    }
}
