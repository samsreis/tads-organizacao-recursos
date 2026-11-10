# Relatorio de testes

Data: 2026-11-10.

## Resumo

- Total: 147 execucoes de teste em 23 classes (Surefire).
- Taxa de sucesso local: 100%.
- Cobertura de linha: 92,8% (655 de 706 linhas; JaCoCo).
- Cobertura de branch: 77,1% (135 de 175 branches; JaCoCo).

## Distribuicao por classe

- AuthControllerTest: autenticacao.
- AutorizacaoPorPerfilTest: 18 cenarios parametrizados e 2 consultas autenticadas por ID.
- ValidacaoEntradaTest: 7 cenarios.
- RegrasReservaTest: 6.
- SobreposicaoTest: constraint no banco.
- SobreposicaoRecursosTest: 8.
- ConcorrenciaTest: 2 cenarios com 10 threads, um deles concorrendo pelo mesmo professor em salas distintas.
- PosseReservaTest: 2.
- AprovacaoTest: 7.
- AlteracaoReservaTest: 6.
- AuditoriaTest: 6.
- AuditoriaRollbackTest: 1 falha de gravacao simulada, com rollback verificado.
- RelatorioUtilizacaoTest: 4.
- ProfessorMaterialTest: 10.
- ProfessorMaterialCrudTest: 13.
- SalaControllerBranchTest: 4.
- SalaControllerTest: 3.
- CoberturaExtraTest: 7.
- MaquinaEstadosTest: 13.
- MovimentacaoMaterialTest: 10.
- NotificacaoTest: 4.

## Execucao

- Comando: `./mvnw -B verify`
- Ambiente: Ubuntu 24.04, Java 21.0.5, Docker com Testcontainers e Postgres 16.
- Tempo medio: 3 a 4 minutos.

## Observacoes

- As mensagens de violacao das constraints de exclusao durante ConcorrenciaTest sao esperadas; os testes confirmam uma reserva aceita e os demais pedidos em conflito, tanto para sala quanto para professor.
- O build local terminou com BUILD SUCCESS.
