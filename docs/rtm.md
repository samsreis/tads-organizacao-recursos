# RTM - Matriz de rastreabilidade

Data: 2026-10-29.
Cada linha liga requisito critico, risco, teste automatizado localizavel por arquivo e nome de metodo, e evidencia.

| Requisito | Risco | Teste (arquivo e metodo) | Nivel | Evidencia |
|---|---|---|---|---|
| RF-01 autenticacao por perfil | qualquer pessoa acessar dado alheio | AutorizacaoPorPerfilTest.todosOsEndpointsProtegidos | API caixa-preta | pipeline verde |
| RF-01 perfil errado recebe 403 | escalada de permissao | AutorizacaoPorPerfilTest.perfilErradoRecebe403 | API caixa-preta | pipeline verde |
| RF-01 sem token recebe 401 | acesso anonimo | AutorizacaoPorPerfilTest.semTokenRecebe401 | API caixa-preta | pipeline verde |
| RF-04 criar reserva | fluxo principal quebrado | RegrasReservaTest.criaReservaComSucesso | API caixa-preta | pipeline verde |
| RF-04 alterar reserva pelo dono | dono nao consegue ajustar | AlteracaoReservaTest.donoAltera200 | API caixa-preta | pipeline verde |
| RF-04 alterar reserva de outro | posse violada | AlteracaoReservaTest.outroSolicitanteRecebe403 | API caixa-preta | pipeline verde |
| RF-04 cancelar reserva propria | dono nao consegue cancelar | PosseReservaTest.donoCancela204 | API caixa-preta | pipeline verde |
| RF-04 cancelar reserva de outro | posse violada | PosseReservaTest.outroRecebe403 | API caixa-preta | pipeline verde |
| RF-05 sobreposicao de sala | dupla reserva | SobreposicaoTest.bloqueiaSobreposicao | integracao com Testcontainers | pipeline verde |
| RF-05 sobreposicao parcial | dupla reserva | RegrasReservaTest.sobreposicaoParcialBloqueada | API caixa-preta | pipeline verde |
| RF-05 sobreposicao de professor | professor em dois lugares | SobreposicaoRecursosTest.professorSobrepostoBloqueado | integracao | pipeline verde |
| RF-05 sobreposicao de material | material em dois lugares | SobreposicaoRecursosTest.materialSobrepostoBloqueado | integracao | pipeline verde |
| RF-06 dupla reserva simultanea | duas reservas aceitas | ConcorrenciaTest.dezRequisicoesApenasUmaAceita | integracao com Testcontainers e threads | pipeline verde |
| RNF-AUT listagem de todas as reservas | vazamento de reservas entre perfis | AutorizacaoPorPerfilTest.matrizDeAutorizacao | api com MockMvc e tokens | pipeline verde |
| RF-07 aprovacao por responsavel | solicitante aprova a si mesmo | AprovacaoTest.responsavelAprovaReserva201 | API caixa-preta | pipeline verde |
| RF-07 solicitante nao aprova | escalada | AprovacaoTest.solicitanteNaoPodeAprovar403 | API caixa-preta | pipeline verde |
| RF-07 nao aprovar duas vezes | estado invalido | AprovacaoTest.naoAprovaReservaJaAprovada409 | API caixa-preta | pipeline verde |
| RF-08 sala em manutencao | reserva de sala em obra | RegrasReservaTest.salaEmManutencaoBloqueada | API caixa-preta | pipeline verde |
| RF-08 material em manutencao | retirada de material em conserto | MovimentacaoMaterialTest.materialEmManutencao409 | API caixa-preta | pipeline verde |
| RF-09 retirada de material | saldo errado | MovimentacaoMaterialTest.responsavelRetiraTresDeCinco | API caixa-preta | pipeline verde |
| RF-09 devolucao de material | saldo errado | MovimentacaoMaterialTest.devolucaoAposRetirada | API caixa-preta | pipeline verde |
| RF-09 retirada maior que disponivel | saldo negativo | MovimentacaoMaterialTest.retiradaMaiorQueDisponivel409 | API caixa-preta | pipeline verde |
| RF-10 auditoria de cada transicao | historico perdido | AuditoriaTest.criacaoGeraRegistro | integracao | pipeline verde |
| RF-10 auditoria na mesma transacao | auditoria orfa | AuditoriaTest.semAuditoriaOrfa | integracao | pipeline verde |
| RF-10 historico so para responsavel e admin | dado sensivel exposto | AuditoriaTest.solicitanteRecebe403 | API caixa-preta | pipeline verde |
| RF-11 notificacao externa | integracao ausente | NotificacaoTest.aprovarEnviaNotificacaoParaProvedor | integracao com WireMock | pipeline verde |
| RF-11 tolerancia a falha do provedor | falha do provedor derruba o fluxo | NotificacaoTest.provedorComErro500NaoQuebraTransicao | integracao com WireMock | pipeline verde |
| RF-11 timeout do provedor | resposta lenta derruba o fluxo | NotificacaoTest.provedorLentoNaoQuebraTransicao | integracao com WireMock | pipeline verde |
| RF-12 relatorio de utilizacao | numero errado | RelatorioUtilizacaoTest.responsavelVeRelatorio | API caixa-preta | pipeline verde |
| RF-12 relatorio so para responsavel e admin | dado sensivel exposto | RelatorioUtilizacaoTest.solicitanteRecebe403 | API caixa-preta | pipeline verde |
| RF-13 mensagem de erro amigavel | stack trace exposto | ValidacaoEntradaTest.todosOsCamposInvalidosRecebem400 | API caixa-preta | pipeline verde |
| Regra: termino depois do inicio | estado invalido | ValidacaoEntradaTest.terminoAntesDoInicioRecebe400 | API caixa-preta | pipeline verde |
| Regra: transicao valida | estado invalido | MaquinaEstadosTest.solicitadaNaoPodeIrDiretoParaConcluida409 | API caixa-preta | pipeline verde |
| Regra: transicao invalida bloqueia | estado invalido | MaquinaEstadosTest.rejeitadaNaoVoltaParaAprovada409 | API caixa-preta | pipeline verde |
| Regra: auditoria de todas as transicoes | historico perdido | MaquinaEstadosTest.historicoRegistraCadaTransicao | API caixa-preta | pipeline verde |
| Regra: reserva iniciada nao apaga | perda de historico | AprovacaoTest.cancelarReservaIiniciadaRetorna409 | API caixa-preta | pipeline verde |

## Requisitos sem evidencia de execucao no commit avaliado

Nenhum nesta lista. A evidencia de cada linha e o pipeline verde no commit da tag v1.2.2, que sera registrado no README do pacote de evidencias.

## Nota

- Testes com foco em requisito usam MockMvc e Testcontainers para ambiente real.
- Nenhum teste desta tabela usa mock de banco.
- Teste que continua verde com a regra quebrada e lacuna. Ver docs/testes/mutacao-manual.md.
