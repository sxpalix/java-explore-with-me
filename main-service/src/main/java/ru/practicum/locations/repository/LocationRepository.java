package ru.practicum.locations.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.locations.model.Location;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByLatAndLonAndRadius(float lat, float lon, float radius);

    List<Location> findByNameIsNotNull(Pageable page);
}
