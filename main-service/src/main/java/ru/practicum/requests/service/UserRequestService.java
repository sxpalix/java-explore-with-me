package ru.practicum.requests.service;

import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.dto.PatchRequestDto;
import ru.practicum.requests.dto.PatchResponseDto;

import java.util.List;

public interface UserRequestService {
    List<ParticipationRequestDto> getParticipateRequestsByEventCurrentUser(long userId, long eventId);

    PatchResponseDto patchParticipateRequestsByEventCurrentUser(long userId, long eventId, PatchRequestDto request);
}