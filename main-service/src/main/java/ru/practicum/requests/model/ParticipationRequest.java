package ru.practicum.requests.model;

import lombok.*;
import ru.practicum.events.model.Event;
import ru.practicum.users.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "participation_requests", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User requester;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ParticipationRequestState status;
    @Column(name = "created_date")
    private LocalDateTime created;
}
