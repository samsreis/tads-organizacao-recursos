# Defeito 06: botoes de acao ignoravam a maquina de estados

## Identificacao
- ID: DEF-06
- Severidade: media
- Prioridade: P2
- Encontrado em: teste manual da interface, 2026-11-09
- Corrigido em: branch fix/frontend-estados-v1.3.2

## Recurso observavel
Tela Aprovar da interface web.

## Pre-condicoes
Existir reserva em qualquer estado.

## Passos minimos
1. Entrar como RESPONSAVEL.
2. Clicar em Aprovar.
3. Observar os botoes ao lado de uma reserva APROVADA.

## Resultado obtido
Os quatro botoes aprovar, rejeitar, iniciar e concluir apareciam em todos os estados. Acoes invalidas retornavam 409 no backend.

## Resultado esperado
Cada estado oferece somente acoes validas.

## Causa raiz
Lista fixa de quatro acoes no frontend, sem consultar o estado da reserva.

## Correcao
Mapa acoesPorEstado no frontend: SOLICITADA permite aprovar e rejeitar, APROVADA permite iniciar, EM_USO permite concluir.

## Teste de regressao
Verificacao manual na interface e node --check no app.js. O backend continua bloqueando por MaquinaEstadosTest.

## Situacao
Fechado.
