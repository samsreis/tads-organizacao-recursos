# Relatorio final de qualidade

Data: 2026-11-10.
Repositorio: https://github.com/samsreis/tads-organizacao-recursos.
Revisao avaliada: branch local `fix/auditoria-completa`, antes do PR; ultima tag publicada conhecida: v1.3.2.
Ambiente de medicao: Ubuntu 24.04, Java 21.0.5, Maven wrapper, Docker com Testcontainers e Postgres 16.

## Escopo avaliado

- Todos os 14 requisitos funcionais obrigatorios.
- Todas as regras criticas da especificacao.
- Autenticacao e autorizacao por perfil.
- Concorrencia e deteccao de sobreposicao.
- Auditoria de mudanca de estado.
- Notificacao externa com WireMock.
- Teste de carga com JMeter.

## Fora de escopo

- Producao real.
- Carga acima de 50 usuarios.
- Interface HTML completa (parcial).
- Filtro por capacidade e localizacao em sala (parcial).

## Estrategia de teste por nivel

| Nivel | Ferramenta | Onde |
|---|---|---|
| Unidade | JUnit 5, Mockito pontual | poucos casos, foco em DTO e regra isolada |
| Integracao com banco real | JUnit 5 + Testcontainers Postgres 16 | SobreposicaoTest, ConcorrenciaTest, AuditoriaTest |
| API caixa-preta | MockMvc + tokens reais | AutorizacaoPorPerfilTest, RegrasReservaTest, AprovacaoTest, MaquinaEstadosTest |
| Integracao externa | WireMock | NotificacaoTest |
| Carga | JMeter 5.6.3 CLI | perf/reservas.jmx |
| Mutacao manual | execucao controlada | docs/testes/mutacao-manual.md, com 11 regras e prova do cancelamento em uso reexecutada em 2026-11-10 |

## Resultados contra as metas

| Meta | Valor alvo | Medido | Situacao |
|---|---|---|---|
| Cobertura de linha | 80% | 92,8% no JaCoCo (655 de 706 linhas) | atende |
| Cobertura de branch | 70% | 77,1% no JaCoCo (135 de 175 branches) | atende |
| Requisitos criticos na RTM | 100% | 100% (todas as linhas com teste localizavel) | atende |
| Bugs ou vulnerabilidades criticas | 0 | ultima analise SonarCloud documentada (v1.3.2) nao registrava issues; branch atual ainda sem nova analise | pendente de reavaliacao remota |

## Contagem de testes

- 145 execucoes de teste em 23 classes.
- `./mvnw -B verify` passou localmente em 2026-11-10, sem falhas ou erros.
- As tags anteriores, incluindo v1.3.2, possuem execucoes historicas no GitHub Actions; a execucao de CI para esta revisao ainda esta pendente.
- A ultima analise SonarCloud documentada e historica (v1.3.2): Quality Gate Passed, sem issues ou hotspots. Nao foi refeita para esta branch.

## Defeitos

- DEF-01 sem token retornava 403: fechado (c0a8bcc).
- DEF-02 erro de validacao sem mensagem amigavel: fechado (420677d).
- Intermitencia do teste de concorrencia: fechado (e62e2b8, ADR-004).

## Performance

- Rodada 02 no Codespace: login p95 209,05 ms, GET salas p95 17,45 ms, POST reserva p95 39,90 ms.
- POST reserva teve uma resposta 201, 249 respostas 409 esperadas e nenhuma resposta 500.
- Metas atendidas nesta rodada unica; detalhes em docs/performance/resultado-02.md.

## Limitacoes

- Todos os numeros desta revisao sao de ambiente local, nao de producao.
- Uma unica rodada JMeter na revisao atual.
- Cobertura alta nao prova qualidade dos testes. A protecao foi conferida com mutacao manual.

## Conclusao

O verify local, as metas de cobertura e a rodada de carga atenderam aos criterios registrados. A aprovacao de release permanece condicionada ao CI remoto, merge e tag v1.3.3; essa evidência ainda nao existe para esta revisao. A decisao esta em docs/decisao-qualidade.md.
Historico do SonarCloud na v1.3.2: https://sonarcloud.io/project/overview?id=samsreis_tads-organizacao-recursos. Esta branch aguarda nova analise remota.
