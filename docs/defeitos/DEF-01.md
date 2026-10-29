# Defeito 01: falta de token retornava 403 em vez de 401

## Identificacao
- ID: DEF-01
- Severidade: media
- Prioridade: P1
- Encontrado em: revisao da Aula 12
- Corrigido em: commit c0a8bcc

## Recurso observavel
POST /reservas, POST /salas, e outros endpoints protegidos.

## Ambiente
- branch main
- Spring Boot 3.5.16
- Java 21
- Postgres 16 em Testcontainers

## Pre-condicoes
Nenhuma. Basta nao enviar header Authorization.

## Passos minimos
1. Enviar POST /reservas sem header Authorization.
2. Observar o status.

## Resultado obtido
HTTP 403 com corpo padrao do Spring Security.

## Resultado esperado
HTTP 401, porque o cliente nao esta autenticado. 403 e para cliente autenticado sem permissao.

## Evidencia
Teste AutorizacaoPorPerfilTest.semTokenRecebe401 falhou no commit 81daeb4 e passou no c0a8bcc.

## Causa raiz
Configuracao padrao do Spring Security retorna 403 quando o usuario anonimo tenta acessar recurso protegido. Faltava um authenticationEntryPoint configurado.

## Correcao
Configurado HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED) no SecurityConfig.

## Teste de regressao
AutorizacaoPorPerfilTest.semTokenRecebe401.

## Licao
Sem entry point explicito, o comportamento padrao nao distingue "nao autenticado" de "sem permissao".

## Situacao
Fechado. Verificado no commit c0a8bcc.
