# Decisao de qualidade

Data: 2026-10-29.
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
| 2 | a ser executado no primeiro push | pendente ate o push |
| 3 | AutorizacaoPorPerfilTest, Matriz de autorizacao | atende |
| 4 | ConcorrenciaTest, ADR-001, ADR-004 | atende |
| 5 | 92,70% linha e 75,82% branch | atende |
| 6 | docs/rtm.md | atende |
| 7 | docs/defeitos/ sem item critico aberto | atende |
| 8 | README e matriz | atende |

## Riscos aceitos

| Risco | Impacto | Justificativa | Responsavel |
|---|---|---|---|
| Login com p95 acima do alvo em carga simultanea | baixo | ambiente local, BCrypt e ajustavel em producao | grupo |
| Interface HTML parcial | baixo | nao e limitador de nota | grupo |
| Filtro de sala por capacidade e localizacao incompleto | baixo | nao e limitador de nota | grupo |
| Pipeline ainda nao executou no GitHub | medio | sera executado no primeiro push | grupo |
| Uma unica rodada de carga | baixo | tempo limitado, resultado registrado como indicativo | grupo |

## Declaracao de cada integrante

- Vitoria: execucao do projeto, testes automatizados, JMeter, documentacao e release.

## Conclusao

A decisao e liberar com ressalvas. O sistema atende o nucleo critico. Os riscos aceitos tem impacto baixo ou ja tem plano de correcao. Nao e correto afirmar que esta pronto para producao: o pipeline ainda nao executou no GitHub, a carga e local e existem duas pendencias funcionais parciais.
