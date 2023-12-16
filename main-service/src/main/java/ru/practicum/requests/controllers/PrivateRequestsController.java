package ru.practicum.requests.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.service.RequestService;

import java.util.List;

@RestController
@RequestMapping({"/users/{userId}/requests"})
@Slf4j
@AllArgsConstructor
public class PrivateRequestsController {
    private final RequestService service;

    @GetMapping
    public List<ParticipationRequestDto> getRequestByUserId(@PathVariable long userId) {
        log.info("\nGET [http://localhost:8080/users/{userId}/requests] : " +
                "запрос на просмотр запросов пользователя с ID {} на участие в событиях\n", userId);
        return service.getRequestsByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@PathVariable long userId, @RequestParam long eventId) {
        log.info("\nPOST [http://localhost:8080/users/{}/requests?eventId={}] : " +
                "запрос на создание запроса на участие в событии {} от пользователя {}\n", userId, eventId, eventId, userId);
        return service.createRequest(userId, eventId);
    }

    @PatchMapping({"/{requestId}/cancel"})
    public ParticipationRequestDto updateRequest(@PathVariable long userId, @PathVariable long requestId) {
        log.info("\nPATCH [http://localhost:8080/users/{userId}/requests] : " +
                "запрос на отмену запроса {} на участие в событии пользователем с ID {}\n", requestId, userId);
        return service.updateRequest(userId, requestId);
    }
}
