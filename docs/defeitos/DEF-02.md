# Defeito 02: erro de validacao devolvia corpo generico sem mensagem util

## Identificacao
- ID: DEF-02
- Severidade: media
- Prioridade: P1
- Encontrado em: revisao da Aula 12
- Corrigido em: commit 420677d

## Recurso observavel
Qualquer endpoint que recebe JSON invalido ou falha de validacao.

## Ambiente
- branch main
- Spring Boot 3.5.16
- Java 21

## Pre-condicoes
Nenhuma.

## Passos minimos
1. Enviar POST /reservas com corpo vazio ou JSON malformado.
2. Observar o status e o corpo.

## Resultado obtido
HTTP 400 com corpo padrao do Spring contendo detalhes internos.

## Resultado esperado
HTTP 400 com corpo {"erro": "..."} em portugues, sem stack trace, sem nome de classe, sem SQL.

## Evidencia
ValidacaoEntradaTest falhou no commit 81daeb4 e passou no 420677d.

## Causa raiz
Sem um @RestControllerAdvice, o Spring devolve o corpo padrao do DefaultHandlerExceptionResolver e do MethodArgumentNotValidException.

## Correcao
Criado TratadorGlobal tratando MethodArgumentNotValidException e HttpMessageNotReadableException com corpo amigavel.

## Teste de regressao
ValidacaoEntradaTest cobre os principais cenarios de erro de entrada.

## Licao
Sem handler global, o professor que provoca um erro de validacao ve o detalhe interno. Isso e vazamento de informacao.

## Situacao
Fechado. Verificado no commit 420677d.
