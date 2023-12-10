package ru.practicum.requests.controllers;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.dto.PatchRequestDto;
import ru.practicum.requests.dto.PatchResponseDto;
import ru.practicum.requests.service.UserRequestService;

import java.util.List;

@RestController
@RequestMapping({"/users/{userId}/events/{eventId}/requests"})
@Validated
@Slf4j
@AllArgsConstructor
public class UserRequestController {
    private final UserRequestService service;

    @GetMapping
    public List<ParticipationRequestDto> getParticipateRequestsByEventCurrentUser(@PathVariable long userId,
                                                                                  @PathVariable long eventId) {
        log.info("\nGET [http://localhost:8080/users/{}/events/{}/requests] : " +
                "запрос от инициатора {} события {} на просмотр запросов на " +
                "участие в событии\n", userId, eventId, userId, eventId);
        return this.service.getParticipateRequestsByEventCurrentUser(userId, eventId);
    }

    @PatchMapping
    public PatchResponseDto patchParticipateRequestsByEventCurrentUser(@PathVariable long userId,
                                                                       @PathVariable long eventId,
                                                                       @RequestBody PatchRequestDto request) {
        log.info("\nPATCH [http://localhost:8080/users/{}/events/{}/requests] : " +
                "запрос на обновление статусов запросов на участие в событии {}\n", userId, eventId, request);
        return this.service.patchParticipateRequestsByEventCurrentUser(userId, eventId, request);
    }
}
