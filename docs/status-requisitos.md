# Status dos requisitos

Data: 2026-11-10.

## Requisitos funcionais obrigatorios (14)

| Numero | Requisito | Situacao | Evidencia | Risco se ficar de fora |
|---|---|---|---|---|
| RF-01 | Autenticacao e autorizacao por perfil | feito | AuthControllerTest, AutorizacaoPorPerfilTest | limitador de nota: falha de autorizacao |
| RF-02 | Cadastro e consulta de salas, professores e materiais | feito | SalaControllerTest, ProfessorMaterialTest, ProfessorMaterialCrudTest | limitador de nota: aplicacao nao cobre o basico |
| RF-03 | Pesquisa por tipo, capacidade, localizacao, competencia e disponibilidade | feito | FiltroSalaTest cobre capacidade, localizacao e disponibilidade. Filtros de material e professor em ProfessorMaterialCrudTest | perde ponto em requisitos |
| RF-04 | Criacao, alteracao e cancelamento de reservas | feito | RegrasReservaTest, AlteracaoReservaTest, PosseReservaTest, AutorizacaoPorPerfilTest | limitador de nota: fluxo principal |
| RF-05 | Deteccao de sobreposicao, inclusive agenda do professor | feito | SobreposicaoTest, SobreposicaoRecursosTest, ConcorrenciaTest.apenasUmaReservaAceitaParaProfessorEmSolicitacoesSimultaneas | limitador de nota: dupla reserva |
| RF-06 | Protecao contra dupla reserva em solicitacoes simultaneas | feito | ConcorrenciaTest.apenasUmaReservaAceitaEmDezSolicitacoesSimultaneas, ConcorrenciaTest.apenasUmaReservaAceitaParaProfessorEmSolicitacoesSimultaneas, ADR-001, ADR-004 | limitador de nota: dupla reserva |
| RF-07 | Aprovacao obrigatoria para recursos restritos | feito | AprovacaoTest, MaquinaEstadosTest | perde ponto em requisitos |
| RF-08 | Bloqueio de recursos em manutencao | feito | RegrasReservaTest, SobreposicaoRecursosTest | perde ponto em requisitos |
| RF-09 | Registro de retirada e devolucao de materiais | feito | MovimentacaoMaterialTest | perde ponto em requisitos |
| RF-10 | Historico auditavel de mudancas | feito | AuditoriaTest, AuditoriaRollbackTest.falhaNaAuditoriaDesfazAReserva, MaquinaEstadosTest | perde ponto em rastreabilidade |
| RF-11 | Notificacao simulada ou integracao externa | feito | NotificacaoTest com WireMock | perde ponto em integracao |
| RF-12 | Relatorio de utilizacao por recurso, carga horaria e conflitos evitados | feito | RelatorioUtilizacaoTest.responsavelVeRelatorio, RelatorioUtilizacaoTest.conflitoEvitadoApareceNoRelatorio | perde ponto em requisitos |
| RF-13 | Interface responsiva e mensagens de erro compreensiveis | Parcial (sem teste de UI, apenas API) | src/main/resources/static com login, filtro, reserva e aprovacao; mensagens de erro no TratadorGlobal | perde ponto em apresentacao |
| RF-14 | Documentacao de API ou fluxos publicos | feito | README.md e docs/seguranca/matriz-autorizacao.md | perde ponto em organizacao |

## Regras criticas

| Regra | Situacao | Evidencia |
|---|---|---|
| Termino posterior ao inicio | feito | RegrasReservaTest, ValidacaoEntradaTest |
| Reservas do mesmo recurso nao se sobrepoem | feito | SobreposicaoTest, SobreposicaoRecursosTest |
| Duas solicitacoes simultaneas produzem uma unica reserva | feito | ConcorrenciaTest |
| Recursos em manutencao nao podem ser reservados | feito | RegrasReservaTest, SobreposicaoRecursosTest |
| Maquina de estados completa | feito | MaquinaEstadosTest |
| Somente RESPONSAVEL e ADMINISTRADOR aprovam restrito | feito | AprovacaoTest, MaquinaEstadosTest |
| Reservas em uso nao podem ser canceladas | feito | AprovacaoTest.cancelarReservaEmUsoRetorna409 |
| Toda mudanca de estado gera auditoria | feito | AuditoriaTest, MaquinaEstadosTest |

## Acompanhamento de qualidade

- SonarCloud integrado; a analise de referencia documentada para v1.3.2 passou no Quality Gate.
- Revisao local e documentacao da versao candidata v1.3.4 concluidas. A publicacao remota depende de autenticacao GitHub, indisponivel neste ambiente.
- JMeter executado em 2026-11-10 no Codespace: p95 de login 209,05 ms, salas 17,45 ms e reserva 39,9 ms; metas atendidas nesta rodada. Resultado em docs/performance/resultado-02.md.

## Estado do GitHub

Repositorio publico: https://github.com/samsreis/tads-organizacao-recursos.
Versao candidata desta rodada: v1.3.4. A ultima tag publicada conhecida permanece v1.3.2.

## Nao cortar

- Aplicacao executa; `./mvnw -B verify` passou localmente em 2026-11-10.
- Autorizacao testada e sobreposicao de sala, professor e material bloqueada.
