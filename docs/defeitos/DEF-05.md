# Defeito 05: lista de aprovacao mostrava reservas ja finalizadas

## Identificacao
- ID: DEF-05
- Severidade: media
- Prioridade: P2
- Encontrado em: teste manual da interface, 2026-11-09
- Corrigido em: branch fix/frontend-estados-v1.3.2

## Recurso observavel
Tela Aprovar da interface web.

## Pre-condicoes
Existir reserva APROVADA e reserva CONCLUIDA.

## Passos minimos
1. Entrar como RESPONSAVEL.
2. Clicar em Aprovar.

## Resultado obtido
A lista mostrava reservas APROVADA e CONCLUIDA junto das SOLICITADA.

## Resultado esperado
A lista mostra apenas reservas com transicao possivel.

## Causa raiz
carregarAprovar chamava GET /reservas e exibia todas, sem filtrar por estado.

## Correcao
Filtro por estado que tem acao valida: SOLICITADA, APROVADA e EM_USO.

## Teste de regressao
Verificacao manual na interface e node --check no app.js.

## Situacao
Fechado.
