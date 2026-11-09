# Revisao integrada - regras criticas

Data: 2026-10-29. Revisao: 2026-11-09, sobre a tag v1.3.2 no commit final da main, CI verde na execucao da tag v1.3.2.

| Regra critica | Requisito | Risco | Tecnica usada | Automacao | Evidencia | Decisao |
|---|---|---|---|---|---|---|
| Termino posterior ao inicio | RF-04 | estado invalido aceito | caixa-preta | ValidacaoEntradaTest, RegrasReservaTest | execucao da tag v1.3.2, tag v1.3.2 | manter |
| Sobreposicao de sala | RF-05 | dupla reserva | integracao Testcontainers | SobreposicaoTest, RegrasReservaTest | execucao da tag v1.3.2, tag v1.3.2 | manter |
| Sobreposicao de professor | RF-05 | professor em dois lugares | integracao | SobreposicaoRecursosTest | execucao da tag v1.3.2, tag v1.3.2 | manter |
| Sobreposicao de material | RF-05 | material em dois lugares | integracao | SobreposicaoRecursosTest | execucao da tag v1.3.2, tag v1.3.2 | manter |
| Dupla reserva simultanea | RF-06 | duas reservas aceitas | integracao com threads | ConcorrenciaTest | execucao da tag v1.3.2, tag v1.3.2 | manter |
| Manutencao de sala | RF-08 | reserva de sala em obra | caixa-preta | RegrasReservaTest | execucao da tag v1.3.2, tag v1.3.2 | manter |
| Manutencao de material | RF-08 | retirada em conserto | caixa-preta | MovimentacaoMaterialTest | execucao da tag v1.3.2, tag v1.3.2 | manter |
| Aprovacao de restrito | RF-07 | solicitante aprova a si mesmo | caixa-preta | AprovacaoTest | execucao da tag v1.3.2, tag v1.3.2 | manter |
| Maquina de estados | RF-07 | estado invalido | caixa-preta | MaquinaEstadosTest | execucao da tag v1.3.2, tag v1.3.2 | manter |
| Auditoria | RF-10 | historico perdido | integracao | AuditoriaTest, MaquinaEstadosTest | execucao da tag v1.3.2, tag v1.3.2 | manter |
| Autorizacao | RF-01 | escalada de permissao | caixa-preta parametrizada | AutorizacaoPorPerfilTest | execucao da tag v1.3.2, tag v1.3.2 | manter |

## Celulas vazias

Nenhuma. Todas as regras criticas tem teste localizavel e evidencia na execucao da tag v1.3.2.

## Prova de que o teste protege a regra

Cada regra critica tem par de tecnica: teste automatizado mais mutacao manual registrada em docs/testes/mutacao-manual.md. Dez regras com mutacao comprovada, sendo duas reexecutadas em 2026-11-09 com teste vermelho comprovado: posse do cancelamento em ReservaController linha 173 e aprovacao de restrito em SecurityConfig linha 62.

## Entregaveis da Aula 16 fora de escopo

Revisao cruzada depende de repositorio de equipe vizinha, combinado em sala. Incidente simulado depende de branch descartavel e equipe para localizar o defeito. Grupo de um integrante nao tem par para esses dois exercicios. Registrado como risco aceito em docs/decisao-qualidade.md.
