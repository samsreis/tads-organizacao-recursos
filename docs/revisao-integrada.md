# Revisao integrada - regras criticas

Data: 2026-10-29.

| Regra critica | Requisito | Risco | Tecnica usada | Automacao | Evidencia | Decisao |
|---|---|---|---|---|---|---|
| Termino posterior ao inicio | RF-04 | estado invalido aceito | caixa-preta | ValidacaoEntradaTest, RegrasReservaTest | pipeline verde | manter |
| Sobreposicao de sala | RF-05 | dupla reserva | integracao Testcontainers | SobreposicaoTest, RegrasReservaTest | pipeline verde | manter |
| Sobreposicao de professor | RF-05 | professor em dois lugares | integracao | SobreposicaoRecursosTest | pipeline verde | manter |
| Sobreposicao de material | RF-05 | material em dois lugares | integracao | SobreposicaoRecursosTest | pipeline verde | manter |
| Dupla reserva simultanea | RF-06 | duas reservas aceitas | integracao com threads | ConcorrenciaTest | pipeline verde | manter |
| Manutencao de sala | RF-08 | reserva de sala em obra | caixa-preta | RegrasReservaTest | pipeline verde | manter |
| Manutencao de material | RF-08 | retirada em conserto | caixa-preta | MovimentacaoMaterialTest | pipeline verde | manter |
| Aprovacao de restrito | RF-07 | solicitante aprova a si mesmo | caixa-preta | AprovacaoTest | pipeline verde | manter |
| Maquina de estados | RF-07 | estado invalido | caixa-preta | MaquinaEstadosTest | pipeline verde | manter |
| Auditoria | RF-10 | historico perdido | integracao | AuditoriaTest, MaquinaEstadosTest | pipeline verde | manter |
| Autorizacao | RF-01 | escalada de permissao | caixa-preta parametrizada | AutorizacaoPorPerfilTest | pipeline verde | manter |

## Celulas vazias

Nenhuma. Todas as regras criticas tem teste localizavel e evidencia.
