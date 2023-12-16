package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.utils.Constants;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class MainExceptionController {
    public MainExceptionController() {
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, String>> notFoundError(final NotFoundException e) {
        log.error("NotFoundException");
        return new ResponseEntity<>(Map.of("status", "NOT_FOUND",
                "reason", "The required object was not found.",
                "message", e.getMessage(),
                "timestamp", LocalDateTime.now().format(Constants.DATE_TIME_FORMAT_PATTERN)),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> constraintViolationError(final ConstraintViolationException e) {
        log.error("ConstraintViolationException");
        return new ResponseEntity<>(Map.of("status", "CONFLICT",
                "reason", "Integrity constraint has been violated.",
                "message", e.getMessage(), "timestamp",
                LocalDateTime.now().format(Constants.DATE_TIME_FORMAT_PATTERN)),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<Map<String, String>> numberFormatError(final NumberFormatException e) {
        log.error("NumberFormatException");
        return new ResponseEntity<>(Map.of("status", "BAD_REQUEST",
                "reason", "Incorrectly made request.",
                "message", e.getMessage(),
                "timestamp", LocalDateTime.now().format(Constants.DATE_TIME_FORMAT_PATTERN)),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity<Map<String, String>> dateError(final DateTimeException e) {
        log.error("DateTimeException");
        return new ResponseEntity<>(Map.of("status", "BAD_REQUEST",
                "reason", "For the requested operation the conditions are not met.",
                "message", e.getMessage(),
                "timestamp", LocalDateTime.now().format(Constants.DATE_TIME_FORMAT_PATTERN)),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RequestException.class)
    public ResponseEntity<Map<String, String>> requestError(final RequestException e) {
        log.error("RequestException but where");
        return new ResponseEntity<>(Map.of("status", "CONFLICT",
                "reason", "Integrity constraint has been violated.",
                "message", e.getMessage(), "timestamp",
                LocalDateTime.now().format(Constants.DATE_TIME_FORMAT_PATTERN)), HttpStatus.CONFLICT);
    }
}