package br.edu.senac.recursos.web;

import br.edu.senac.recursos.domain.Material;
import br.edu.senac.recursos.repository.MaterialRepository;
import br.edu.senac.recursos.web.dto.MaterialRequest;
import br.edu.senac.recursos.web.dto.MaterialResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/materiais")
public class MaterialController {

    private final MaterialRepository materiais;

    public MaterialController(MaterialRepository materiais) {
        this.materiais = materiais;
    }

    @GetMapping
    public List<MaterialResponse> listar(@RequestParam(required = false) String tipo) {
        List<Material> base = (tipo == null || tipo.isBlank())
                ? materiais.findAll()
                : materiais.findByTipoContainingIgnoreCase(tipo);
        return base.stream().map(MaterialResponse::de).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        return materiais.findById(id)
                .<ResponseEntity<?>>map(m -> ResponseEntity.ok(MaterialResponse.de(m)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", "Material não encontrado")));
    }

    @PostMapping
    public ResponseEntity<?> criar(@Valid @RequestBody MaterialRequest req) {
        Material m = new Material();
        m.setNome(req.nome());
        m.setTipo(req.tipo());
        m.setQuantidade(req.quantidade());
        m.setEmManutencao(req.emManutencao() != null && req.emManutencao());
        materiais.save(m);
        return ResponseEntity.status(HttpStatus.CREATED).body(MaterialResponse.de(m));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @Valid @RequestBody MaterialRequest req) {
        return materiais.findById(id)
                .<ResponseEntity<?>>map(m -> {
                    m.setNome(req.nome());
                    m.setTipo(req.tipo());
                    m.setQuantidade(req.quantidade());
                    m.setEmManutencao(req.emManutencao() != null && req.emManutencao());
                    materiais.save(m);
                    return ResponseEntity.ok(MaterialResponse.de(m));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", "Material não encontrado")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> apagar(@PathVariable Long id) {
        if (!materiais.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("erro", "Material não encontrado"));
        }
        materiais.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
