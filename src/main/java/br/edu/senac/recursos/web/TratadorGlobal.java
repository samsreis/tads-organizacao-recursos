package br.edu.senac.recursos.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestControllerAdvice
public class TratadorGlobal {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> tratar(ResponseStatusException e) {
        String motivo = e.getReason() != null ? e.getReason() : "Erro";
        return ResponseEntity.status(e.getStatusCode()).body(Map.of("erro", motivo));
    }
}
