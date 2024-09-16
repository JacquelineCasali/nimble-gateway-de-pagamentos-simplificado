package com.pickpaysimplificado.infra.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
@ExceptionHandler(UserFoundException.class)
private ResponseEntity<String> eventNotFoundHandler(UserFoundException exception){
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event not Found");
}
}
