package com.rocketseat.planner.controller.exceptions;

import com.rocketseat.planner.exceptions.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<StandardError> noSuchElementException(NoSuchElementException e) {

        String error = "Resource not found. ";
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardError standardError = new StandardError(LocalDateTime.now(), status.value(), error, e.getMessage());

        return ResponseEntity.status(status).body(standardError);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<StandardError> nullPointerException (NullPointerException e){

        String error = "Campo obrigatório não pode estar vazio.";
        HttpStatus status = HttpStatus.CONFLICT;
        StandardError standardError = new StandardError(LocalDateTime.now(), status.value(), error, e.getMessage());

        return ResponseEntity.status(status).body(standardError);
    }

}
