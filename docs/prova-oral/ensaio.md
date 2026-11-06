# Ensaio da prova oral

Roteiro de 8 minutos por integrante.

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
   - 142+ testes, Testcontainers, MockMvc, WireMock.
   - Cobertura 92,70% linha, 75,82% branch.
   - Workflow no GitHub Actions.

5. RTM e relatorio (1 min)
   - docs/rtm.md liga requisito a teste e evidencia.
   - docs/relatorio-qualidade.md com numeros reais.

6. Limitacoes (1 min)
   - Login acima da meta de p95.
   - SonarCloud nao integrado.
   - Interface minima.

## Pontuacao do ensaio

Preencher por integrante apos rodar o roteiro cronometrado.

| Integrante | Tempo | Pontos fortes | Lacunas |
|------------|-------|---------------|---------|
| Samuel Silva | 8 min | Explicou o fluxo de CI e o TDD da autorizacao. | Faltou citar o ADR do lock pessimista e as constraints do banco. |

## Lacunas mais frequentes

1. Esquecer os numeros exatos de cobertura (92,70% e 75,82%).
2. Nao explicar a diferenca entre WireMock e Testcontainers.
3. Nao citar a constraint de exclusao do banco de dados.

## Plano de estudo ate a Semana 47

1. Ler o docs/rtm.md em voz alta uma vez por dia.
2. Revisar o ADR da constraint de exclusao e do lock pessimista.
3. Ensaiar a demonstracao do frontend no Codespaces com cronometro.
