# Relatorio final de qualidade

Data: 2026-11-10.
Repositorio: https://github.com/samsreis/tads-organizacao-recursos.
Autor: Samuel Silva dos Reis.
Integrante: Samuel Silva dos Reis (Trabalho Individual)
Branch avaliada: `main`.
Commit avaliado: o commit da tag de entrega.
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
| Cobertura de linha | 80% | 93,6% no JaCoCo (663 de 708 linhas) | atende |
| Cobertura de branch | 70% | 78,3% no JaCoCo (137 de 175 branches) | atende |
| Requisitos criticos na RTM | 100% | 100% (todas as linhas com teste localizavel) | atende |
| Bugs ou vulnerabilidades criticas abertas | 0 | DEF-03 (critica) foi corrigido; analise SonarCloud mais recente com Quality Gate Passed, zero issues BLOCKER ou CRITICAL | concluido para os defeitos registrados |

## Contagem de testes

- 147 execucoes de teste em 23 classes.
- `./mvnw -B verify` passou localmente em 2026-11-10, sem falhas ou erros.
- As tags anteriores, incluindo v1.3.2, possuem execucoes historicas no GitHub Actions. A tag v1.4.2 foi publicada com CI verde em https://github.com/samsreis/tads-organizacao-recursos/actions?query=branch%3Av1.4.2.
- A analise SonarCloud mais recente com Quality Gate OK e zero issues BLOCKER/CRITICAL: Quality Gate Passed, sem issues ou hotspots.

## Defeitos registrados

| ID | Defeito | Severidade | Situacao |
|---|---|---|---|
| DEF-01 | Sem token retornava 403 em vez de 401 | Media | Concluido (c0a8bcc) |
| DEF-02 | Erro de validacao sem mensagem amigavel | Media | Concluido (420677d) |
| DEF-03 | Cadastro publico aceitava perfil privilegiado | Critica | Concluido (771e5d1) |
| DEF-04 | Mensagem antiga permanecia na tela ao trocar de usuario | Baixa | Concluido (fix/frontend-estados-v1.3.2) |
| DEF-05 | Lista de aprovacao mostrava reservas ja finalizadas | Media | Concluido (fix/frontend-estados-v1.3.2) |
| DEF-06 | Botoes de acao ignoravam a maquina de estados | Media | Concluido (fix/frontend-estados-v1.3.2) |

## Performance

- Rodada 02 no Codespace: login p95 209,05 ms, GET salas p95 17,45 ms, POST reserva p95 39,90 ms.
- POST reserva teve uma resposta 201, 249 respostas 409 esperadas e nenhuma resposta 500.
- Metas atendidas nesta rodada unica; detalhes em docs/performance/resultado-02.md.

## Limitacoes

- Todos os numeros desta revisao sao de ambiente local, nao de producao.
- Uma unica rodada JMeter na revisao atual.
- Cobertura alta nao prova qualidade dos testes. A protecao foi conferida com mutacao manual.

## Conclusao

O verify local, as metas de cobertura e a rodada de carga atenderam aos criterios registrados. A versao avaliada e v1.4.2; a tag v1.4.2 foi publicada com CI verde em https://github.com/samsreis/tads-organizacao-recursos/actions?query=branch%3Av1.4.2. A decisao esta em docs/decisao-qualidade.md.
Analise atual do SonarCloud: https://sonarcloud.io/project/overview?id=samsreis_tads-organizacao-recursos. Quality Gate Passed, zero issues BLOCKER ou CRITICAL.
