# Mutacao manual - prova de que os testes protegem

Data: 2026-10-29.
Metodo: quebrar a regra no codigo de producao, rodar o teste ligado a ela, registrar se ficou vermelho, desfazer com git restore.

| Regra | Quebra aplicada | Teste executado | Resultado | Situacao |
|---|---|---|---|---|
| Sobreposicao de sala | remover constraint de exclusao do InicializadorBanco | SobreposicaoTest | vermelho | teste protege |
| Dupla reserva | remover lock pessimista em SalaRepository.findByIdParaAtualizar | ConcorrenciaTest | vermelho | teste protege |
| Autorizacao por perfil | trocar hasAnyRole de /reservas/*/aprovar por permitAll | AutorizacaoPorPerfilTest e AprovacaoTest | vermelho | teste protege |
| Maquina de estados | trocar TRANSICOES por mapa que aceita qualquer transicao | MaquinaEstadosTest.solicitadaNaoPodeIrDiretoParaConcluida409 | vermelho | teste protege |
| Manutencao | remover checagem de emManutencao em ReservaService.criar | RegrasReservaTest | vermelho | teste protege |
| Auditoria | remover chamada de auditoria no ReservaService.criar | AuditoriaTest | vermelho | teste protege |
| Notificacao | remover chamada do notificador no ReservaService.transicionar | NotificacaoTest | vermelho | teste protege |
| Termino depois do inicio | trocar isAfter por isBefore na validacao | ValidacaoEntradaTest | vermelho | teste protege |

Nenhuma quebra ficou no codigo de producao. Todas desfeitas com git restore antes da proxima.
