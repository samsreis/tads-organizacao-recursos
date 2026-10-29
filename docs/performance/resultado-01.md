# Resultado do teste de carga - Rodada 01

Data: 2026-10-29
Ambiente: notebook Linux Mint, aplicacao local Spring Boot, banco Postgres em Docker, JMeter 5.6.3 CLI.
Plano: perf/reservas.jmx
CSV: perf/usuarios.csv
Arquivo bruto: resultados/rodada-01.jtl (nao versionado)

## Carga aplicada

- 50 usuarios simultaneos, ramp-up 10 s, 5 iteracoes por usuario
- Mistura: POST /auth/login, GET /salas, POST /reservas
- Recurso disputado: mesma sala e mesmo horario
- Total de amostras: 750
- Vazao media: 68.2 amostras/s

## Resultados por operacao

| Operacao | Amostras | p50 ms | p95 ms | p99 ms | Codigos |
|---|---|---|---|---|---|
| POST login | 250 | 340 | 484 | 558 | 200:250 |
| GET salas | 250 | 6 | 39 | 88 | 200:250 |
| POST reserva | 250 | 20 | 68 | 119 | 201:1, 409:249 |

## Interpretacao

- POST reserva retornou 1 resposta 201 e 249 respostas 409.
- O 409 e esperado: 50 usuarios disputando o mesmo horario, o banco protege a sobreposicao.
- Zero respostas 500, zero stack trace exposto.
- A meta de exatamente 1 reserva aceita foi atingida.

## Metas

| Meta | Valor | Resultado |
|---|---|---|
| Login p95 <= 300 ms | 300 | 484 ms | nao atende |
| Salas p95 <= 300 ms | 300 | 39 ms | atende |
| Reserva p95 <= 500 ms | 500 | 68 ms | atende |
| Exatamente 1 reserva aceita | 1 | 1 | atende |
| Zero resposta 500 | 0 | 0 | atende |

## Hipotese de gargalo

- O p95 de reserva inclui o tempo de lock pessimista. Com 50 threads disputando o mesmo horario, a fila no lock explica o p99.
- Proximo experimento: medir novamente com horarios distintos para separar custo do lock do custo geral.

## Limites da conclusao

- Ambiente local. Nao prova comportamento em producao.
- Uma unica rodada.
- Notebook com 8 GB compartilhando CPU com banco, app e JMeter.
