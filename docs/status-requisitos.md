# Status dos requisitos

Data: 2026-10-29.

## Requisitos funcionais obrigatorios (14)

| Numero | Requisito | Situacao | Evidencia | Risco se ficar de fora |
|---|---|---|---|---|
| RF-01 | Autenticacao e autorizacao por perfil | feito | AuthControllerTest, AutorizacaoPorPerfilTest | limitador de nota: falha de autorizacao |
| RF-02 | Cadastro e consulta de salas, professores e materiais | feito | SalaControllerTest, ProfessorMaterialTest, ProfessorMaterialCrudTest | limitador de nota: aplicacao nao cobre o basico |
| RF-03 | Pesquisa por tipo, capacidade, localizacao, competencia e disponibilidade | parcial | filtro por tipo em material, por competencia em professor. Falta filtro por capacidade e localizacao em sala e disponibilidade | perde ponto em requisitos |
| RF-04 | Criacao, alteracao e cancelamento de reservas | feito | RegrasReservaTest, AlteracaoReservaTest, PosseReservaTest | limitador de nota: fluxo principal |
| RF-05 | Deteccao de sobreposicao, inclusive agenda do professor | feito | SobreposicaoTest, SobreposicaoRecursosTest | limitador de nota: dupla reserva |
| RF-06 | Protecao contra dupla reserva em solicitacoes simultaneas | feito | ConcorrenciaTest, ADR-001, ADR-004 | limitador de nota: dupla reserva |
| RF-07 | Aprovacao obrigatoria para recursos restritos | feito | AprovacaoTest, MaquinaEstadosTest | perde ponto em requisitos |
| RF-08 | Bloqueio de recursos em manutencao | feito | RegrasReservaTest, SobreposicaoRecursosTest | perde ponto em requisitos |
| RF-09 | Registro de retirada e devolucao de materiais | feito | MovimentacaoMaterialTest | perde ponto em requisitos |
| RF-10 | Historico auditavel de mudancas | feito | AuditoriaTest, MaquinaEstadosTest | perde ponto em rastreabilidade |
| RF-11 | Notificacao simulada ou integracao externa | feito | NotificacaoTest com WireMock | perde ponto em integracao |
| RF-12 | Relatorio de utilizacao por recurso, carga horaria e conflitos evitados | feito | RelatorioUtilizacaoTest | perde ponto em requisitos |
| RF-13 | Interface responsiva e mensagens de erro compreensiveis | parcial | mensagens de erro prontas. Interface HTML falta | perde ponto em apresentacao |
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

- RF-03 parcial: adicionar filtro por capacidade e localizacao em GET /salas. Tempo estimado 30 minutos. Nao e limitador de nota.
- RF-13 parcial: interface HTML. Tempo estimado 1 hora. Nao e limitador de nota, mas conta em apresentacao.

## Nao cortar

- Aplicacao executa, pipeline verde no GitHub, autorizacao testada, dupla reserva bloqueada.
