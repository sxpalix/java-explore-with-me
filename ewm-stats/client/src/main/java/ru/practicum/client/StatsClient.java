package ru.practicum.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import ru.practicum.model.dto.StatsHitDto;
import ru.practicum.model.dto.ViewStats;

import java.util.List;

@Slf4j
public class StatsClient {
    private final WebClient client;

    public StatsClient(String baseUrl) {
        client = WebClient.builder().baseUrl(baseUrl).build();
    }

    public void postHit(StatsHitDto hit) {
        client.post()
                .uri("/hit")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(hit))
                .retrieve()
                .bodyToMono(Void.class)
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
