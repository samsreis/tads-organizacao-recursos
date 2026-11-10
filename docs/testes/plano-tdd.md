# Plano TDD - revisao de auditoria

Data: 2026-11-10.
Branch: `fix/ajustes-finais`.

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
- Resultado: BUILD SUCCESS, 147 execucoes, zero falhas, zero erros.
- JaCoCo: 92,8% de linhas e 77,1% de branches.
- Verify local aprovado; CI remoto, merge e tag da v1.3.4 nao executados porque o ambiente nao possui autenticacao GitHub.

## Matriz TDD: regras, limites e falhas

| Regra | Caso Feliz | Valor Limite | Entrada Inválida | Conflito | Estado Proibido |
|---|---|---|---|---|---|
| Cadastro, login e perfil público seguro | Cadastro 201 e login 200 com token (`AuthControllerTest.registroNovoRetorna201`, `loginCorretoRetorna200ComToken`) | Campos obrigatórios de cadastro validados (`CoberturaExtraTest.registroSemPerfilRetorna400ComErro`) | Email ausente ou inválido retorna 400 (`ValidacaoEntradaTest.registroSemEmailRetorna400ComErro`, `CoberturaExtraTest.loginEmailInvalidoRetorna400ComErro`) | Email duplicado retorna 409 (`AuthControllerTest.registroEmailDuplicadoRetorna409`) | Perfil ADMINISTRADOR enviado no cadastro é persistido como SOLICITANTE (`AuthControllerTest.cadastroComPerfilAdministradorGravaSolicitante`) |
| Acesso a recursos por perfil | Solicitante autenticado consulta sala e professor por ID com 200 (`AutorizacaoPorPerfilTest.endpointsPorIdExigemAutenticacao`) | Consultas por ID cobrem recursos existentes (`AutorizacaoPorPerfilTest.endpointsPorIdExigemAutenticacao`) | Requisição anônima retorna 401 (`AutorizacaoPorPerfilTest.endpointsPorIdExigemAutenticacao`) | — | Perfil sem permissão não cria sala, professor ou material (403; `AutorizacaoPorPerfilTest.matrizDeAutorizacao`, `ProfessorMaterialTest`) |
| Validação de sala, professor e material | CRUD válido de sala, professor e material (`SalaControllerBranchTest`, `ProfessorMaterialTest`, `ProfessorMaterialCrudTest`) | Capacidade e quantidade devem ser no mínimo 1 (`SalaRequest`, `MaterialRequest`; limite configurado com `@Min(1)`) | Capacidade negativa, nome/email/tipo ausentes ou corpo inválido retornam 400 (`ValidacaoEntradaTest`, `CoberturaExtraTest`) | Email de professor duplicado retorna 409 (`ProfessorMaterialTest.emailDuplicadoProfessor409`) | — |
| Filtros de consulta | Capacidade/localização combinadas e filtros de competência/tipo (`FiltroSalaTest`, `ProfessorMaterialTest`) | Capacidade mínima inclusiva inclui salas com capacidade igual ou superior ao filtro (`FiltroSalaTest.filtraPorCapacidadeMinima`) | Intervalo de disponibilidade invertido retorna 400 (`FiltroSalaTest.intervaloInvalidoRetornaErro`) | Reserva ativa remove sala da busca de disponibilidade (`FiltroSalaTest.filtraPorDisponibilidade`) | Reserva cancelada não ocupa a sala (`FiltroSalaTest.reservaCanceladaNaoOcupa`) |
| Intervalo de reserva | Horários adjacentes são aceitos (`RegrasReservaTest.horariosSemSobreposicaoAceitos`) | Término deve ser estritamente posterior ao início; intervalo igual retorna 400 (`ValidacaoEntradaTest.validacao`) | Início/término ou sala ausentes, e corpo malformado, retornam 400 (`ValidacaoEntradaTest.validacao`) | Sobreposição total ou parcial retorna 409 (`RegrasReservaTest`) | — |
| Disponibilidade de recursos reserváveis | Reserva sem sobreposição é criada com 201 (`RegrasReservaTest.horariosSemSobreposicaoAceitos`) | — | ID de sala/professor/material inexistente retorna 404 (`RegrasReservaTest`, `SobreposicaoRecursosTest`) | Sobreposição de sala, professor ou material retorna 409 (`SobreposicaoTest`, `SobreposicaoRecursosTest`) | Sala ou material em manutenção, ou professor inativo, não podem ser reservados (`RegrasReservaTest.salaEmManutencaoRetorna409`, `SobreposicaoRecursosTest`) |
| Concorrência de reservas | Uma solicitação concorrente é aceita para sala ou professor (`ConcorrenciaTest`) | Dez solicitações simultâneas resultam em uma reserva aceita e nove conflitos (`ConcorrenciaTest`) | — | Tentativas concorrentes restantes retornam 409 (`ConcorrenciaTest`) | Duas reservas simultâneas do mesmo recurso não podem ser confirmadas (`ConcorrenciaTest`) |
| Alteração e cancelamento com posse | Dono altera e cancela a própria reserva (`AlteracaoReservaTest.donoAlteraPropriaReserva200`, `PosseReservaTest.donoCancelaPropriaReserva204`) | Término anterior ao início retorna 400 (`AlteracaoReservaTest.terminoAntesDoInicio400`) | Reserva inexistente retorna 404 (`AlteracaoReservaTest`) | Novo horário sobreposto retorna 409 (`AlteracaoReservaTest.alterarParaHorarioEmConflito409`) | Outro solicitante não altera/cancela; reserva iniciada não pode ser alterada e reserva em uso não pode ser cancelada (`AlteracaoReservaTest`, `PosseReservaTest`, `AprovacaoTest.cancelarReservaEmUsoRetorna409`) |
| Aprovação e máquina de estados | Responsável/admin aprova; transições válidas seguem o fluxo (`AprovacaoTest`, `MaquinaEstadosTest`) | — | Reserva inexistente retorna 404 (`AprovacaoTest.aprovarReservaInexistenteRetorna404`, `MaquinaEstadosTest.transicaoEmReservaInexistente404`) | Aprovação repetida ou transição inválida retorna 409 (`AprovacaoTest.naoAprovaReservaJaAprovada409`, `MaquinaEstadosTest`) | Solicitante não aprova/inicia; estados rejeitado ou concluído não aceitam transições proibidas (`AprovacaoTest`, `MaquinaEstadosTest`) |
| Autorização de transições | Responsável aprova e inicia reserva (`AprovacaoTest.responsavelAprovaReserva200`, `MaquinaEstadosTest.aprovadaParaEmUsoPeloResponsavel`) | — | Reserva inexistente retorna 404 | Transição não permitida retorna 409 | Anônimo recebe 401 e solicitante recebe 403 ao executar operação restrita (`AprovacaoTest`, `MaquinaEstadosTest`) |
| Movimentação de materiais | Retirada e devolução atualizam saldo (`MovimentacaoMaterialTest.responsavelRetiraTresDeCinco`, `devolucaoAposRetirada`) | Retirada não pode exceder quantidade disponível; devolução não pode exceder o que está em posse (`MovimentacaoMaterialTest`) | Quantidade zero retorna 400 (`MovimentacaoMaterialTest.quantidadeZero400`) | Excesso de retirada/devolução ou material em manutenção retorna 409 (`MovimentacaoMaterialTest`) | Solicitante não movimenta material; anônimo recebe 401 (`MovimentacaoMaterialTest`) |
| Auditoria de estados | Cada transição registra histórico (`AuditoriaTest`, `MaquinaEstadosTest.historicoRegistraCadaTransicao`) | — | Reserva inexistente retorna 404 (`AuditoriaTest.historicoDeReservaInexistente404`) | — | Falha ao persistir auditoria desfaz a reserva na mesma transação (`AuditoriaRollbackTest.falhaNaAuditoriaDesfazAReserva`); solicitante não consulta histórico (403, `AuditoriaTest`) |
| Notificação externa | Provedor recebe notificação na aprovação/criação (`NotificacaoTest`) | Timeout do provedor é tolerado (`NotificacaoTest.provedorLentoNaoQuebraTransicao`) | — | Erro HTTP 500 do provedor não interrompe a transição (`NotificacaoTest.provedorComErro500NaoQuebraTransicao`) | Falha externa não pode desfazer a operação principal (`NotificacaoTest`) |
| Relatório de utilização | Responsável consulta horas e conflitos evitados com valores exatos (`RelatorioUtilizacaoTest`) | Intervalo solicitado é obrigatório e validado (`RelatorioUtilizacaoTest`) | Intervalo inválido retorna 400 (`RelatorioUtilizacaoTest`) | Conflito evitado é contabilizado (`RelatorioUtilizacaoTest.conflitoEvitadoApareceNoRelatorio`) | Solicitante não consulta relatório (403, `RelatorioUtilizacaoTest.solicitanteNaoVeRelatorio403`) |
| Fluxo ponta a ponta e BDD | Fluxo de cadastro, reserva, aprovação, uso e conclusão (`E2ETest`); cenário BDD de aprovação (`aprovacao.feature`) | — | — | — | — |
| Interface e documentação | Endpoints e perfis estão descritos no README e na matriz de autorização | — | — | — | Interface web permanece parcial e sem teste automatizado de UI (RF-13) |
