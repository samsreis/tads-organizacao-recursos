# Plano de desempenho - Sistema de Organizacao de Recursos

Status: executado em 2026-11-10. Resultado original em docs/performance/resultado-01.md; repeticao no Codespace em docs/performance/resultado-02.md.
Data do plano original: 2026-10-29.
Ambiente da repeticao: Ubuntu 24.04 no Codespace, Java 21.0.5, JMeter 5.6.3, aplicacao local e Postgres 16 em container isolado.

## Requisitos de desempenho

RNF-01 Consulta de disponibilidade
- 50 usuarios simultaneos
- p95 <= 300 ms
- taxa de erro real <= 1%

RNF-02 Criacao de reserva
- 50 usuarios simultaneos disputando o mesmo recurso e horario
- p95 <= 500 ms
- exatamente 1 resposta 201 por horario
- respostas 409 sao esperadas e nao contam como erro
- nenhuma resposta 500

## Modelo de carga

- Usuarios: 50
- Ramp-up: 10 segundos
- Duracao: 5 iteracoes de consulta e reserva por usuario; login uma vez por usuario.
- Mistura:
  - POST /auth/login (1x por usuario no inicio)
  - GET /salas (consulta, 1x por iteracao)
  - POST /reservas (criacao, 1x por iteracao)
- Recursos disputados: mesma sala e mesmo horario para todos, forçando o conflito.

## Reexecucao de 2026-11-10

- Comando: `jmeter -n -t perf/reservas.jmx -Jcsv=perf/usuarios.csv -Jhost=localhost -Jport=8080 -JsalaId=1 -l /tmp/resultado.jtl -e -o /tmp/resultado`.
- Total observado: 550 amostras: 50 logins, 250 consultas e 250 criacoes de reserva.
- Resultado: uma reserva criada (201), 249 conflitos esperados (409), sem outras respostas nem falhas de assercao.
- Valores medidos e avaliacao: docs/performance/resultado-02.md.

## Fora de escopo

- Teste em ambiente de producao
- Teste de carga com dados reais
- Teste de estresse alem de 50 usuarios

## Riscos

- Notebook com pouca memoria pode distorcer o resultado.
- Resultado local nao prova comportamento em producao.
- 409 esperado precisa ser separado de erro real na leitura.
