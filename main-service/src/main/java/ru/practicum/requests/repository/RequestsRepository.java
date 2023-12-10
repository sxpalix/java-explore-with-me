package ru.practicum.requests.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.requests.model.ParticipationRequest;
import ru.practicum.requests.model.ParticipationRequestState;

import java.util.List;
import java.util.Optional;

public interface RequestsRepository extends JpaRepository<ParticipationRequest, Long> {
    List<ParticipationRequest> findAllByRequester_Id(long userId);

    List<ParticipationRequest> findAllByEvent_Id(long eventId);

    List<ParticipationRequest> findByEvent_IdAndStatus(long eventId, ParticipationRequestState status);

    List<ParticipationRequest> findAllByEvent_IdAndIdIn(long eventId, List<Long> ids);

    Optional<ParticipationRequest> findByRequester_IdAndId(long userId, long requestId);
}