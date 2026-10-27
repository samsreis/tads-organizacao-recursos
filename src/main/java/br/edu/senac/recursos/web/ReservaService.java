package br.edu.senac.recursos.web;

import br.edu.senac.recursos.domain.Reserva;
import br.edu.senac.recursos.domain.Sala;
import br.edu.senac.recursos.domain.Usuario;
import br.edu.senac.recursos.repository.ReservaRepository;
import br.edu.senac.recursos.repository.SalaRepository;
import br.edu.senac.recursos.repository.UsuarioRepository;
import br.edu.senac.recursos.web.dto.ReservaRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ReservaService {

    private final ReservaRepository reservas;
    private final SalaRepository salas;
    private final UsuarioRepository usuarios;

    public ReservaService(ReservaRepository reservas, SalaRepository salas, UsuarioRepository usuarios) {
        this.reservas = reservas;
        this.salas = salas;
        this.usuarios = usuarios;
    }

    public Reserva criar(String emailSolicitante, ReservaRequest req) {
        if (!req.fim().isAfter(req.inicio())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Término deve ser posterior ao início");
        }
        Sala sala = salas.findById(req.salaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sala não encontrada"));
        if (Boolean.TRUE.equals(sala.getEmManutencao())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Sala em manutenção");
        }
        Usuario solicitante = usuarios.findByEmail(emailSolicitante)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Solicitante inválido"));

        Reserva r = new Reserva();
        r.setSala(sala);
        r.setSolicitante(solicitante);
        r.setInicio(req.inicio());
        r.setFim(req.fim());
        r.setStatus(Reserva.StatusReserva.SOLICITADA);

        try {
            return reservas.saveAndFlush(r);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Horário indisponível para esta sala");
        }
    }
}
