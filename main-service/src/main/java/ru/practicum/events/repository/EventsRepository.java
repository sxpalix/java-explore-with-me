package ru.practicum.events.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.State;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventsRepository extends JpaRepository<Event, Long> {
    Page<Event> findAllByInitiator_Id(long userId, Pageable pageable);

    Optional<Event> findEventByIdAndInitiator_Id(long eventId, long initiatorId);

    List<Event> findAllByCategory_Id(long catId);

    @Query("SELECT e FROM Event e " +
            "WHERE ((:users) IS NULL OR e.initiator.id IN :users) " +
            "AND ((:states) IS NULL OR e.state IN :states) " +
            "AND ((:categories) IS NULL OR e.category.id IN :categories) " +
            "AND (e.eventDate BETWEEN :rangeStart AND :rangeEnd)")
    Page<Event> findAllByParameters(@Param("users") List<Long> users,
                                    @Param("states") List<State> states,
                                    @Param("categories") List<Long> categories,
                                    @Param("rangeStart") LocalDateTime rangeStart,
                                    @Param("rangeEnd") LocalDateTime rangeEnd,
                                    Pageable pageable);

    @Query("select e from Event e " +
            "where (e.state = 'PUBLISHED') " +
            "and (lower(e.annotation) " +
            "like lower(concat('%', :text, '%')) " +
            "or lower(e.description) like lower(concat('%', :text, '%'))) " +
            "and ((:paid) is null or e.paid = :paid) and ((:categories) is null " +
            "or e.category.id in :categories) " +
            "and (e.eventDate between :rangeStart " +
            "and :rangeEnd) ")
    Page<Event> getEventsByParametersSortByViews(@Param("text") String text,
                                                 @Param("categories") List<Long> categories,
                                                 @Param("paid") Boolean paid,
                                                 @Param("rangeStart") LocalDateTime rangeStart,
                                                 @Param("rangeEnd") LocalDateTime rangeEnd,
                                                 Pageable pageable);

    @Query("select e from Event e " +
            "where (e.state = 'PUBLISHED') " +
            "and (lower(e.annotation) like lower(concat('%', :text, '%')) " +
            "or lower(e.description) like lower(concat('%', :text, '%'))) " +
            "and ((:paid) is null or e.paid = :paid) and ((:categories) is null " +
            "or e.category.id in :categories) " +
            "and (e.eventDate between :rangeStart " +
            "and :rangeEnd) group by e.id order by e.eventDate desc")
    Page<Event> getEventsByParametersSortByDate(@Param("text") String text,
                                                @Param("categories") List<Long> categories,
                                                @Param("paid") Boolean paid,
                                                @Param("rangeStart") LocalDateTime rangeStart,
                                                @Param("rangeEnd") LocalDateTime rangeEnd,
                                                Pageable pageable);
}