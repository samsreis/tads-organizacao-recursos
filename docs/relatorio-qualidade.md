# Relatorio final de qualidade

Data: 2026-11-09.
Repositorio: https://github.com/samsreis/tads-organizacao-recursos.
Commit avaliado: tag v1.3.0 no commit final da main, com documentos posteriores ja integrados na main em d545ba9.
Ambiente de medicao: Linux Mint, Java 21, Maven wrapper, Docker com Testcontainers e Postgres 16.

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
| Mutacao manual | execucao controlada | docs/testes/mutacao-manual.md, com 10 regras e duas provas reexecutadas em 2026-11-09 |

## Resultados contra as metas

| Meta | Valor alvo | Medido | Situacao |
|---|---|---|---|
| Cobertura de linha | 80% | 90% no JaCoCo em clone limpo (3.396 de 3.741 linhas) e 89,1% no SonarCloud | atende |
| Cobertura de branch | 70% | 77% local (40 de 175 branches, JaCoCo) | atende |
| Requisitos criticos na RTM | 100% | 100% (todas as linhas com teste localizavel) | atende |
| Bugs ou vulnerabilidades criticas | 0 | 0 issues, 0 security issues, 0 hotspots no SonarCloud, Security A, Reliability A, Maintainability A | atende |

## Contagem de testes

- 144 metodos de teste em cerca de 21 classes.
- Todos passam no ./mvnw -B verify local.
- Pipeline do GitHub Actions verde nas tags v1.2.2, v1.2.3, v1.2.4, v1.2.5, v1.2.6, v1.2.7, v1.2.8 e v1.3.0, sempre com Quality Gate obrigatorio.
- SonarCloud: projeto publico, Quality Gate Passed, 0 issues, 0 security issues, 0 hotspots, Security A, Reliability A, Maintainability A, coverage 89,1%, duplicacao 2,4%, 1.8k linhas.

## Defeitos

- DEF-01 sem token retornava 403: fechado (c0a8bcc).
- DEF-02 erro de validacao sem mensagem amigavel: fechado (420677d).
- Intermitencia do teste de concorrencia: fechado (e62e2b8, ADR-004).

## Performance

- POST login p95 484 ms, acima do alvo de 300 ms. Gargalo identificado: BCrypt em 50 requisicoes simultaneas.
- GET salas p95 39 ms.
- POST reserva p95 68 ms, com 1 aceita e 249 conflitos, zero 500.
- Detalhe em docs/performance/resultado-01.md.

## Limitacoes

- Todos os numeros sao de ambiente local, nao de producao.
- Uma unica rodada de carga.
- Cobertura alta nao prova qualidade dos testes. A protecao foi conferida com mutacao manual.

## Conclusao

O sistema atende os requisitos criticos e as metas mensuraveis. As pendencias sao parciais e nao bloqueiam a entrega. A decisao final esta em docs/decisao-qualidade.md.
Quality Gate do SonarCloud: https://sonarcloud.io/project/overview?id=samsreis_tads-organizacao-recursos
