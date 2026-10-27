package br.edu.senac.recursos.web;

import br.edu.senac.recursos.domain.Sala;
import br.edu.senac.recursos.repository.SalaRepository;
import br.edu.senac.recursos.web.dto.SalaRequest;
import br.edu.senac.recursos.web.dto.SalaResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/salas")
public class SalaController {

    private final SalaRepository salas;

    public SalaController(SalaRepository salas) {
        this.salas = salas;
    }

    @GetMapping
    public List<SalaResponse> listar() {
        return salas.findAll().stream().map(SalaResponse::de).toList();
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
