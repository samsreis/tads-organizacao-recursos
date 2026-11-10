# Matriz de autorizacao - Sistema de Organizacao de Recursos

Data: 2026-11-10.
Fonte: SecurityConfig.java e controllers.
Legenda: 200/201/204 = sucesso; 401 = sem token ou token invalido; 403 = perfil sem permissao; 409 = conflito de estado; 404 = recurso inexistente; 400 = erro de validacao.

## Operacoes publicas

| Operacao | Anonimo | SOLICITANTE | RESPONSAVEL | ADMINISTRADOR |
|---|---|---|---|---|
| POST /auth/registrar | 201 | 201 | 201 | 201 |
| POST /auth/login | 200 | 200 | 200 | 200 |
| GET /actuator/health | 200 | 200 | 200 | 200 |

## Salas

| Operacao | Anonimo | SOLICITANTE | RESPONSAVEL | ADMINISTRADOR |
|---|---|---|---|---|
| GET /salas | 401 | 200 | 200 | 200 |
| POST /salas | 401 | 403 | 403 | 201 |
| PUT /salas/{id} | 401 | 403 | 403 | 200 / 404 |
| DELETE /salas/{id} | 401 | 403 | 403 | 204 / 404 |

## Professores

| Operacao | Anonimo | SOLICITANTE | RESPONSAVEL | ADMINISTRADOR |
|---|---|---|---|---|
| GET /professores | 401 | 200 | 200 | 200 |
| POST /professores | 401 | 403 | 403 | 201 |
| PUT /professores/{id} | 401 | 403 | 403 | 200 / 404 |
| DELETE /professores/{id} | 401 | 403 | 403 | 204 / 404 |

## Materiais - CRUD

| Operacao | Anonimo | SOLICITANTE | RESPONSAVEL | ADMINISTRADOR |
|---|---|---|---|---|
| GET /materiais | 401 | 200 | 200 | 200 |
| GET /materiais/{id} | 401 | 200 / 404 | 200 / 404 | 200 / 404 |
| POST /materiais | 401 | 403 | 403 | 201 |
| PUT /materiais/{id} | 401 | 403 | 403 | 200 / 404 |
| DELETE /materiais/{id} | 401 | 403 | 403 | 204 / 404 |

## Materiais - movimentacao

| Operacao | Anonimo | SOLICITANTE | RESPONSAVEL | ADMINISTRADOR |
|---|---|---|---|---|
| POST /materiais/{id}/retiradas | 401 | 403 | 201 / 404 / 409 | 201 / 404 / 409 |
| POST /materiais/{id}/devolucoes | 401 | 403 | 201 / 404 / 409 | 201 / 404 / 409 |
| GET /materiais/{id}/saldo | 401 | 200 / 404 | 200 / 404 | 200 / 404 |
| GET /materiais/{id}/movimentacoes | 401 | 403 | 200 / 404 | 200 / 404 |

## Reservas - dono

Regra de posse: SOLICITANTE so altera e cancela reserva em que ele e o solicitante. Tentativa de mexer em reserva de outro solicitante retorna 403.

| Operacao | Anonimo | SOLICITANTE dono | SOLICITANTE nao dono | RESPONSAVEL | ADMINISTRADOR |
|---|---|---|---|---|---|
| POST /reservas | 401 | 201 / 409 | 201 / 409 | 201 / 409 | 201 / 409 |
| GET /reservas/minhas | 401 | 200 | 200 | 200 | 200 |
| GET /reservas | 401 | 403 | 200 | 200 |
| PUT /reservas/{id} | 401 | 200 / 404 / 409 | 403 | 200 / 404 / 409 | 200 / 404 / 409 |
| DELETE /reservas/{id} | 401 | 204 / 404 / 409 | 403 | 204 / 404 / 409 | 204 / 404 / 409 |

## Reservas - transicoes e historico

| Operacao | Anonimo | SOLICITANTE | RESPONSAVEL | ADMINISTRADOR |
|---|---|---|---|---|
| GET /reservas/{id}/historico | 401 | 403 | 200 / 404 | 200 / 404 |
| POST /reservas/{id}/aprovar | 401 | 403 | 200 / 404 / 409 | 200 / 404 / 409 |
| POST /reservas/{id}/rejeitar | 401 | 403 | 200 / 404 / 409 | 200 / 404 / 409 |
| POST /reservas/{id}/iniciar | 401 | 403 | 200 / 404 / 409 | 200 / 404 / 409 |
| POST /reservas/{id}/concluir | 401 | 403 | 200 / 404 / 409 | 200 / 404 / 409 |
| POST /reservas/{id}/nao-compareceu | 401 | 403 | 200 / 404 / 409 | 200 / 404 / 409 |

## Relatorios

| Operacao | Anonimo | SOLICITANTE | RESPONSAVEL | ADMINISTRADOR |
|---|---|---|---|---|
| GET /relatorios/utilizacao?inicio&fim | 401 | 403 | 200 / 400 | 200 / 400 |

## Rotas fora de qualquer regra especifica

Nenhuma. A regra final anyRequest().authenticated() cobre qualquer rota nao listada, exigindo token. As rotas publicas estao explicitamente liberadas.

## Divergencias conhecidas entre matriz e codigo

Nenhuma divergencia pendente no momento desta revisao.

## Notas

- 409 em transicao vem de transicao invalida na maquina de estados.
- DELETE /reservas/{id} para reserva em EM_USO devolve 409; a transicao esta bloqueada em ReservaService.TRANSICOES.
- 409 em criacao ou alteracao de reserva vem da constraint de exclusao no banco.
- 401 vem de HttpStatusEntryPoint no SecurityConfig.
- 403 vem de hasRole/hasAnyRole.
- Validacao de entrada devolve 400 com corpo {"erro": "..."} sem stack trace.
- A regra de cancelamento em uso foi revalidada por AprovacaoTest.cancelarReservaEmUsoRetorna409 e por mutacao temporaria na maquina de estados do servico.
