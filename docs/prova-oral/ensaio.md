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
|            |       |               |         |

## Lacunas mais frequentes

(preencher apos o ensaio)

## Plano de estudo ate a Semana 47

(preencher apos o ensaio)
