package br.edu.senac.recursos.web;

import br.edu.senac.recursos.web.dto.MovimentacaoRequest;
import br.edu.senac.recursos.web.dto.MovimentacaoResponse;
import br.edu.senac.recursos.web.dto.SaldoMaterialResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/materiais/{materialId}")
public class MovimentacaoMaterialController {

    private final MovimentacaoMaterialService service;

    public MovimentacaoMaterialController(MovimentacaoMaterialService service) {
        this.service = service;
    }

    @PostMapping("/retiradas")
    public ResponseEntity<MovimentacaoResponse> retirar(@PathVariable Long materialId,
                                                        @Valid @RequestBody MovimentacaoRequest req,
                                                        Authentication auth) {
        var mov = service.retirar(materialId, req.quantidade(), auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(MovimentacaoResponse.de(mov));
    }

    @PostMapping("/devolucoes")
    public ResponseEntity<MovimentacaoResponse> devolver(@PathVariable Long materialId,
                                                         @Valid @RequestBody MovimentacaoRequest req,
                                                         Authentication auth) {
        var mov = service.devolver(materialId, req.quantidade(), auth.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(MovimentacaoResponse.de(mov));
    }

    @GetMapping("/saldo")
    public SaldoMaterialResponse saldo(@PathVariable Long materialId) {
        return service.saldo(materialId);
    }

    @GetMapping("/movimentacoes")
    public List<MovimentacaoResponse> historico(@PathVariable Long materialId) {
        return service.historico(materialId).stream().map(MovimentacaoResponse::de).toList();
    }
}
