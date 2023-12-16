package ru.practicum.requests.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.dto.PatchRequestDto;
import ru.practicum.requests.dto.PatchResponseDto;
import ru.practicum.requests.service.RequestService;

import java.util.List;

@RestController
@RequestMapping({"/users/{userId}/events/{eventId}/requests"})
@Slf4j
@AllArgsConstructor
public class UserRequestController {
    private final RequestService service;

    @GetMapping
    public List<ParticipationRequestDto> getParticipateRequestsByEventCurrentUser(@PathVariable long userId,
                                                                                  @PathVariable long eventId) {
        log.info("\nGET [http://localhost:8080/users/{}/events/{}/requests] : " +
                "запрос от инициатора {} события {} на просмотр запросов на " +
                "участие в событии\n", userId, eventId, userId, eventId);
        return service.getParticipateRequestsByEventCurrentUser(userId, eventId);
    }

    @PatchMapping
    public PatchResponseDto updateParticipateRequestsByEventCurrentUser(@PathVariable long userId,
                                                                       @PathVariable long eventId,
                                                                       @RequestBody PatchRequestDto request) {
        log.info("\nPATCH [http://localhost:8080/users/{}/events/{}/requests] : " +
                "запрос на обновление статусов запросов на участие в событии {}\n", userId, eventId, request);
        return service.updateParticipateRequestsByEventCurrentUser(userId, eventId, request);
    }
}
