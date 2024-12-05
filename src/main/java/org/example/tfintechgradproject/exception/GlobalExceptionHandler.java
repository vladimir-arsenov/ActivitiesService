package org.example.tfintechgradproject.exception;

import org.example.tfintechgradproject.exception.exceptions.CannotJoinActivityRequest;
import org.example.tfintechgradproject.exception.exceptions.ExternalServiceUnavailable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ ExternalServiceUnavailable.class })
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<String> handleExternalServiceUnavailable(ExternalServiceUnavailable e) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .header("Retry-After", "60s")
                .body(e.getMessage());
    }

    @ExceptionHandler({ CannotJoinActivityRequest.class })
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleCannotJoinActivityRequest(CannotJoinActivityRequest e) {
        return e.getMessage();
    }
}
