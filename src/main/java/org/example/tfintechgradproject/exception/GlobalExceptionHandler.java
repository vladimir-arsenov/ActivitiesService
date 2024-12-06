package org.example.tfintechgradproject.exception;

import jakarta.persistence.EntityNotFoundException;
import org.example.tfintechgradproject.exception.exceptions.CannotJoinActivityRequestException;
import org.example.tfintechgradproject.exception.exceptions.ExternalServiceUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ ExternalServiceUnavailableException.class })
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<String> handleExternalServiceUnavailable(ExternalServiceUnavailableException e) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .header("Retry-After", "60s")
                .body(e.getMessage());
    }

    @ExceptionHandler({ CannotJoinActivityRequestException.class })
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleCannotJoinActivityRequest(CannotJoinActivityRequestException e) {
        return e.getMessage();
    }

    @ExceptionHandler({ EntityNotFoundException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleEntityNotFoundException(EntityNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler({ IllegalStateException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalStateException(IllegalStateException e) {
        return e.getMessage();
    }
}
