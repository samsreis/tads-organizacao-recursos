# Plano TDD - revisao de auditoria

Data: 2026-11-10.
Branch: `fix/auditoria-completa`.

## Ciclo vermelho-verde

1. Mutacao controlada: em `ReservaService.TRANSICOES`, permitir temporariamente `CANCELADA` a partir de `EM_USO`.
2. Executar `./mvnw -B -Dtest=AprovacaoTest#cancelarReservaEmUsoRetorna409 test`.
3. Resultado vermelho observado: 1 teste, 1 falha; HTTP esperado 409, recebido 204.
4. Restaurar imediatamente a regra correta. A mutacao foi registrada em docs/testes/mutacao-manual.md.
5. Executar o teste novamente junto dos testes de aprovacao; a classe passou no verify completo.

## Coberturas verificadas

- Concorrencia: dez reservas para o mesmo professor, em salas distintas e mesmo intervalo; uma aceita e nove conflitos.
- Relatorio: apos uma tentativa concorrente rejeitada, assertar `conflitosEvitados == 1`.
- Validacao: executar os sete cenarios da classe ValidacaoEntradaTest.
- Integracao externa: executar os quatro cenarios de NotificacaoTest com WireMock.
- Auditoria: simular falha ao persistir a auditoria e verificar rollback da reserva.

## Verificacao final local

- Comando: `./mvnw -B verify`.
- Resultado: BUILD SUCCESS, 145 execucoes, zero falhas, zero erros.
- JaCoCo: 92,8% de linhas e 77,1% de branches.
- CI remoto, merge e tag v1.3.3 ainda pendentes.
