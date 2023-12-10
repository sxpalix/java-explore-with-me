package ru.practicum.requests.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.service.PrivateRequestsService;

import java.util.List;

@RestController
@RequestMapping({"/users/{userId}/requests"})
@Validated
@Slf4j
@AllArgsConstructor
public class PrivateRequestsController {
    private final PrivateRequestsService service;

    @GetMapping
    public List<ParticipationRequestDto> getRequestByUserId(@PathVariable long userId) {
        log.info("\nGET [http://localhost:8080/users/{userId}/requests] : " +
                "запрос на просмотр запросов пользователя с ID {} на участие в событиях\n", userId);
        return this.service.getRequestsByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto postRequest(@PathVariable long userId, @RequestParam long eventId) {
        log.info("\nPOST [http://localhost:8080/users/{}/requests?eventId={}] : " +
                "запрос на создание запроса на участие в событии {} от пользователя {}\n", userId, eventId, eventId, userId);
        return this.service.postRequest(userId, eventId);
    }

    @PatchMapping({"/{requestId}/cancel"})
    public ParticipationRequestDto patchRequest(@PathVariable long userId, @PathVariable long requestId) {
        log.info("\nPATCH [http://localhost:8080/users/{userId}/requests] : " +
                "запрос на отмену запроса {} на участие в событии пользователем с ID {}\n", requestId, userId);
        return this.service.updateRequest(userId, requestId);
    }
}
