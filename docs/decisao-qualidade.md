# Decisao de qualidade

Data: 2026-11-10.
Decisao: liberar com ressalvas apos o CI remoto.

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
| 2 | `./mvnw -B verify`: BUILD SUCCESS local. CI remoto nao executado: ambiente sem autenticacao GitHub. | parcial |
| 3 | AutorizacaoPorPerfilTest, Matriz de autorizacao | atende |
| 4 | ConcorrenciaTest, ADR-001, ADR-004 | atende |
| 5 | 93,6% linha (663/708) e 78,3% branch (137/175), JaCoCo local | atende |
| 6 | docs/rtm.md | atende |
| 7 | docs/defeitos/ sem item critico aberto | atende |
| 8 | README e matriz | atende |

## Riscos aceitos

| Risco | Impacto | Justificativa | Responsavel |
|---|---|---|---|
| Medicoes de carga feitas em uma unica rodada no Codespace | baixo | Ambiente local compartilhado; resultado indicativo, nao representa producao | Samuel Silva dos Reis |
| Revisao por pares nao realizada | medio | Trabalho individual, sem equipe vizinha disponivel; revisao formal impossivel. Compensado com teste de mutacao manual e matriz de autorizacao | Samuel Silva dos Reis |

## Declaracao de cada integrante

- Samuel Silva dos Reis: execucao do projeto, testes automatizados, JMeter, documentacao e release.

## Conclusao

A decisao local e liberar v1.4.2 com ressalvas apos CI remoto: verify passou, e a rodada JMeter de 2026-11-10 atendeu os p95 definidos. A tag v1.4.2 foi publicada com CI verde em https://github.com/samsreis/tads-organizacao-recursos/actions?query=branch%3Av1.4.2. A carga foi feita uma unica vez em ambiente local e nao comprova desempenho em producao.
Analise atual do SonarCloud no SHA ea9bc64: https://sonarcloud.io/project/overview?id=samsreis_tads-organizacao-recursos. Quality Gate Passed, 89,7% de cobertura, zero issues.
