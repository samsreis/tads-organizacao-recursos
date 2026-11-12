# Revisao integrada - regras criticas

Data: 2026-11-10. Revisao local da branch `main`; `./mvnw -B verify` passou com 147 execucoes. A versao candidata desta rodada e v1.4.0.

| Regra critica | Requisito | Risco | Tecnica usada | Automacao | Evidencia | Decisao |
|---|---|---|---|---|---|---|
| Termino posterior ao inicio | RF-04 | estado invalido aceito | caixa-preta | ValidacaoEntradaTest, RegrasReservaTest | execucao da tag v1.4.0, tag v1.4.0 | manter |
| Sobreposicao de sala | RF-05 | dupla reserva | integracao Testcontainers | SobreposicaoTest, RegrasReservaTest | execucao da tag v1.4.0, tag v1.4.0 | manter |
| Sobreposicao de professor | RF-05 | professor em dois lugares | integracao | SobreposicaoRecursosTest | execucao da tag v1.4.0, tag v1.4.0 | manter |
| Concorrencia de professor | RF-05/RF-06 | duas reservas concorrentes para o mesmo professor | Testcontainers, 10 threads e salas distintas | ConcorrenciaTest.apenasUmaReservaAceitaParaProfessorEmSolicitacoesSimultaneas | verify local 2026-11-10 | manter |
| Sobreposicao de material | RF-05 | material em dois lugares | integracao | SobreposicaoRecursosTest | execucao da tag v1.4.0, tag v1.4.0 | manter |
| Dupla reserva simultanea | RF-06 | duas reservas aceitas | integracao com threads | ConcorrenciaTest | execucao da tag v1.4.0, tag v1.4.0 | manter |
| Manutencao de sala | RF-08 | reserva de sala em obra | caixa-preta | RegrasReservaTest | execucao da tag v1.4.0, tag v1.4.0 | manter |
| Manutencao de material | RF-08 | retirada em conserto | caixa-preta | MovimentacaoMaterialTest | execucao da tag v1.4.0, tag v1.4.0 | manter |
| Aprovacao de restrito | RF-07 | solicitante aprova a si mesmo | caixa-preta | AprovacaoTest | execucao da tag v1.4.0, tag v1.4.0 | manter |
| Maquina de estados | RF-07 | estado invalido | caixa-preta | MaquinaEstadosTest, AprovacaoTest.cancelarReservaEmUsoRetorna409 | verify local e mutacao manual 2026-11-10 | manter |
| Auditoria | RF-10 | historico perdido ou reserva sem auditoria | integracao | AuditoriaTest, AuditoriaRollbackTest.falhaNaAuditoriaDesfazAReserva, MaquinaEstadosTest | verify local 2026-11-10 | manter |
| Autorizacao | RF-01 | escalada de permissao | caixa-preta parametrizada | AutorizacaoPorPerfilTest | execucao da tag v1.4.0, tag v1.4.0 | manter |

## Celulas vazias

Nenhuma lacuna de teste local identificada. A evidencia remota para esta revisao depende da execucao de CI no PR.

## Resultados revalidados

- `./mvnw -B verify`: 145 execucoes, 0 falhas, 0 erros.
- JaCoCo: 92,8% de linhas e 77,1% de branches.
- Mutacao em `ReservaService.TRANSICOES`, permitindo cancelamento a partir de `EM_USO`: `AprovacaoTest.cancelarReservaEmUsoRetorna409` falhou com HTTP 204 em vez de 409; a regra foi restaurada.
- JMeter: resultado atual em docs/performance/resultado-02.md.

## Prova de que o teste protege a regra

Cada regra critica tem par de tecnica: teste automatizado mais mutacao manual registrada em docs/testes/mutacao-manual.md. Onze regras com mutacao comprovada; tres provas reexecutadas recentemente: posse do cancelamento e aprovacao em 2026-11-09, e cancelamento em uso no `ReservaService` em 2026-11-10.

## Entregaveis da Aula 16 fora de escopo

Revisao cruzada depende de repositorio de equipe vizinha, combinado em sala. Incidente simulado depende de branch descartavel e equipe para localizar o defeito. O trabalho individual de Samuel Silva dos Reis nao teve equipe vizinha disponivel para esses dois exercicios. Registrado como risco aceito em docs/decisao-qualidade.md.
