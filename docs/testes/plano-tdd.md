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

## Matriz regra -> caso -> teste -> status

| Regra | Caso coberto | Teste(s) | Status |
|---|---|---|---|
| Cadastro, login e perfil publico seguro | Cadastro novo/duplicado, login valido/invalido e perfil privilegiado enviado no cadastro | `AuthControllerTest` | aprovado |
| Recursos exigem autenticacao e autorizacao por perfil | Rotas publicas, autenticacao, permissao por papel e leitura individual de sala/professor | `AutorizacaoPorPerfilTest.matrizDeAutorizacao`, `solicitanteConsultaSalaPorId200`, `solicitanteConsultaProfessorPorId200`; `ProfessorMaterialTest` | aprovado |
| Validacao de entrada apresenta erro controlado | Dados ausentes, formato invalido, campos obrigatorios e intervalos incorretos | `ValidacaoEntradaTest`, `CoberturaExtraTest`, `AlteracaoReservaTest.terminoAntesDoInicio400` | aprovado |
| Salas suportam cadastro, consulta e atualizacao | Cadastro com manutencao, busca/atualizacao de inexistente e caminhos alternativos do controller | `SalaControllerTest`, `SalaControllerBranchTest` | aprovado |
| Professores e materiais suportam CRUD | Criar/listar/filtrar, duplicidade, busca, alteracao, exclusao e recurso inexistente | `ProfessorMaterialTest`, `ProfessorMaterialCrudTest` | aprovado |
| Pesquisa respeita filtros | Capacidade, localizacao, disponibilidade, competencia e tipo de material | `FiltroSalaTest`, `ProfessorMaterialTest.filtroPorCompetencia`, `filtroMaterialPorTipo` | aprovado |
| Reserva exige intervalo valido e disponibilidade | Sobreposicao total/parcial, horario livre, sala em manutencao e recurso inexistente | `RegrasReservaTest`, `ValidacaoEntradaTest` | aprovado |
| Reserva so pode ser alterada/cancelada por seu dono e no estado permitido | Dono, outro solicitante, inicio da reserva, conflito e reserva inexistente | `PosseReservaTest`, `AlteracaoReservaTest` | aprovado |
| Recursos nao podem ser reservados em conflito | Sobreposicao no banco para sala, professor ou material; professor inativo e recursos em manutencao | `SobreposicaoTest`, `SobreposicaoRecursosTest`, `RegrasReservaTest` | aprovado |
| Concorrencia aceita no maximo uma reserva de recurso compartilhado | Dez requisicoes simultaneas disputam a sala ou o mesmo professor | `ConcorrenciaTest.apenasUmaReservaAceitaEmDezSolicitacoesSimultaneas`, `apenasUmaReservaAceitaParaProfessorEmSolicitacoesSimultaneas` | aprovado |
| Aprovacao e transicoes seguem a maquina de estados | Papeis autorizados, transicao valida/invalida, inexistente e cancelamento durante uso | `AprovacaoTest`, `MaquinaEstadosTest` | aprovado |
| Toda transicao deixa auditoria atomica | Criacao, aprovacao, cancelamento e rollback quando a auditoria falha | `AuditoriaTest`, `AuditoriaRollbackTest.falhaNaAuditoriaDesfazAReserva` | aprovado |
| Notificacao externa nao derruba a operacao | Sucesso, HTTP 500, provedor lento e notificacao na criacao | `NotificacaoTest` | aprovado |
| Relatorio calcula carga horaria e conflitos evitados | Reserva de duas horas por sala/professor e conflito contabilizado | `RelatorioUtilizacaoTest.responsavelVeRelatorio`, `conflitoEvitadoApareceNoRelatorio` | aprovado |
| Material controla retirada, devolucao e saldo | Quantidade disponivel, excesso, manutencao, autorizacao e historico | `MovimentacaoMaterialTest` | aprovado |
| Fluxo ponta a ponta integra os principais recursos | Fluxo combinado de API | `E2ETest` | aprovado |
| Especificacao de comportamento usa Gherkin | Responsavel aprova uma reserva solicitada | `src/test/resources/features/aprovacao.feature` (sem step definitions) | artefato BDD presente |
| Interface e mensagens publicas mantem cobertura declarada | Interface minima documentada; nao ha teste automatizado de UI | Revisao de `src/main/resources/static`; RF-13 permanece parcial | limitado por escopo |
| Documentacao dos fluxos esta acessivel | Endpoints, perfis, regras e execucao documentados | `README.md`, `docs/seguranca/matriz-autorizacao.md` | conferido |
