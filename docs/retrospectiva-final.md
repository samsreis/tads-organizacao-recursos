# Retrospectiva - revisao de auditoria

Data: 2026-11-10.

## Resultado

- `./mvnw -B verify`: BUILD SUCCESS, 145 execucoes, sem falhas ou erros.
- JaCoCo: 92,8% de linhas e 77,1% de branches.
- O teste de mutacao identificou a tentativa de cancelar reserva em uso ao receber 204 em vez de 409; a mutacao temporaria foi removida.
- A rodada JMeter corrigida aplicou 550 amostras, com um 201 e 249 conflitos 409 esperados, sem falhas de assercao nem respostas 500.

## O que funcionou

- O teste de cancelamento estabelece o estado real `EM_USO` antes de tentar excluir e protege a regra no servico.
- A concorrencia de professor usa salas distintas, isolando a constraint do professor da constraint da sala.
- O teste do relatorio confere o contador numericamente, e o teste de auditoria simula uma falha real antes de verificar rollback.
- A revisão dos resultados JMeter encontrou dois problemas de configuracao na preparacao da medicao: o caminho antigo do CSV e o login repetido em todas as iteracoes. A rodada registrada foi executada depois de corrigir o perfil e confirmar a quantidade de logins.

## A melhorar

- Reexecutar a carga em ambiente representativo e com mais de uma rodada antes de assumir os valores como garantia operacional.
- Confirmar CI, merge e tag v1.3.3 no GitHub antes de declarar a release concluida.

## Decisao

Os resultados locais atendem aos criterios automatizados e metas de carga definidos para esta revisao. A publicacao permanece condicionada ao CI remoto verde e ao merge.
