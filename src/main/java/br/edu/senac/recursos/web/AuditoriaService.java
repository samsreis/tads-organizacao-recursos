package br.edu.senac.recursos.web;

import br.edu.senac.recursos.domain.AuditoriaReserva;
import br.edu.senac.recursos.repository.AuditoriaReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
public class AuditoriaService {

    private final AuditoriaReservaRepository auditorias;

    public AuditoriaService(AuditoriaReservaRepository auditorias) {
        this.auditorias = auditorias;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void registrar(Long reservaId, String usuarioEmail, String estadoAnterior,
                          String estadoNovo, String motivo) {
        AuditoriaReserva a = new AuditoriaReserva();
        a.setReservaId(reservaId);
        a.setUsuarioEmail(usuarioEmail);
        a.setInstante(OffsetDateTime.now());
        a.setEstadoAnterior(estadoAnterior);
        a.setEstadoNovo(estadoNovo);
        a.setMotivo(motivo);
        auditorias.save(a);
    }
}
