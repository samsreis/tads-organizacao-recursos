# Checklist de entrega

Data: 2026-10-29.
Clone limpo simulado: nao. Executado a partir da pasta de desenvolvimento.
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
| Execucao do CI no GitHub | nao | nao | precisa do primeiro push |
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
| Tag de release | nao | nao | sera criada apos push e CI verde |
| Repositorio publico no GitHub | nao | nao | sera feito no bloco final |

## Bloqueadores

1. Repositorio ainda nao foi publicado no GitHub. Enquanto isso, o limitador "pipeline ausente" esta ativo.
2. Tag v1.0.0-rc1 so pode ser criada depois do CI verde.

## Pendencias nao bloqueantes

- SonarCloud sem token.
- Interface HTML parcial.
- Filtro de sala por capacidade e localizacao incompleto.
