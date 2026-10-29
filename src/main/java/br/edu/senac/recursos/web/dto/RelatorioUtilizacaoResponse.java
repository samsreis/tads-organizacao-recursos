package br.edu.senac.recursos.web.dto;

import java.util.List;

public record RelatorioUtilizacaoResponse(
        String periodoInicio,
        String periodoFim,
        List<ItemUso> porSala,
        List<ItemUso> porProfessor,
        long conflitosEvitados
) {
    public record ItemUso(Long recursoId, String nome, long horasReservadas, long totalReservas) {}
}
