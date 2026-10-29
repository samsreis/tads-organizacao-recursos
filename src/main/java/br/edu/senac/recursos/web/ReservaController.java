package br.edu.senac.recursos.web;

import br.edu.senac.recursos.domain.Reserva;
import br.edu.senac.recursos.repository.AuditoriaReservaRepository;
import br.edu.senac.recursos.repository.ReservaRepository;
import br.edu.senac.recursos.web.dto.AuditoriaResponse;
import br.edu.senac.recursos.web.dto.ReservaRequest;
import br.edu.senac.recursos.web.dto.ReservaResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reservas")
public class ReservaController {

    private final ReservaService service;
    private final ReservaRepository reservas;
    private final AuditoriaReservaRepository auditorias;
    private final TentativaRecusadaService tentativas;

    public ReservaController(ReservaService service, ReservaRepository reservas,
                             AuditoriaReservaRepository auditorias,
                             TentativaRecusadaService tentativas) {
        this.service = service;
        this.reservas = reservas;
        this.auditorias = auditorias;
        this.tentativas = tentativas;
    }

    @PostMapping
    public ResponseEntity<ReservaResponse> criar(@Valid @RequestBody ReservaRequest req, Authentication auth) {
        try {
            Reserva r = service.criar(auth.getName(), req);
            return ResponseEntity.status(HttpStatus.CREATED).body(ReservaResponse.de(r));
        } catch (ResponseStatusException e) {
            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                tentativas.registrar(req.salaId(), req.professorId(), req.materialId(),
                        req.inicio(), req.fim(), "CONFLITO_HORARIO");
            }
            throw e;
        }
    }

    @GetMapping("/minhas")
    public List<ReservaResponse> minhas(Authentication auth) {
        return reservas.findAll().stream()
                .filter(r -> r.getSolicitante().getEmail().equals(auth.getName()))
                .map(ReservaResponse::de)
                .toList();
    }

    @GetMapping("/{id}/historico")
    public ResponseEntity<?> historico(@PathVariable Long id) {
        if (!reservas.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", "Reserva não encontrada"));
        }
        List<AuditoriaResponse> historico = auditorias.findByReservaIdOrderByInstanteAsc(id).stream()
                .map(AuditoriaResponse::de)
                .toList();
        return ResponseEntity.ok(historico);
    }

    @PostMapping("/{id}/aprovar")
    public ResponseEntity<?> aprovar(@PathVariable Long id, Authentication auth) {
        if (!reservas.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", "Reserva não encontrada"));
        }
        Reserva atual = reservas.findById(id).orElseThrow();
        if (atual.getStatus() != Reserva.StatusReserva.SOLICITADA) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("erro", "Só é possível aprovar reservas com status SOLICITADA"));
        }
        try {
            Reserva r = service.transicionar(id, auth.getName(),
                    Reserva.StatusReserva.APROVADA, "Aprovada por " + auth.getName());
            return ResponseEntity.ok(ReservaResponse.de(r));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map.of("erro", e.getReason() == null ? "Erro" : e.getReason()));
        }
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
                    } catch (ResponseStatusException e) {
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
                    service.transicionar(id, auth.getName(),
                            Reserva.StatusReserva.CANCELADA, "Cancelada por " + auth.getName());
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("erro", "Reserva não encontrada")));
    }
}
