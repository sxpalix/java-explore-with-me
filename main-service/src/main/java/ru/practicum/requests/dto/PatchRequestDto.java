package ru.practicum.requests.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.requests.model.ParticipationRequestState;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PatchRequestDto {
    List<Long> requestIds;
    ParticipationRequestState status;
}
