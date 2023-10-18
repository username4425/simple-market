package com.example.marketplace.advices;

import com.example.marketplace.appexceptions.AppException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> handleException(AppException e){
        return ResponseEntity.status(e.getStatus()).body(e.getDescription());
    }
}
