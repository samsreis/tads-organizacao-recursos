# Checklist de entrega

Data: 2026-10-30.
Clone limpo: sim, executado em /tmp/clone-teste a partir do GitHub em 2026-10-30. 143 testes, cobertura ok, BUILD SUCCESS.
Aplicacao executa: sim, ./mvnw spring-boot:run sobe em 8 segundos.
Build verde: sim, ./mvnw -B verify com EXIT 0.

| Item | Existe | Atualizado | Caminho |
|---|---|---|---|
| Repositorio com historico | sim | sim | git log |
| Aplicacao executavel | sim | sim | src/main/java |
| README | sim | sim | README.md |
| Documentacao de API | sim | sim | README.md secao Endpoints e docs/seguranca/matriz-autorizacao.md |
| RTM | sim | sim | docs/rtm.md |
| Status dos requisitos | sim | sim | docs/status-requisitos.md |
| Diagramas C4 e sequencias | sim | sim | docs/arquitetura.md |
| Plano de testes | sim | sim | docs/testes/plano.md |
| Relatorio de testes | sim | sim | docs/testes/relatorio.md |
| Mutacao manual | sim | sim | docs/testes/mutacao-manual.md |
| Pipeline CI | sim, arquivo presente | sim | .github/workflows/ci.yml |
| Execucao do CI no GitHub | sim | sim | https://github.com/samsreis/tads-organizacao-recursos/actions/runs/41076861945 |
| JaCoCo | sim | sim | pom.xml e target/site/jacoco |
| SonarCloud | nao | nao | pendente, requer SONAR_TOKEN |
| Plano de carga | sim | sim | docs/performance/plano.md e perf/reservas.jmx |
| Resultado de carga | sim | sim | docs/performance/resultado-01.md |
| Defeitos registrados | sim | sim | docs/defeitos/ |
| Relatorio de qualidade | sim | sim | docs/relatorio-qualidade.md |
| Decisao de qualidade | sim | sim | docs/decisao-qualidade.md |
| ADRs | sim | sim | docs/adr/ADR-001 a ADR-004 |
| Matriz de autorizacao | sim | sim | docs/seguranca/matriz-autorizacao.md |
| Licoes aprendidas | sim | sim | docs/licoes-aprendidas.md |
| Revisao integrada | sim | sim | docs/revisao-integrada.md |
| Tag de release | sim | sim | v1.0.0-rc2 no commit 2b5d9e7 |
| Repositorio publico no GitHub | sim | sim | https://github.com/samsreis/tads-organizacao-recursos |

## Bloqueadores

Nenhum. Repositorio publicado, CI verde e tag v1.0.0-rc2 criada no commit da execucao verde.

## Pendencias nao bloqueantes

- SonarCloud sem token.
- Interface HTML implementada com login, filtro de sala, reserva e aprovacao, mas sem animacao por escolha de design.
