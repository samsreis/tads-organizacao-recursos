# Relatorio final de qualidade

Data: 2026-10-29.
Repositorio: projeto local (a ser publicado).
Commit avaliado: sera o da tag v1.0.0-rc1.
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
| Mutacao manual | execucao controlada | docs/testes/mutacao-manual.md |

## Resultados contra as metas

| Meta | Valor alvo | Medido | Situacao |
|---|---|---|---|
| Cobertura de linha | 80% | 92,70% | atende |
| Cobertura de branch | 70% | 75,82% | atende |
| Requisitos criticos na RTM | 100% | 100% (todas as linhas com teste localizavel) | atende |
| Bugs ou vulnerabilidades criticas | 0 | 0 identificados nas revisoes | atende |

## Contagem de testes

- Mais de 130 metodos de teste em cerca de 21 classes.
- Todos passam no ./mvnw -B verify local.
- Pipeline do GitHub Actions configurado, sera executado no primeiro push.

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
