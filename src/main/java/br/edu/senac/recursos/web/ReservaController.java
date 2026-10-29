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

import java.time.OffsetDateTime;
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

    @PostMapping("/{id}/aprovar")
    public ResponseEntity<?> aprovar(@PathVariable Long id) {
        return reservas.findById(id)
                .<ResponseEntity<?>>map(r -> {
                    if (r.getStatus() != Reserva.StatusReserva.SOLICITADA) {
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(Map.of("erro", "Só é possível aprovar reservas com status SOLICITADA"));
                    }
                    r.setStatus(Reserva.StatusReserva.APROVADA);
                    reservas.save(r);
                    return ResponseEntity.ok(ReservaResponse.de(r));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("erro", "Reserva não encontrada")));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> alterar(@PathVariable Long id, @Valid @RequestBody ReservaRequest req, Authentication auth) {
        return reservas.findById(id)
                .<ResponseEntity<?>>map(r -> {
                    if (!r.getSolicitante().getEmail().equals(auth.getName())) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body(Map.of("erro", "Só o dono da reserva pode alterar"));
                    }
                    if (r.getInicio().isBefore(OffsetDateTime.now())) {
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(Map.of("erro", "Reserva já iniciada não pode ser alterada"));
                    }
                    if (!req.fim().isAfter(req.inicio())) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(Map.of("erro", "Término deve ser posterior ao início"));
                    }
                    try {
                        Reserva atualizada = service.alterar(id, req);
                        return ResponseEntity.ok(ReservaResponse.de(atualizada));
                    } catch (org.springframework.web.server.ResponseStatusException e) {
                        return ResponseEntity.status(e.getStatusCode())
                                .body(Map.of("erro", e.getReason() == null ? "Erro" : e.getReason()));
                    }
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("erro", "Reserva não encontrada")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelar(@PathVariable Long id, Authentication auth) {
        return reservas.findById(id)
                .<ResponseEntity<?>>map(r -> {
                    if (!r.getSolicitante().getEmail().equals(auth.getName())) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                .body(Map.of("erro", "Só o dono da reserva pode cancelar"));
                    }
                    if (r.getStatus() == Reserva.StatusReserva.CONCLUIDA
                            || r.getStatus() == Reserva.StatusReserva.CANCELADA) {
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(Map.of("erro", "Reserva já finalizada não pode ser cancelada"));
                    }
                    if (r.getInicio().isBefore(OffsetDateTime.now())) {
                        return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(Map.of("erro", "Reserva já iniciada não pode ser cancelada"));
                    }
                    r.setStatus(Reserva.StatusReserva.CANCELADA);
                    reservas.save(r);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("erro", "Reserva não encontrada")));
    }
}
