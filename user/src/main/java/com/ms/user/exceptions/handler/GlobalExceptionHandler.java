package com.ms.user.exceptions.handler;

import com.ms.user.exceptions.CPFRegisteredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CPFRegisteredException.class)
    public ResponseEntity<String> handleCPFRegisteredException(CPFRegisteredException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // 409
                .body(ex.getMessage());      // só a mensagem da exceção
    }
}
