package ru.practicum.compilations.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.compilations.model.Compilations;

public interface CompilationsRepository extends JpaRepository<Compilations, Long> {
    Page<Compilations> findAllByPinned(boolean pinned, Pageable pageable);
}

