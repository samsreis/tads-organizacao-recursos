# Resultado do teste de carga - Rodada 02

Data: 2026-11-10.
Ambiente: Codespace Ubuntu 24.04, Java 21.0.5, JMeter 5.6.3, aplicacao local e Postgres 16 em container isolado.
Plano: perf/reservas.jmx.
CSV: perf/usuarios.csv.

O arquivo .jtl bruto não é versionado por boas práticas (é um artefato de build), mas as métricas resumidas estão neste documento e podem ser reproduzidas com perf/reservas.jmx.

## Carga aplicada

- 50 usuarios, ramp-up de 10 segundos e 5 iteracoes por usuario.
- Cada usuario fez login uma vez; em cada iteracao consultou salas e tentou reservar o mesmo horario.
- Amostras: 50 logins, 250 consultas e 250 criacoes, 550 no total.
- JMeter concluiu sem falhas de assercao. O plano trata HTTP 409 como resposta esperada somente depois de validar o status; nenhuma resposta HTTP 500 ocorreu.

## Resultados

| Operacao | Amostras | p50 ms | p90 ms | p95 ms | p99 ms | Max ms | Codigos HTTP |
|---|---:|---:|---:|---:|---:|---:|---|
| POST login | 50 | 134 | 180,40 | 209,05 | 228 | 228 | 200: 50 |
| GET salas | 250 | 5 | 11 | 17,45 | 25 | 27 | 200: 250 |
| POST reserva | 250 | 18 | 32 | 39,90 | 58,49 | 62 | 201: 1, 409: 249 |

Percentis e tempos extraidos de `statistics.json` do Dashboard JMeter 5.6.3.

## Metas

| Meta | Alvo | Resultado | Situacao |
|---|---:|---:|---|
| Login p95 | <= 300 ms | 209,05 ms | atende nesta rodada |
| Consulta p95 | <= 300 ms | 17,45 ms | atende nesta rodada |
| Reserva p95 | <= 500 ms | 39,90 ms | atende nesta rodada |
| Reservas aceitas para o mesmo horario | exatamente 1 | 1 resposta 201 e 249 respostas 409 | atende |
| Respostas HTTP 500 | 0 | 0 | atende |

## Limites

- Esta foi uma unica rodada local, sem usuarios ou dados de producao.
- Os resultados dependem dos recursos compartilhados do Codespace e nao sao diretamente comparaveis aos da rodada 01, executada em outro ambiente.
- A rodada valida o perfil funcional e as metas definidas, mas nao substitui um teste de carga em ambiente representativo de producao.

> **Nota de versionamento:** O arquivo `.jtl` bruto gerado pelo JMeter não é versionado neste repositório por boas práticas de engenharia (evitar versionar artefatos binários de build). Os resultados extraídos dele estão sumarizados nas tabelas acima.
