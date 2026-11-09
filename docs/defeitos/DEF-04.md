# Defeito 04: mensagem antiga permanecia na tela ao trocar de usuario

## Identificacao
- ID: DEF-04
- Severidade: baixa
- Prioridade: P3
- Encontrado em: teste manual da interface, 2026-11-09
- Corrigido em: branch fix/frontend-estados-v1.3.2

## Recurso observavel
Interface web, src/main/resources/static/app.js.

## Pre-condicoes
Estar logado e ter solicitado uma reserva.

## Passos minimos
1. Entrar como SOLICITANTE e solicitar uma reserva.
2. Clicar em Sair.
3. Entrar como RESPONSAVEL.
4. Observar a area de mensagens.

## Resultado obtido
A mensagem "Reserva solicitada" permanecia visivel na tela do novo usuario.

## Resultado esperado
Toda mensagem e limpa ao trocar de tela ou de usuario.

## Causa raiz
trocarTela nao limpava os elementos de mensagem.

## Correcao
trocarTela limpa msgLogin, msgSalas, msgMinhas, msgReserva e msgAprovar a cada navegacao.

## Teste de regressao
Verificacao manual na interface e node --check no app.js. Sem teste automatizado de frontend, registrado como limitacao.

## Situacao
Fechado.
