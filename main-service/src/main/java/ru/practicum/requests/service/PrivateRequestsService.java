package ru.practicum.requests.service;

import ru.practicum.requests.dto.ParticipationRequestDto;

import java.util.List;

public interface PrivateRequestsService {
    List<ParticipationRequestDto> getRequestsByUserId(long userId);

    ParticipationRequestDto postRequest(long userId, long eventId);

    ParticipationRequestDto updateRequest(long userId, long eventId);
}
