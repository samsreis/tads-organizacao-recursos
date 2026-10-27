package br.edu.senac.recursos.web;

import br.edu.senac.recursos.domain.Reserva;
import br.edu.senac.recursos.repository.ReservaRepository;
import br.edu.senac.recursos.web.dto.ReservaRequest;
import br.edu.senac.recursos.web.dto.ReservaResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService service;
    private final ReservaRepository reservas;

    public ReservaController(ReservaService service, ReservaRepository reservas) {
        this.service = service;
        this.reservas = reservas;
    }

    @PostMapping
    public ResponseEntity<ReservaResponse> criar(@Valid @RequestBody ReservaRequest req, Authentication auth) {
        Reserva r = service.criar(auth.getName(), req);
        return ResponseEntity.status(HttpStatus.CREATED).body(ReservaResponse.de(r));
    }

    @GetMapping("/minhas")
    public List<ReservaResponse> minhas(Authentication auth) {
        return reservas.findAll().stream()
                .filter(r -> r.getSolicitante().getEmail().equals(auth.getName()))
                .map(ReservaResponse::de)
                .toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelar(@PathVariable Long id, Authentication auth) {
        return reservas.findById(id)
                .<ResponseEntity<?>>map(r -> {
                    if (!r.getSolicitante().getEmail().equals(auth.getName())) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body(Map.of("erro", "Só o dono da reserva pode cancelar"));
                    }
                    r.setStatus(Reserva.StatusReserva.CANCELADA);
                    reservas.save(r);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", "Reserva não encontrada")));
    }
}
