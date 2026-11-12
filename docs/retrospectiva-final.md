# Retrospectiva - revisao de auditoria

Data: 2026-11-10.

## Resultado

- `./mvnw -B verify`: BUILD SUCCESS, 147 execucoes, sem falhas ou erros.
- JaCoCo: 93,6% de linhas e 78,3% de branches.
- O teste de mutacao identificou a tentativa de cancelar reserva em uso ao receber 204 em vez de 409; a mutacao temporaria foi removida.
- A rodada JMeter corrigida aplicou 550 amostras, com um 201 e 249 conflitos 409 esperados, sem falhas de assercao nem respostas 500.

## O que funcionou

- O teste de cancelamento estabelece o estado real `EM_USO` antes de tentar excluir e protege a regra no servico.
- A concorrencia de professor usa salas distintas, isolando a constraint do professor da constraint da sala.
- O teste do relatorio confere o contador numericamente, e o teste de auditoria simula uma falha real antes de verificar rollback.
- A revisão dos resultados JMeter encontrou dois problemas de configuracao na preparacao da medicao: o caminho antigo do CSV e o login repetido em todas as iteracoes. A rodada registrada foi executada depois de corrigir o perfil e confirmar a quantidade de logins.

## A melhorar

- Reexecutar a carga em ambiente representativo e com mais de uma rodada antes de assumir os valores como garantia operacional.
- A verificacao local da candidata v1.4.3 foi concluida; a tag v1.4.3 foi publicada com CI verde em https://github.com/samsreis/tads-organizacao-recursos/actions?query=branch%3Av1.4.3.

## Metricas semanais

Os dados abaixo sao o retrato semanal disponivel no relatorio de qualidade, consolidado em 2026-11-10; nao ha serie historica semanal comparavel para inferir tendencia.

| Semana | Testes | Cobertura de linhas | Cobertura de branches | Login p95 | Salas p95 | Reserva p95 | Falhas HTTP 500 |
|---|---:|---:|---:|---:|---:|---:|---:|
| 2026-11-09 a 2026-11-15 (medicao em 10/11) | 147 | 93,6% (663/708) | 78,3% (137/175) | 209,05 ms | 17,45 ms | 39,90 ms | 0 |

## Divida tecnica remanescente

- Interface funcional sem teste automatizado de UI; RF-13 permanece parcial.
- Carga medida em uma unica rodada local, sem ambiente representativo de producao.
- Historico de cobertura e performance nao possui serie semanal para comparacao de tendencia.

## Plano de tres incrementos

1. Acrescentar testes automatizados de UI para login, filtro, reserva e aprovacao.
2. Executar multiplas rodadas de carga em ambiente representativo e comparar percentis.
3. Automatizar a consolidacao semanal de cobertura, estabilidade e performance no relatorio de qualidade.

## Decisao

Os resultados locais atendem aos criterios automatizados e metas de carga definidos para esta revisao. A versao candidata e v1.4.3; A tag v1.4.3 foi publicada com CI verde em https://github.com/samsreis/tads-organizacao-recursos/actions?query=branch%3Av1.4.3.
