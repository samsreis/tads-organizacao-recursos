# Checklist de entrega

Data: 2026-11-10.
Clone limpo e execucao de 2026-10-30 sao evidencias historicas.
Reverificacao local desta branch: `./mvnw -B verify`, 147 execucoes, zero falhas/erros, BUILD SUCCESS; JaCoCo 92,8% linha e 77,1% branch.
JMeter: rodada local em 2026-11-10, detalhada em docs/performance/resultado-02.md.

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
| Execucao do CI no GitHub | nao executada | Requer autenticacao GitHub | https://github.com/samsreis/tads-organizacao-recursos/actions |
| JaCoCo | sim | sim | pom.xml e target/site/jacoco |
| SonarCloud | integrado em revisao anterior | sem nova analise nesta branch | https://sonarcloud.io/project/overview?id=samsreis_tads-organizacao-recursos |
| Plano de carga | sim | sim | docs/performance/plano.md e perf/reservas.jmx |
| Resultado de carga | sim | sim | docs/performance/resultado-02.md (atual) e resultado-01.md (historico) |
| Defeitos registrados | sim | sim | docs/defeitos/ |
| Relatorio de qualidade | sim | sim | docs/relatorio-qualidade.md |
| Decisao de qualidade | sim | sim | docs/decisao-qualidade.md |
| ADRs | sim | sim | docs/adr/ADR-001 a ADR-004 |
| Matriz de autorizacao | sim | sim | docs/seguranca/matriz-autorizacao.md |
| Licoes aprendidas | sim | sim | docs/licoes-aprendidas.md |
| Revisao integrada | sim | sim | docs/revisao-integrada.md |
| Tag de release | v1.3.2 e anteriores | v1.3.4 preparada localmente; tag nao enviada | docs/evidencias/README.md |
| Repositorio publico no GitHub | sim | sim | https://github.com/samsreis/tads-organizacao-recursos |

## Publicacao remota nao executada

O ambiente nao tinha credencial GitHub valida para criar PR, fazer push, merge ou publicar a tag v1.3.4.

## Limites aceitos

- Interface HTML implementada com login, filtro de sala, reserva e aprovacao, mas sem animacao por escolha de design.
