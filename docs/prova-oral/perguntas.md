# Banco de perguntas para a prova oral

Formato: pergunta, resposta curta de referencia, evidencia no repositorio.

## Requisitos e regras de negocio

1. Quais sao os 14 requisitos funcionais obrigatorios?
   Resposta: ver docs/rtm.md, cada RF esta numerado com teste e evidencia.

2. Como o sistema impede dupla reserva do mesmo recurso?
   Resposta: constraint de exclusao no PostgreSQL (btree_gist, tstzrange) criada em InicializadorBanco, mais lock pessimista em SalaRepository.findByIdParaAtualizar.
   Evidencia: ConcorrenciaTest, SobreposicaoRecursosTest, docs/adr/ADR-002.md.

3. Como e detectada sobreposicao na agenda do professor e do material?
   Resposta: mesmas constraints de exclusao cobrem sala, professor e material.
   Evidencia: SobreposicaoRecursosTest.

4. Como funciona a maquina de estados da reserva?
   Resposta: SOLICITADA, APROVADA, EM_USO, CONCLUIDA. Alternativos: REJEITADA, CANCELADA, NAO_COMPARECEU. Mapa TRANSICOES em ReservaService.
   Evidencia: MaquinaEstadosTest.

5. Reserva em uso pode ser cancelada?
   Resposta: nao. Retorna 409.
   Evidencia: AprovacaoTest.cancelarReservaEmUsoRetorna409; a mutacao no mapa de transicoes do ReservaService faz esse teste falhar.

6. Quem aprova recurso restrito?
   Resposta: apenas RESPONSAVEL e ADMINISTRADOR.
   Evidencia: AprovacaoTest, docs/seguranca/matriz-autorizacao.md.

## Autorizacao e seguranca

7. Um solicitante consegue alterar a reserva de outro solicitante?
   Resposta: nao, retorna 403. Regra de posse no ReservaController.
   Evidencia: PosseReservaTest.

8. Como estao configuradas as rotas no Spring Security?
   Resposta: ordem importa. Especificas antes das genericas. JwtFiltro sem @Component para evitar dupla inscricao.
   Evidencia: SecurityConfig, docs/adr/ADR-001.md.

9. Como erros de validacao chegam ao usuario?
   Resposta: TratadorGlobal converte em 400 com mensagem legivel, sem stack trace.
   Evidencia: ValidacaoEntradaTest.

## Testes

10. Qual a estrategia de testes?
    Resposta: JUnit 5, parametrizados, integracao com Testcontainers, API caixa-preta com MockMvc, E2E, concorrencia.
    Evidencia: docs/testes/plano.md.

11. Como a suite prova protecao contra dupla reserva?
    Resposta: teste de concorrencia dispara N solicitacoes simultaneas e verifica que apenas uma e aceita.
    Evidencia: ConcorrenciaTest.

12. O que e o teste de mutacao manual?
    Resposta: quebrar a regra de proposito e verificar se o teste falha. Registro em docs/testes/mutacao-manual.md.

13. Como o WireMock entra?
    Resposta: simula o provedor de notificacao externo, testa falha sem derrubar o fluxo.
    Evidencia: NotificacaoTest.

## CI, cobertura e analise estatica

14. Quais as metas de cobertura e qual o estado atual?
    Resposta: meta 80% linha e 70% branch, com check ativo no pom. Em 2026-11-10, o JaCoCo local mediu 92,8% de linhas (655/706) e 77,1% de branches (135/175). O SonarCloud nao foi reexecutado nesta branch.
    Evidencia: target/site/jacoco/index.html, SonarCloud samsreis_tads-organizacao-recursos.

15. O que o workflow faz?
    Resposta: build, testes, cobertura e analise Sonar com qualitygate.wait=true a cada pull request, push na main e push de tag v*. .github/workflows/ci.yml.

15a. O SonarCloud esta integrado?
    Resposta: a analise de referencia da v1.3.2 passou no Quality Gate; o workflow atual executa SonarCloud na integracao continua.
    Evidencia: https://sonarcloud.io/project/overview?id=samsreis_tads-organizacao-recursos.

## Performance

16. Qual o resultado mais recente do teste de carga?
    Resposta: no Codespace, login p95 209,05 ms, salas 17,45 ms e reserva 39,90 ms; uma resposta 201, 249 conflitos esperados 409 e zero respostas 500.
    Evidencia: docs/performance/resultado-02.md. A rodada anterior esta em docs/performance/resultado-01.md.

17. O resultado de carga comprova desempenho em producao?
    Resposta: nao. Foi uma unica rodada local, com ambiente compartilhado; nao substitui medicao em ambiente de producao.

## Rastreabilidade e qualidade

18. Onde vejo a ligacao requisito, risco, teste e evidencia?
    Resposta: docs/rtm.md.

19. Qual a decisao de qualidade do projeto?
    Resposta: Samuel Silva dos Reis decidiu liberar v1.3.9 com ressalvas, conforme docs/decisao-qualidade.md.

20. O que ficou de fora?
    Resposta: interface simples, sem animacao. Carga unica em ambiente local. Sem revisao por pares, pois o trabalho foi individual e nao havia equipe vizinha disponivel.

21. Qual a tag final de entrega?
    Resposta: v1.3.9 e a versao candidata local; a tag v1.3.9 foi publicada com CI verde.

22. Como se prova que um teste protege a regra?
    Resposta: mutacao manual. Quebra-se a regra, executa-se o teste e confirma-se o vermelho; depois restaura-se o codigo. Onze regras registradas, tres provas reexecutadas em 2026-11-09 e 2026-11-10.
    Evidencia: docs/testes/mutacao-manual.md.
