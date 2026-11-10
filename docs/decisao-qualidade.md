# Decisao de qualidade

Data: 2026-11-10.
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
| 2 | `./mvnw -B verify`: BUILD SUCCESS, 145 testes locais. CI remoto e tag v1.3.3 pendentes. | atende localmente; CI pendente |
| 3 | AutorizacaoPorPerfilTest, Matriz de autorizacao | atende |
| 4 | ConcorrenciaTest, ADR-001, ADR-004 | atende |
| 5 | 92,8% linha (655/706) e 77,1% branch (135/175), JaCoCo local | atende |
| 6 | docs/rtm.md | atende |
| 7 | docs/defeitos/ sem item critico aberto | atende |
| 8 | README e matriz | atende |

## Riscos aceitos

| Risco | Impacto | Justificativa | Responsavel |
|---|---|---|---|
| Medicoes de carga feitas em uma unica rodada no Codespace | baixo | Ambiente local compartilhado; resultado indicativo, nao representa producao | grupo |
| Revisao por pares nao realizada | medio | grupo de um unico integrante, sem par disponivel; revisao formal impossivel. Compensado com teste de mutacao manual e matriz de autorizacao | grupo |

## Declaracao de cada integrante

- Samuel Silva dos Reis: execucao do projeto, testes automatizados, JMeter, documentacao e release.

## Conclusao

A decisao local e liberar com ressalvas: verify passou com 145 execucoes, e a rodada JMeter de 2026-11-10 atendeu os p95 definidos. Isso nao equivale a aprovacao de release: o CI remoto da branch, o merge e a tag v1.3.3 ainda estao pendentes. A carga foi feita uma unica vez em ambiente local e nao comprova desempenho em producao.
Historico do SonarCloud na v1.3.2: https://sonarcloud.io/project/overview?id=samsreis_tads-organizacao-recursos. A branch atual aguarda nova analise remota.
