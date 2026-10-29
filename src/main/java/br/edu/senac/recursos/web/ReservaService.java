package br.edu.senac.recursos.web;

import br.edu.senac.recursos.domain.Material;
import br.edu.senac.recursos.domain.Professor;
import br.edu.senac.recursos.domain.Reserva;
import br.edu.senac.recursos.domain.Reserva.StatusReserva;
import br.edu.senac.recursos.domain.Sala;
import br.edu.senac.recursos.domain.Usuario;
import br.edu.senac.recursos.repository.MaterialRepository;
import br.edu.senac.recursos.repository.ProfessorRepository;
import br.edu.senac.recursos.repository.ReservaRepository;
import br.edu.senac.recursos.repository.SalaRepository;
import br.edu.senac.recursos.repository.UsuarioRepository;
import br.edu.senac.recursos.web.dto.ReservaRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

@Service
public class ReservaService {

    public static final Map<StatusReserva, Set<StatusReserva>> TRANSICOES = Map.of(
        StatusReserva.SOLICITADA, EnumSet.of(StatusReserva.APROVADA, StatusReserva.REJEITADA, StatusReserva.CANCELADA),
        StatusReserva.APROVADA, EnumSet.of(StatusReserva.EM_USO, StatusReserva.CANCELADA, StatusReserva.NAO_COMPARECEU),
        StatusReserva.EM_USO, EnumSet.of(StatusReserva.CONCLUIDA),
        StatusReserva.CONCLUIDA, EnumSet.noneOf(StatusReserva.class),
        StatusReserva.REJEITADA, EnumSet.noneOf(StatusReserva.class),
        StatusReserva.CANCELADA, EnumSet.noneOf(StatusReserva.class),
        StatusReserva.NAO_COMPARECEU, EnumSet.noneOf(StatusReserva.class)
    );

    private final ReservaRepository reservas;
    private final SalaRepository salas;
    private final ProfessorRepository professores;
    private final MaterialRepository materiais;
    private final UsuarioRepository usuarios;
    private final AuditoriaService auditoria;

    public ReservaService(ReservaRepository reservas, SalaRepository salas,
                          ProfessorRepository professores, MaterialRepository materiais,
                          UsuarioRepository usuarios, AuditoriaService auditoria) {
        this.reservas = reservas;
        this.salas = salas;
        this.professores = professores;
        this.materiais = materiais;
        this.usuarios = usuarios;
        this.auditoria = auditoria;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Reserva criar(String emailSolicitante, ReservaRequest req) {
        if (!req.fim().isAfter(req.inicio())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Término deve ser posterior ao início");
        }
        Sala sala = salas.findByIdParaAtualizar(req.salaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sala não encontrada"));
        if (Boolean.TRUE.equals(sala.getEmManutencao())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Sala em manutenção");
        }

        Professor professor = null;
        if (req.professorId() != null) {
            professor = professores.findById(req.professorId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor não encontrado"));
            if (!Boolean.TRUE.equals(professor.getAtivo())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Professor inativo");
            }
        }

        Material material = null;
        if (req.materialId() != null) {
            material = materiais.findById(req.materialId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Material não encontrado"));
            if (Boolean.TRUE.equals(material.getEmManutencao())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Material em manutenção");
            }
        }

        Usuario solicitante = usuarios.findByEmail(emailSolicitante)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Solicitante inválido"));

        Reserva r = new Reserva();
        r.setSala(sala);
        r.setProfessor(professor);
        r.setMaterial(material);
        r.setSolicitante(solicitante);
        r.setInicio(req.inicio());
        r.setFim(req.fim());
        r.setStatus(StatusReserva.SOLICITADA);

        try {
            Reserva salva = reservas.saveAndFlush(r);
            auditoria.registrar(salva.getId(), emailSolicitante, null,
                    salva.getStatus().name(), "Reserva criada");
            return salva;
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Horário indisponível para o recurso selecionado");
        } catch (org.springframework.dao.CannotAcquireLockException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Horário indisponível para o recurso selecionado");
        } catch (org.springframework.dao.PessimisticLockingFailureException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Horário indisponível para o recurso selecionado");
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Reserva transicionar(Long id, String emailUsuario, StatusReserva novoEstado, String motivo) {
        Reserva r = reservas.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva não encontrada"));
        StatusReserva atual = r.getStatus() == null ? StatusReserva.SOLICITADA : r.getStatus();
        Set<StatusReserva> permitidos = TRANSICOES.getOrDefault(atual, EnumSet.noneOf(StatusReserva.class));
        if (!permitidos.contains(novoEstado)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Transição inválida de " + atual + " para " + novoEstado);
        }
        r.setStatus(novoEstado);
        reservas.saveAndFlush(r);
        auditoria.registrar(r.getId(), emailUsuario, atual.name(), novoEstado.name(), motivo);
        return r;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Reserva alterar(Long id, ReservaRequest req) {
        Reserva r = reservas.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva não encontrada"));
        Sala sala = salas.findByIdParaAtualizar(req.salaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sala não encontrada"));
        if (Boolean.TRUE.equals(sala.getEmManutencao())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Sala em manutenção");
        }
        Professor professor = null;
        if (req.professorId() != null) {
            professor = professores.findById(req.professorId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Professor não encontrado"));
            if (!Boolean.TRUE.equals(professor.getAtivo())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Professor inativo");
            }
        }
        Material material = null;
        if (req.materialId() != null) {
            material = materiais.findById(req.materialId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Material não encontrado"));
            if (Boolean.TRUE.equals(material.getEmManutencao())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Material em manutenção");
            }
        }
        r.setSala(sala);
        r.setProfessor(professor);
        r.setMaterial(material);
        r.setInicio(req.inicio());
        r.setFim(req.fim());
        try {
            return reservas.saveAndFlush(r);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Horário indisponível para o recurso selecionado");
        }
    }
}
