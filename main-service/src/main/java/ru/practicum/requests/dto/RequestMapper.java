package ru.practicum.requests.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.requests.model.ParticipationRequest;

@UtilityClass
public class RequestMapper {
    public ParticipationRequestDto toDto(ParticipationRequest request) {
        return ParticipationRequestDto.builder()
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .id(request.getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }
}
