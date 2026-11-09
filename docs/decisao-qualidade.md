# Decisao de qualidade

Data: 2026-10-30.
Decisao: liberar com ressalvas.

## Criterios usados

1. Aplicacao executa a partir do README em clone limpo.
2. Pipeline de CI verde no commit da tag.
3. Nenhuma falha de autorizacao nos testes da matriz.
4. Nenhuma possibilidade de dupla reserva nos testes.
5. Cobertura de linha >= 80% e branch >= 70%.
6. 100% dos requisitos criticos com evidencia na RTM.
7. Zero bug critico aberto.
8. Documentacao de API disponivel.

## Evidencia por criterio

| Criterio | Evidencia | Situacao |
|---|---|---|
| 1 | README.md com passo a passo | atende |
| 2 | https://github.com/samsreis/tads-organizacao-recursos/actions/runs/42669345357. Tag final v1.2.7 no commit 01eac53, execucao 42669345357. | atende |
| 3 | AutorizacaoPorPerfilTest, Matriz de autorizacao | atende |
| 4 | ConcorrenciaTest, ADR-001, ADR-004 | atende |
| 5 | 92,70% linha e 77,1% branch | atende |
| 6 | docs/rtm.md | atende |
| 7 | docs/defeitos/ sem item critico aberto | atende |
| 8 | README e matriz | atende |

## Riscos aceitos

| Risco | Impacto | Justificativa | Responsavel |
|---|---|---|---|
| Login com p95 acima do alvo em carga simultanea | baixo | ambiente local, BCrypt e ajustavel em producao | grupo |
| Uma unica rodada de carga | baixo | tempo limitado, resultado registrado como indicativo | grupo |
| Revisao por pares nao realizada | medio | grupo de um unico integrante, sem par disponivel; revisao formal impossivel. Compensado com teste de mutacao manual e matriz de autorizacao | grupo |

## Declaracao de cada integrante

- Vitoria: execucao do projeto, testes automatizados, JMeter, documentacao e release.

## Conclusao

A decisao e liberar com ressalvas. O sistema atende o nucleo critico, o CI esta verde na main e os 14 requisitos funcionais estao cobertos. Nao e correto afirmar que esta pronto para producao: a carga foi feita em ambiente local, o login ficou acima do alvo de p95. O SonarCloud esta integrado, com Quality Gate verde.
Quality Gate do SonarCloud: https://sonarcloud.io/project/overview?id=samsreis_tads-organizacao-recursos
