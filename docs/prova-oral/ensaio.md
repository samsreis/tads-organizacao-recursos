# Ensaio individual da prova oral

Roteiro individual de 8 minutos.

## Estrutura do roteiro

1. Problema e risco (1 min)
   - Sistema aloca salas, professores e materiais sem conflito.
   - Riscos: dupla reserva, sobreposicao, autorizacao, manutencao.

2. Arquitetura (1 min 30)
   - Spring Boot 3.5.16, Java 21, PostgreSQL 16.
   - C4 em docs/arquitetura.md.
   - Constraints de exclusao no banco, lock pessimista, JWT.

3. Demonstracao (2 min)
   - Login, filtro de sala, criar reserva, aprovar, iniciar, concluir.
   - Interface em /index.html.

4. Testes e CI (1 min 30)
   - 145 execucoes de teste, Testcontainers, MockMvc, WireMock.
   - Cobertura local JaCoCo: 93,6% linha e 78,3% branch.
   - Verify local aprovado; PR e CI remotos dependem de autenticacao GitHub.

5. RTM e relatorio (1 min)
   - docs/rtm.md liga requisito a teste e evidencia.
   - docs/relatorio-qualidade.md com numeros reais.

6. Limitacoes (1 min)
   - JMeter no Codespace: login p95 209,05 ms, salas 17,45 ms e reserva 39,9 ms nesta unica rodada.
   - Carga unica em ambiente local.
   - Interface minima, sem animacao.

## Pontuacao do ensaio

Preencher por integrante apos rodar o roteiro cronometrado.

| Integrante | Tempo | Pontos fortes | Lacunas |
|------------|-------|---------------|---------|
| Samuel Silva dos Reis | 8 min | Explicou o fluxo de CI e o TDD da autorizacao. | Faltou citar o ADR do lock pessimista e as constraints do banco. |
| Samuel Silva dos Reis | nao cronometrado | Ensaio realizado em 2026-11-09 sobre o roteiro de 8 minutos, sem registro de tempo por bloco. | Sem lacuna declarada no ensaio. Plano de estudo mantido. |

## Lacunas mais frequentes

1. Esquecer os numeros exatos de cobertura (93,6% linha e 78,3% branch no JaCoCo local).
2. Nao explicar a diferenca entre WireMock e Testcontainers.
3. Nao citar a constraint de exclusao do banco de dados.

## Plano de estudo ate a Semana 47

1. Ler o docs/rtm.md em voz alta uma vez por dia.
2. Revisar o ADR da constraint de exclusao e do lock pessimista.
3. Ensaiar a demonstracao do frontend no Codespaces com cronometro.
4. Explicar que a v1.4.2 e a candidata validada localmente e que a tag v1.4.2 foi publicada com CI verde.
