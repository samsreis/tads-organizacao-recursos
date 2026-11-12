# RTM - Matriz de rastreabilidade

Data: 2026-11-10.
Cada linha liga requisito critico, risco, teste automatizado localizavel por arquivo e nome de metodo, e evidencia. Todos os testes listados estao incluidos no `./mvnw -B verify` local desta revisao (147 execucoes, sem falhas). As celulas `pipeline verde` sao evidencia historica da release anterior.

| Requisito | Risco | Teste (arquivo e metodo) | Nivel | Evidencia |
|---|---|---|---|---|
| RF-01 autenticacao por perfil | qualquer pessoa acessar dado alheio | AutorizacaoPorPerfilTest.matrizDeAutorizacao | API caixa-preta | pipeline verde |
| RF-01 perfil errado recebe 403 | escalada de permissao | AutorizacaoPorPerfilTest.matrizDeAutorizacao | API caixa-preta | pipeline verde |
| RF-01 sem token recebe 401 | acesso anonimo | AutorizacaoPorPerfilTest.matrizDeAutorizacao | API caixa-preta | pipeline verde |
| RF-02 consulta sala por id | recurso existente nao e retornado | AutorizacaoPorPerfilTest.endpointsPorIdExigemAutenticacao | API caixa-preta | verify local |
| RF-02 consulta professor por id | recurso existente nao e retornado | AutorizacaoPorPerfilTest.endpointsPorIdExigemAutenticacao | API caixa-preta | verify local |
| RF-04 criar reserva | fluxo principal quebrado | E2ETest.fluxoCompletoCadastroLoginReservaAprovacaoUsoConclusao | API caixa-preta | pipeline verde |
| RF-04 alterar reserva pelo dono | dono nao consegue ajustar | AlteracaoReservaTest.donoAlteraPropriaReserva200 | API caixa-preta | pipeline verde |
| RF-04 alterar reserva de outro | posse violada | AlteracaoReservaTest.outroSolicitanteNaoPodeAlterar403 | API caixa-preta | pipeline verde |
| RF-04 cancelar reserva propria | dono nao consegue cancelar | PosseReservaTest.donoCancelaPropriaReserva204 | API caixa-preta | pipeline verde |
| RF-04 cancelar reserva de outro | posse violada | PosseReservaTest.outroSolicitanteNaoPodeCancelarReservaAlheia403 | API caixa-preta | pipeline verde |
| RF-05 sobreposicao de sala | dupla reserva | SobreposicaoTest.bancoBloqueiaSobreposicaoNaMesmaSala | integracao com Testcontainers | pipeline verde |
| RF-05 sobreposicao parcial | dupla reserva | RegrasReservaTest.sobreposicaoParcialNoInicioRetorna409 | API caixa-preta | pipeline verde |
| RF-05 sobreposicao de professor | professor em dois lugares | SobreposicaoRecursosTest.professorNaoPodeTerDuasReservasSobrepostas | integracao | pipeline verde |
| RF-05 sobreposicao de material | material em dois lugares | SobreposicaoRecursosTest.materialNaoPodeTerDuasReservasSobrepostas | integracao | pipeline verde |
| RF-06 dupla reserva simultanea | duas reservas aceitas | ConcorrenciaTest.apenasUmaReservaAceitaEmDezSolicitacoesSimultaneas | integracao com Testcontainers e threads | pipeline verde |
| RF-06 sobreposicao concorrente de professor | professor alocado simultaneamente em duas salas | ConcorrenciaTest.apenasUmaReservaAceitaParaProfessorEmSolicitacoesSimultaneas | integracao com Testcontainers e threads | verify local |
| RNF-AUT listagem de todas as reservas | vazamento de reservas entre perfis | AutorizacaoPorPerfilTest.matrizDeAutorizacao | api com MockMvc e tokens | pipeline verde |
| RF-07 aprovacao por responsavel | solicitante aprova a si mesmo | AprovacaoTest.responsavelAprovaReserva200 | API caixa-preta | pipeline verde |
| RF-07 solicitante nao aprova | escalada | AprovacaoTest.solicitanteNaoPodeAprovar403 | API caixa-preta | pipeline verde |
| RF-07 nao aprovar duas vezes | estado invalido | AprovacaoTest.naoAprovaReservaJaAprovada409 | API caixa-preta | pipeline verde |
| RF-08 sala em manutencao | reserva de sala em obra | RegrasReservaTest.salaEmManutencaoRetorna409 | API caixa-preta | pipeline verde |
| RF-08 material em manutencao | retirada de material em conserto | MovimentacaoMaterialTest.materialEmManutencao409 | API caixa-preta | pipeline verde |
| RF-09 retirada de material | saldo errado | MovimentacaoMaterialTest.responsavelRetiraTresDeCinco | API caixa-preta | pipeline verde |
| RF-09 devolucao de material | saldo errado | MovimentacaoMaterialTest.devolucaoAposRetirada | API caixa-preta | pipeline verde |
| RF-09 retirada maior que disponivel | saldo negativo | MovimentacaoMaterialTest.retiradaMaiorQueDisponivel409 | API caixa-preta | pipeline verde |
| RF-10 auditoria de cada transicao | historico perdido | AuditoriaTest.criacaoGeraRegistroDeAuditoria | integracao | pipeline verde |
| RF-10 auditoria na mesma transacao | reserva salva sem auditoria | AuditoriaRollbackTest.falhaNaAuditoriaDesfazAReserva | integracao com falha simulada | verify local |
| RF-10 historico so para responsavel e admin | dado sensivel exposto | AuditoriaTest.solicitanteNaoPodeVerHistorico403 | API caixa-preta | pipeline verde |
| RF-11 notificacao externa | integracao ausente | NotificacaoTest.aprovarEnviaNotificacaoParaProvedor | integracao com WireMock | pipeline verde |
| RF-11 tolerancia a falha do provedor | falha do provedor derruba o fluxo | NotificacaoTest.provedorComErro500NaoQuebraTransicao | integracao com WireMock | pipeline verde |
| RF-11 timeout do provedor | resposta lenta derruba o fluxo | NotificacaoTest.provedorLentoNaoQuebraTransicao | integracao com WireMock | pipeline verde |
| RF-12 relatorio de utilizacao e carga horaria | numero errado | RelatorioUtilizacaoTest.responsavelVeRelatorio | API caixa-preta, assertivas de horas exatas | verify local |
| RF-12 conflitos evitados no relatorio | contador incorreto | RelatorioUtilizacaoTest.conflitoEvitadoApareceNoRelatorio | API caixa-preta, assertiva do valor | verify local |
| RF-12 relatorio so para responsavel e admin | dado sensivel exposto | RelatorioUtilizacaoTest.solicitanteNaoVeRelatorio403 | API caixa-preta | pipeline verde |
| RF-13 mensagem de erro amigavel | stack trace exposto | ValidacaoEntradaTest.validacao | API caixa-preta | pipeline verde |
| Regra: termino depois do inicio | estado invalido | AlteracaoReservaTest.terminoAntesDoInicio400 | API caixa-preta | pipeline verde |
| Regra: transicao valida | estado invalido | MaquinaEstadosTest.solicitadaNaoPodeIrDiretoParaConcluida409 | API caixa-preta | pipeline verde |
| Regra: transicao invalida bloqueia | estado invalido | MaquinaEstadosTest.rejeitadaNaoVoltaParaAprovada409 | API caixa-preta | pipeline verde |
| Regra: auditoria de todas as transicoes | historico perdido | MaquinaEstadosTest.historicoRegistraCadaTransicao | API caixa-preta | pipeline verde |
| Regra: reserva em uso nao pode ser cancelada | estado invalido | AprovacaoTest.cancelarReservaEmUsoRetorna409 | API caixa-preta | mutacao manual detectada; verify local |

## Evidencia do commit avaliado

Os testes locais desta RTM passaram no verify de 2026-11-10. A versao candidata desta rodada e v1.4.3; o CI da tag v1.4.3 ficou verde em https://github.com/samsreis/tads-organizacao-recursos/actions?query=branch%3Av1.4.3.

## Nota

- Testes com foco em requisito usam MockMvc e Testcontainers para ambiente real.
- Nenhum teste desta tabela usa mock de banco.
- Teste que continua verde com a regra quebrada e lacuna. Ver docs/testes/mutacao-manual.md.
