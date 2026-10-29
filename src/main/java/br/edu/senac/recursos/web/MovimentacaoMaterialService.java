package br.edu.senac.recursos.web;

import br.edu.senac.recursos.domain.Material;
import br.edu.senac.recursos.domain.MovimentacaoMaterial;
import br.edu.senac.recursos.repository.MaterialRepository;
import br.edu.senac.recursos.repository.MovimentacaoMaterialRepository;
import br.edu.senac.recursos.web.dto.SaldoMaterialResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class MovimentacaoMaterialService {

    private final MaterialRepository materiais;
    private final MovimentacaoMaterialRepository movs;

    public MovimentacaoMaterialService(MaterialRepository materiais, MovimentacaoMaterialRepository movs) {
        this.materiais = materiais;
        this.movs = movs;
    }

    @Transactional
    public MovimentacaoMaterial retirar(Long materialId, int quantidade, String email) {
        Material m = buscar(materialId);
        if (Boolean.TRUE.equals(m.getEmManutencao())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Material em manutenção");
        }
        SaldoMaterialResponse saldo = saldoDe(m);
        if (quantidade > saldo.disponivel()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Quantidade indisponível. Disponível: " + saldo.disponivel());
        }
        return registrar(m, "RETIRADA", quantidade, email);
    }

    @Transactional
    public MovimentacaoMaterial devolver(Long materialId, int quantidade, String email) {
        Material m = buscar(materialId);
        SaldoMaterialResponse saldo = saldoDe(m);
        if (quantidade > saldo.emPosse()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Devolução maior que o em posse. Em posse: " + saldo.emPosse());
        }
        return registrar(m, "DEVOLUCAO", quantidade, email);
    }

    public SaldoMaterialResponse saldo(Long materialId) {
        return saldoDe(buscar(materialId));
    }

    public java.util.List<MovimentacaoMaterial> historico(Long materialId) {
        buscar(materialId);
        return movs.findByMaterialIdOrderByInstanteAsc(materialId);
    }

    private MovimentacaoMaterial registrar(Material m, String tipo, int quantidade, String email) {
        MovimentacaoMaterial mov = new MovimentacaoMaterial();
        mov.setMaterial(m);
        mov.setTipo(tipo);
        mov.setQuantidade(quantidade);
        mov.setUsuarioEmail(email);
        return movs.save(mov);
    }

    private SaldoMaterialResponse saldoDe(Material m) {
        long retirado = movs.somarPorTipo(m.getId(), "RETIRADA");
        long devolvido = movs.somarPorTipo(m.getId(), "DEVOLUCAO");
        int emPosse = (int) (retirado - devolvido);
        int disponivel = m.getQuantidade() - emPosse;
        return new SaldoMaterialResponse(m.getId(), m.getQuantidade(), emPosse, disponivel);
    }

    private Material buscar(Long id) {
        return materiais.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Material não encontrado"));
    }
}
