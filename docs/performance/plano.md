# Plano de desempenho - Sistema de Organizacao de Recursos

Status: executado em 2026-10-29, resultado em docs/performance/resultado-01.md.
Data: 2026-10-29.
Ambiente: notebook Linux Mint, aplicacao local, banco Postgres em container.

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
- Duracao: 5 iteracoes por usuario
- Mistura:
  - POST /auth/login (1x por usuario no inicio)
  - GET /salas (consulta, 3x por iteracao)
  - POST /reservas (criacao, 1x por iteracao)
- Recursos disputados: mesma sala e mesmo horario para todos, forçando o conflito.

## Fora de escopo

- Teste em ambiente de producao
- Teste de carga com dados reais
- Teste de estresse alem de 50 usuarios

## Riscos

- Notebook com pouca memoria pode distorcer o resultado.
- Resultado local nao prova comportamento em producao.
- 409 esperado precisa ser separado de erro real na leitura.
