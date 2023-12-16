package ru.practicum.requests.service;

import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.dto.PatchRequestDto;
import ru.practicum.requests.dto.PatchResponseDto;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> getParticipateRequestsByEventCurrentUser(long userId, long eventId);

    PatchResponseDto updateParticipateRequestsByEventCurrentUser(long userId, long eventId, PatchRequestDto request);

    List<ParticipationRequestDto> getRequestsByUserId(long userId);

    ParticipationRequestDto createRequest(long userId, long eventId);

    ParticipationRequestDto updateRequest(long userId, long eventId);
}