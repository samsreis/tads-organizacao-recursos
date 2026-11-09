# Checklist de entrega

Data: 2026-10-30.
Clone limpo: sim, executado em /tmp/clone-teste a partir do GitHub em 2026-10-30. 144 testes, cobertura acima das metas, BUILD SUCCESS.
Aplicacao executa: sim, ./mvnw spring-boot:run sobe em 8 segundos.
Build verde: sim, ./mvnw -B verify com EXIT 0.
Reverificacao: 2026-11-09, apos correcao do DEF-03. 144 testes, cobertura acima das metas, BUILD SUCCESS.

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
| Execucao do CI no GitHub | sim | sim | https://github.com/samsreis/tads-organizacao-recursos/actions/runs/42165085123 |
| JaCoCo | sim | sim | pom.xml e target/site/jacoco |
| SonarCloud | sim | sim | https://sonarcloud.io/project/overview?id=samsreis_tads-organizacao-recursos |
| Plano de carga | sim | sim | docs/performance/plano.md e perf/reservas.jmx |
| Resultado de carga | sim | sim | docs/performance/resultado-01.md |
| Defeitos registrados | sim | sim | docs/defeitos/ |
| Relatorio de qualidade | sim | sim | docs/relatorio-qualidade.md |
| Decisao de qualidade | sim | sim | docs/decisao-qualidade.md |
| ADRs | sim | sim | docs/adr/ADR-001 a ADR-004 |
| Matriz de autorizacao | sim | sim | docs/seguranca/matriz-autorizacao.md |
| Licoes aprendidas | sim | sim | docs/licoes-aprendidas.md |
| Revisao integrada | sim | sim | docs/revisao-integrada.md |
| Tag de release | sim | sim | v1.3.2 no commit final da main. Historico: v1.0.0-rc1, v1.0.0-rc2, v1.0.0, v1.1.0, v1.2.0, v1.2.1, v1.2.2, v1.2.3, v1.2.4, v1.2.5, v1.2.6, v1.2.7, v1.2.8 e v1.3.2. Execucao: https://github.com/samsreis/tads-organizacao-recursos/actions?query=branch%3Av1.3.2 |
| Repositorio publico no GitHub | sim | sim | https://github.com/samsreis/tads-organizacao-recursos |

## Bloqueadores

Nenhum. Repositorio publicado, CI verde. Tag final v1.3.2, CI verde na execucao da tag.

## Pendencias nao bloqueantes

- Interface HTML implementada com login, filtro de sala, reserva e aprovacao, mas sem animacao por escolha de design.
