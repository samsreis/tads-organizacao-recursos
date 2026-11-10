# Mutacao manual - prova de que os testes protegem

Data: 2026-10-29. Reexecucoes: 2026-11-09 e 2026-11-10.
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
| Cancelamento em uso | Em `ReservaService.TRANSICOES`, permitir `CANCELADA` a partir de `EM_USO` | `./mvnw -B -Dtest=AprovacaoTest#cancelarReservaEmUsoRetorna409 test` | vermelho: 1 teste, 1 falha; esperava HTTP 409 e recebeu 204 | regra restaurada; mutacao detectada |

Nenhuma quebra ficou no codigo de producao. Todas desfeitas com git restore antes da proxima.

## Provas reexecutadas em 2026-11-09 no commit final da main

| Regra | Quebra aplicada | Teste executado | Resultado | Restauracao |
|---|---|---|---|---|
| Posse do cancelamento | ReservaController linha 173, if (!r.getSolicitante().getEmail().equals(auth.getName())) trocado por if (false) | PosseReservaTest | 2 testes, 1 falha em outroSolicitanteNaoPodeCancelarReservaAlheia403, BUILD FAILURE | git restore, arvore limpa, linha restaurada |
| Aprovacao de recurso restrito | SecurityConfig linha 62, hasAnyRole(RESPONSAVEL, ADMINISTRADOR) trocado por permitAll em /reservas/*/aprovar | AprovacaoTest#solicitanteNaoPodeAprovar403 | 1 teste, 1 falha, BUILD FAILURE | git restore, arvore limpa, linha restaurada |

## Mutacao de transicao em 2026-11-10

| Regra | Quebra aplicada | Teste executado | Resultado | Restauracao |
|---|---|---|---|---|
| Reserva em uso nao pode ser cancelada | Em `ReservaService.TRANSICOES`, incluir `CANCELADA` entre os estados permitidos a partir de `EM_USO` | `./mvnw -B -Dtest=AprovacaoTest#cancelarReservaEmUsoRetorna409 test` | 1 teste, 1 falha: esperava HTTP 409, recebeu HTTP 204; BUILD FAILURE | Alteracao temporaria removida de `ReservaService`; regra de producao restaurada |

Esta mutacao testa a maquina de estados no servico. A verificacao de posse do solicitante e uma regra separada, coberta pela mutacao de `ReservaController` acima.
