# Status dos requisitos

Data: 2026-10-30.

## Requisitos funcionais obrigatorios (14)

| Numero | Requisito | Situacao | Evidencia | Risco se ficar de fora |
|---|---|---|---|---|
| RF-01 | Autenticacao e autorizacao por perfil | feito | AuthControllerTest, AutorizacaoPorPerfilTest | limitador de nota: falha de autorizacao |
| RF-02 | Cadastro e consulta de salas, professores e materiais | feito | SalaControllerTest, ProfessorMaterialTest, ProfessorMaterialCrudTest | limitador de nota: aplicacao nao cobre o basico |
| RF-03 | Pesquisa por tipo, capacidade, localizacao, competencia e disponibilidade | feito | FiltroSalaTest cobre capacidade, localizacao e disponibilidade. Filtros de material e professor em ProfessorMaterialCrudTest | perde ponto em requisitos |
| RF-04 | Criacao, alteracao e cancelamento de reservas | feito | RegrasReservaTest, AlteracaoReservaTest, PosseReservaTest | limitador de nota: fluxo principal |
| RF-05 | Deteccao de sobreposicao, inclusive agenda do professor | feito | SobreposicaoTest, SobreposicaoRecursosTest | limitador de nota: dupla reserva |
| RF-06 | Protecao contra dupla reserva em solicitacoes simultaneas | feito | ConcorrenciaTest, ADR-001, ADR-004 | limitador de nota: dupla reserva |
| RF-07 | Aprovacao obrigatoria para recursos restritos | feito | AprovacaoTest, MaquinaEstadosTest | perde ponto em requisitos |
| RF-08 | Bloqueio de recursos em manutencao | feito | RegrasReservaTest, SobreposicaoRecursosTest | perde ponto em requisitos |
| RF-09 | Registro de retirada e devolucao de materiais | feito | MovimentacaoMaterialTest | perde ponto em requisitos |
| RF-10 | Historico auditavel de mudancas | feito | AuditoriaTest, MaquinaEstadosTest | perde ponto em rastreabilidade |
| RF-11 | Notificacao simulada ou integracao externa | feito | NotificacaoTest com WireMock | perde ponto em integracao |
| RF-12 | Relatorio de utilizacao por recurso, carga horaria e conflitos evitados | feito | RelatorioUtilizacaoTest | perde ponto em requisitos |
| RF-13 | Interface responsiva e mensagens de erro compreensiveis | feito | src/main/resources/static com login, filtro, reserva e aprovacao; mensagens de erro no TratadorGlobal | perde ponto em apresentacao |
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
| Reservas iniciadas nao podem ser apagadas | feito | AprovacaoTest |
| Toda mudanca de estado gera auditoria | feito | AuditoriaTest, MaquinaEstadosTest |

## Pendencia aberta

- SonarCloud nao integrado. Requer SONAR_TOKEN nos secrets do repositorio. Pendencia justificada, nao e limitador de nota.
- JMeter: login com p95 acima da meta (484 ms contra 300 ms). Hipotese registrada em docs/performance/resultado-01.md.

## Estado do GitHub

Repositorio publico: https://github.com/samsreis/tads-organizacao-recursos
CI verde da tag v1.0.0-rc1: https://github.com/samsreis/tads-organizacao-recursos/actions/runs/41076861945
Tag v1.0.0-rc1 no commit da1b6ea. Execucoes posteriores na main sao permitidas; a tag nao e movida.

## Nao cortar

- Aplicacao executa, pipeline verde no GitHub, autorizacao testada, dupla reserva bloqueada.
