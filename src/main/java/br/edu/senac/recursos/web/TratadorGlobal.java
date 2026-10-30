package br.edu.senac.recursos.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class TratadorGlobal {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> tratar(ResponseStatusException e) {
        String motivo = e.getReason() != null ? e.getReason() : "Erro";
        return ResponseEntity.status(e.getStatusCode()).body(Map.of("erro", motivo));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> tratarValidacao(MethodArgumentNotValidException e) {
        String mensagem = e.getBindingResult().getFieldErrors().stream()
                .map(this::descrever)
                .collect(Collectors.joining("; "));
        if (mensagem.isBlank()) {
            mensagem = "Dados inválidos";
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", mensagem));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> tratarCorpoIlegivel(HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("erro", "Corpo da requisição inválido ou malformado"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> tratarArgumento(IllegalArgumentException e) {
        String motivo = e.getMessage() != null ? e.getMessage() : "Argumento inválido";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", motivo));
    }

    private String descrever(FieldError erro) {
        String campo = erro.getField();
        String padrao = erro.getCode();
        if ("NotBlank".equals(padrao)) return campo + " é obrigatório";
        if ("NotNull".equals(padrao)) return campo + " é obrigatório";
        if ("Email".equals(padrao)) return campo + " deve ser um e-mail válido";
        if ("Min".equals(padrao)) return campo + " deve ser maior ou igual a 1";
        return campo + " inválido";
    }
}
