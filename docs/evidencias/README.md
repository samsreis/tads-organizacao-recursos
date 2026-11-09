# Pacote de evidencias

Tag avaliada: v1.2.6. Repositorio: https://github.com/samsreis/tads-organizacao-recursos. Historico: v1.0.0-rc1, v1.0.0-rc2, v1.0.0, v1.1.0, v1.2.0 e v1.2.1.

## Indice

| Evidencia | Caminho |
|---|---|
| README de execucao | README.md |
| RTM | docs/rtm.md |
| Status dos requisitos | docs/status-requisitos.md |
| Matriz de autorizacao | docs/seguranca/matriz-autorizacao.md |
| Arquitetura C4 e sequencias | docs/arquitetura.md |
| Plano de testes | docs/testes/plano.md |
| Relatorio de testes | docs/testes/relatorio.md |
| Mutacao manual | docs/testes/mutacao-manual.md |
| Defeitos | docs/defeitos/ |
| Relatorio de qualidade | docs/relatorio-qualidade.md |
| Decisao de qualidade | docs/decisao-qualidade.md |
| Licoes aprendidas | docs/licoes-aprendidas.md |
| Revisao integrada | docs/revisao-integrada.md |
| ADRs | docs/adr/ |
| Plano de carga | docs/performance/plano.md |
| Resultado de carga | docs/performance/resultado-01.md |
| Checklist de entrega | docs/entrega/checklist.md |
| Pipeline | .github/workflows/ci.yml |
| Relatorio JaCoCo | target/site/jacoco/index.html gerado pelo build |

## Execucao do CI

Execucao verde da tag v1.2.6: https://github.com/samsreis/tads-organizacao-recursos/actions?query=branch%3Av1.2.6. Historico de tags anteriores preservado.

## Relatorio JaCoCo

Artefato publicado pelo workflow em cada execucao. Caminho local: target/site/jacoco/index.html.

## Quality Gate do SonarCloud

Pendente. Requer SONAR_TOKEN configurado nos secrets do repositorio. Se o token nao for configurado, sera registrado como pendencia justificada em docs/status-requisitos.md.

## Regra

Toda evidencia aponta para o commit da tag. Se algo precisar mudar depois da tag, sera criada uma nova versao, sem mover a tag anterior.


## SonarCloud

Quality Gate verde: [Ver painel no SonarCloud](https://sonarcloud.io/project/overview?id=samsreis_tads-organizacao-recursos)
