# Relatorio final de qualidade

Data: 2026-11-10.
Repositorio: https://github.com/samsreis/tads-organizacao-recursos.
Autor: Samuel Silva dos Reis.
Integrante: Samuel Silva dos Reis (Trabalho Individual)
Branch avaliada: `main`.
Commit atual no inicio desta rodada: commit atual.
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
| Bugs ou vulnerabilidades criticas abertas | 0 | DEF-03 (critica) foi corrigido; ultima analise SonarCloud documentada (v1.3.2) nao registrava issues | concluido para os defeitos registrados |

## Contagem de testes

- 147 execucoes de teste em 23 classes.
- `./mvnw -B verify` passou localmente em 2026-11-10, sem falhas ou erros.
- As tags anteriores, incluindo v1.3.2, possuem execucoes historicas no GitHub Actions. A tag v1.4.0 foi publicada com CI verde em https://github.com/samsreis/tads-organizacao-recursos/actions/runs/43150356823.
- A ultima analise SonarCloud documentada e historica (v1.3.2): Quality Gate Passed, sem issues ou hotspots. Nao foi refeita para esta branch.

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

O verify local, as metas de cobertura e a rodada de carga atenderam aos criterios registrados. A versao candidata desta rodada e v1.4.0; a tag v1.4.0 foi publicada com CI verde em https://github.com/samsreis/tads-organizacao-recursos/actions/runs/43150356823. A decisao esta em docs/decisao-qualidade.md.
Historico do SonarCloud na v1.3.2: https://sonarcloud.io/project/overview?id=samsreis_tads-organizacao-recursos. Nova analise remota sera executada quando a branch puder ser publicada.
