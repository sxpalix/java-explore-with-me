package ru.practicum.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Exception constraintViolationError(final Exception e) {
        log.debug("Get status 400 {}", e.getMessage(), e);
        return new Exception(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationException constraintViolationError(final ValidationException e) {
        log.debug("Get status 400 {}", e.getMessage(), e);
        return new ValidationException(e.getMessage());
    }
}