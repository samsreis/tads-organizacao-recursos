# Mutacao manual - prova de que os testes protegem

Data: 2026-10-29. Reexecucao: 2026-11-09.
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

## Provas reexecutadas em 2026-11-09 no commit 953a3a3

| Regra | Quebra aplicada | Teste executado | Resultado | Restauracao |
|---|---|---|---|---|
| Posse do cancelamento | ReservaController linha 173, if (!r.getSolicitante().getEmail().equals(auth.getName())) trocado por if (false) | PosseReservaTest | 2 testes, 1 falha em outroSolicitanteNaoPodeCancelarReservaAlheia403, BUILD FAILURE | git restore, arvore limpa, linha restaurada |
| Aprovacao de recurso restrito | SecurityConfig linha 62, hasAnyRole(RESPONSAVEL, ADMINISTRADOR) trocado por permitAll em /reservas/*/aprovar | AprovacaoTest#solicitanteNaoPodeAprovar403 | 1 teste, 1 falha, BUILD FAILURE | git restore, arvore limpa, linha restaurada |
