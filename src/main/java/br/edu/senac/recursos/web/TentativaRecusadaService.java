package br.edu.senac.recursos.web;

import br.edu.senac.recursos.domain.TentativaRecusada;
import br.edu.senac.recursos.repository.TentativaRecusadaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class TentativaRecusadaService {

    private final TentativaRecusadaRepository repo;

    public TentativaRecusadaService(TentativaRecusadaRepository repo) {
        this.repo = repo;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registrar(Long salaId, Long professorId, Long materialId,
                          OffsetDateTime inicio, OffsetDateTime fim, String motivo) {
        TentativaRecusada t = new TentativaRecusada();
        t.setSalaId(salaId);
        t.setProfessorId(professorId);
        t.setMaterialId(materialId);
        t.setInicio(inicio);
        t.setFim(fim);
        t.setInstante(OffsetDateTime.now());
        t.setMotivo(motivo);
        repo.save(t);
    }
}
