package br.edu.senac.recursos.web;

import br.edu.senac.recursos.domain.Reserva;
import br.edu.senac.recursos.repository.ProfessorRepository;
import br.edu.senac.recursos.repository.ReservaRepository;
import br.edu.senac.recursos.repository.SalaRepository;
import br.edu.senac.recursos.repository.TentativaRecusadaRepository;
import br.edu.senac.recursos.web.dto.RelatorioUtilizacaoResponse;
import br.edu.senac.recursos.web.dto.RelatorioUtilizacaoResponse.ItemUso;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/relatorios")
public class RelatorioController {

    private final ReservaRepository reservas;
    private final SalaRepository salas;
    private final ProfessorRepository professores;
    private final TentativaRecusadaRepository tentativas;

    public RelatorioController(ReservaRepository reservas, SalaRepository salas,
                               ProfessorRepository professores, TentativaRecusadaRepository tentativas) {
        this.reservas = reservas;
        this.salas = salas;
        this.professores = professores;
        this.tentativas = tentativas;
    }

    @GetMapping("/utilizacao")
    public ResponseEntity<RelatorioUtilizacaoResponse> utilizacao(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime fim) {

        List<Reserva> noPeriodo = reservas.findAll().stream()
                .filter(r -> r.getStatus() != Reserva.StatusReserva.CANCELADA
                        && r.getStatus() != Reserva.StatusReserva.REJEITADA)
                .filter(r -> r.getInicio().isBefore(fim) && r.getFim().isAfter(inicio))
                .toList();

        Map<Long, long[]> porSala = new HashMap<>();
        Map<Long, long[]> porProf = new HashMap<>();
        for (Reserva r : noPeriodo) {
            long minutos = Math.min(r.getFim().toEpochSecond(), fim.toEpochSecond())
                    - Math.max(r.getInicio().toEpochSecond(), inicio.toEpochSecond());
            minutos = Math.max(0, minutos);
            porSala.computeIfAbsent(r.getSala().getId(), k -> new long[2]);
            porSala.get(r.getSala().getId())[0] += minutos;
            porSala.get(r.getSala().getId())[1] += 1;
            if (r.getProfessor() != null) {
                porProf.computeIfAbsent(r.getProfessor().getId(), k -> new long[2]);
                porProf.get(r.getProfessor().getId())[0] += minutos;
                porProf.get(r.getProfessor().getId())[1] += 1;
            }
        }

        List<ItemUso> listaSala = porSala.entrySet().stream().map(e -> {
            var s = salas.findById(e.getKey()).orElse(null);
            String nome = s != null ? s.getNome() : ("Sala " + e.getKey());
            return new ItemUso(e.getKey(), nome, e.getValue()[0] / 3600, e.getValue()[1]);
        }).collect(Collectors.toList());

        List<ItemUso> listaProf = porProf.entrySet().stream().map(e -> {
            var p = professores.findById(e.getKey()).orElse(null);
            String nome = p != null ? p.getNome() : ("Professor " + e.getKey());
            return new ItemUso(e.getKey(), nome, e.getValue()[0] / 3600, e.getValue()[1]);
        }).collect(Collectors.toList());

        long conflitos = tentativas.findAll().stream()
                .filter(t -> t.getInicio().isBefore(fim) && t.getFim().isAfter(inicio))
                .count();
        return ResponseEntity.ok(new RelatorioUtilizacaoResponse(
                inicio.toString(), fim.toString(), listaSala, listaProf, conflitos));
    }
}
