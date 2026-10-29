# Relatorio de testes

Data: 2026-10-29.

## Resumo

- Total: mais de 130 metodos.
- Taxa de sucesso local: 100%.
- Cobertura de linha: 92,70%.
- Cobertura de branch: 75,82%.

## Distribuicao por classe

- AuthControllerTest: autenticacao.
- AutorizacaoPorPerfilTest: 18 cenarios parametrizados.
- ValidacaoEntradaTest: 7 cenarios.
- RegrasReservaTest: 6.
- SobreposicaoTest: constraint no banco.
- SobreposicaoRecursosTest: 8.
- ConcorrenciaTest: 1 cenario com 10 threads.
- PosseReservaTest: 2.
- AprovacaoTest: 7.
- AlteracaoReservaTest: 6.
- AuditoriaTest: 7.
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

- Comando: ./mvnw -B verify
- Ambiente: Linux Mint, Java 21, Docker com Testcontainers.
- Tempo medio: 3 a 4 minutos.

## Observacoes

- As linhas de erro no log durante a execucao do ConcorrenciaTest sao esperadas: a constraint de exclusao rejeita as reservas concorrentes. O teste comprova que apenas uma reserva e aceita.
