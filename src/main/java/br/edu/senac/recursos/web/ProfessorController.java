package br.edu.senac.recursos.web;

import br.edu.senac.recursos.domain.Professor;
import br.edu.senac.recursos.repository.ProfessorRepository;
import br.edu.senac.recursos.web.dto.ProfessorRequest;
import br.edu.senac.recursos.web.dto.ProfessorResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/professores")
public class ProfessorController {

    private final ProfessorRepository professores;

    public ProfessorController(ProfessorRepository professores) {
        this.professores = professores;
    }

    @GetMapping
    public List<ProfessorResponse> listar(@RequestParam(required = false) String competencia) {
        List<Professor> base = (competencia == null || competencia.isBlank())
                ? professores.findAll()
                : professores.findByCompetenciaContainingIgnoreCase(competencia);
        return base.stream().map(ProfessorResponse::de).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        return professores.findById(id)
                .<ResponseEntity<?>>map(p -> ResponseEntity.ok(ProfessorResponse.de(p)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", "Professor não encontrado")));
    }

    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody ProfessorRequest req) {
        if (professores.findByEmail(req.email()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("erro", "E-mail já cadastrado para outro professor"));
        }
        Professor p = new Professor();
        p.setNome(req.nome());
        p.setEmail(req.email());
        p.setCompetencia(req.competencia());
        p.setAtivo(req.ativo() == null || req.ativo());
        professores.save(p);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProfessorResponse.de(p));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody ProfessorRequest req) {
        return professores.findById(id)
                .<ResponseEntity<?>>map(p -> {
                    p.setNome(req.nome());
                    p.setEmail(req.email());
                    p.setCompetencia(req.competencia());
                    p.setAtivo(req.ativo() == null || req.ativo());
                    professores.save(p);
                    return ResponseEntity.ok(ProfessorResponse.de(p));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", "Professor não encontrado")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> apagar(@PathVariable Long id) {
        if (!professores.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", "Professor não encontrado"));
        }
        professores.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
