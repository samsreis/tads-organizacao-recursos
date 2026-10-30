package br.edu.senac.recursos.web;

import br.edu.senac.recursos.domain.Reserva;
import br.edu.senac.recursos.domain.Sala;
import br.edu.senac.recursos.repository.ReservaRepository;
import br.edu.senac.recursos.repository.SalaRepository;
import br.edu.senac.recursos.web.dto.SalaRequest;
import br.edu.senac.recursos.web.dto.SalaResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/salas")
public class SalaController {

    private final SalaRepository salas;
    private final ReservaRepository reservas;

    public SalaController(SalaRepository salas, ReservaRepository reservas) {
        this.salas = salas;
        this.reservas = reservas;
    }

    @GetMapping
    public List<SalaResponse> listar(
            @RequestParam(required = false) Integer capacidadeMinima,
            @RequestParam(required = false) String localizacao,
            @RequestParam(required = false) OffsetDateTime inicio,
            @RequestParam(required = false) OffsetDateTime fim) {

        List<Sala> lista = salas.findAll();

        if (capacidadeMinima != null) {
            lista = lista.stream().filter(s -> s.getCapacidade() >= capacidadeMinima).toList();
        }
        if (localizacao != null && !localizacao.isBlank()) {
            String alvo = localizacao.toLowerCase();
            lista = lista.stream().filter(s -> s.getLocalizacao().toLowerCase().contains(alvo)).toList();
        }
        if ((inicio == null) != (fim == null)) {
            throw new IllegalArgumentException("Informe inicio e fim juntos");
        }
        if (inicio != null) {
            if (!fim.isAfter(inicio)) {
                throw new IllegalArgumentException("Fim deve ser posterior ao inicio");
            }
            List<Reserva.StatusReserva> ativos = List.of(
                    Reserva.StatusReserva.SOLICITADA,
                    Reserva.StatusReserva.APROVADA,
                    Reserva.StatusReserva.EM_USO);
            List<Long> ocupadas = reservas.salasOcupadasNoIntervalo(inicio, fim, ativos);
            lista = lista.stream().filter(s -> !ocupadas.contains(s.getId())).toList();
        }

        return lista.stream().map(SalaResponse::de).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        return salas.findById(id)
                .<ResponseEntity<?>>map(s -> ResponseEntity.ok(SalaResponse.de(s)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", "Sala não encontrada")));
    }

    @PostMapping
    public ResponseEntity<SalaResponse> criar(@Valid @RequestBody SalaRequest req) {
        Sala s = new Sala();
        s.setNome(req.nome());
        s.setCapacidade(req.capacidade());
        s.setLocalizacao(req.localizacao());
        s.setEmManutencao(req.emManutencao() != null && req.emManutencao());
        salas.save(s);
        return ResponseEntity.status(HttpStatus.CREATED).body(SalaResponse.de(s));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody SalaRequest req) {
        return salas.findById(id)
                .<ResponseEntity<?>>map(s -> {
                    s.setNome(req.nome());
                    s.setCapacidade(req.capacidade());
                    s.setLocalizacao(req.localizacao());
                    s.setEmManutencao(req.emManutencao() != null && req.emManutencao());
                    salas.save(s);
                    return ResponseEntity.ok(SalaResponse.de(s));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", "Sala não encontrada")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> apagar(@PathVariable Long id) {
        if (!salas.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", "Sala não encontrada"));
        }
        salas.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
