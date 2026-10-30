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

5. Reserva ja iniciada pode ser cancelada?
   Resposta: nao. Retorna 409.
   Evidencia: AprovacaoTest.cancelarReservaIiniciadaRetorna409.

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

14. Quais as metas de cobertura?
    Resposta: 80% linha, 70% branch. Check ativo no pom.
    Estado atual: 92,70% linha, 75,82% branch.

15. O que o workflow faz?
    Resposta: build, testes, cobertura a cada pull request. .github/workflows/ci.yml.

## Performance

16. Qual o resultado do teste de carga?
    Resposta: login p95 484 ms (nao atende meta de 300), salas p95 39 ms, reserva p95 68 ms. 0 erro 500, 1 aceita, 249 conflitos 409.
    Evidencia: docs/performance/resultado-01.md.

17. Por que o login ficou acima da meta?
    Resposta: BCrypt e o custo de hash. Hipotese registrada com dado que a sustenta.

## Rastreabilidade e qualidade

18. Onde vejo a ligacao requisito, risco, teste e evidencia?
    Resposta: docs/rtm.md.

19. Qual a decisao de qualidade do grupo?
    Resposta: ver docs/decisao-qualidade.md.

20. O que ficou de fora?
    Resposta: SonarCloud (pendencia justificada), interface simples, sem animacao.
