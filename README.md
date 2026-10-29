# Sistema de Organizacao de Recursos

Projeto final da disciplina Qualidade de Software 2026.2, TADS, Senac, professor Afonso.
Sistema Spring Boot para alocar salas, professores e materiais sem conflito de horario.

## Requisitos

- Java 21
- Docker e Docker Compose v2
- Linux, macOS ou WSL (testado em Linux Mint)

## Como subir a aplicacao

1. Suba o banco.

    docker compose up -d db

2. Rode a aplicacao.

    ./mvnw spring-boot:run

A aplicacao sobe em http://localhost:8080.

3. Confira saude.

    curl http://localhost:8080/actuator/health

## Como rodar a suite de testes

A suite usa Testcontainers, entao precisa de Docker rodando. O banco local nao e usado nos testes.

    ./mvnw -B verify

Relatorio de cobertura em target/site/jacoco/index.html.
Regra de cobertura do build: 80% linha e 70% branch. Se cair abaixo, o build falha.

## Usuarios de teste para desenvolvimento manual

Se voce subir a aplicacao com o banco vazio, crie os usuarios pelo endpoint de registro.

    curl -X POST http://localhost:8080/auth/registrar \
      -H "Content-Type: application/json" \
      -d '{"email":"sol@teste.com","senha":"123456","nome":"Solicitante","perfil":"SOLICITANTE"}'

    curl -X POST http://localhost:8080/auth/registrar \
      -H "Content-Type: application/json" \
      -d '{"email":"res@teste.com","senha":"123456","nome":"Responsavel","perfil":"RESPONSAVEL"}'

    curl -X POST http://localhost:8080/auth/registrar \
      -H "Content-Type: application/json" \
      -d '{"email":"adm@teste.com","senha":"123456","nome":"Administrador","perfil":"ADMINISTRADOR"}'

## Perfis

- SOLICITANTE: cria, altera e cancela as proprias reservas.
- RESPONSAVEL: aprova, rejeita, inicia, conclui reservas, consulta historico, ve relatorio, registra retirada e devolucao de material.
- ADMINISTRADOR: gerencia salas, professores, materiais e usuarios. Tambem tem todas as permissoes do responsavel.

## Autenticacao

O login devolve um token JWT. Envie em todas as chamadas protegidas:

    Authorization: Bearer SEU_TOKEN

## Endpoints

| Metodo | Caminho | Perfis permitidos | Descricao |
|---|---|---|---|
| POST | /auth/registrar | publico | Cria usuario |
| POST | /auth/login | publico | Faz login e devolve token |
| GET | /actuator/health | publico | Saude da aplicacao |
| GET | /salas | autenticado | Lista salas |
| POST | /salas | ADMINISTRADOR | Cria sala |
| PUT | /salas/{id} | ADMINISTRADOR | Atualiza sala |
| DELETE | /salas/{id} | ADMINISTRADOR | Apaga sala |
| GET | /professores | autenticado | Lista professores |
| POST | /professores | ADMINISTRADOR | Cria professor |
| PUT | /professores/{id} | ADMINISTRADOR | Atualiza professor |
| DELETE | /professores/{id} | ADMINISTRADOR | Apaga professor |
| GET | /materiais | autenticado | Lista materiais |
| POST | /materiais | ADMINISTRADOR | Cria material |
| PUT | /materiais/{id} | ADMINISTRADOR | Atualiza material |
| DELETE | /materiais/{id} | ADMINISTRADOR | Apaga material |
| POST | /materiais/{id}/retiradas | RESPONSAVEL, ADMINISTRADOR | Registra retirada |
| POST | /materiais/{id}/devolucoes | RESPONSAVEL, ADMINISTRADOR | Registra devolucao |
| GET | /materiais/{id}/saldo | autenticado | Consulta saldo |
| GET | /materiais/{id}/movimentacoes | RESPONSAVEL, ADMINISTRADOR | Lista movimentacoes |
| POST | /reservas | autenticado | Cria reserva |
| GET | /reservas/minhas | autenticado | Lista as proprias reservas |
| PUT | /reservas/{id} | dono | Altera reserva propria |
| DELETE | /reservas/{id} | dono | Cancela reserva propria |
| GET | /reservas/{id}/historico | RESPONSAVEL, ADMINISTRADOR | Historico auditavel |
| POST | /reservas/{id}/aprovar | RESPONSAVEL, ADMINISTRADOR | Aprova |
| POST | /reservas/{id}/rejeitar | RESPONSAVEL, ADMINISTRADOR | Rejeita |
| POST | /reservas/{id}/iniciar | RESPONSAVEL, ADMINISTRADOR | Inicia uso |
| POST | /reservas/{id}/concluir | RESPONSAVEL, ADMINISTRADOR | Conclui |
| POST | /reservas/{id}/nao-compareceu | RESPONSAVEL, ADMINISTRADOR | Registra ausencia |
| GET | /relatorios/utilizacao?inicio=...&fim=... | RESPONSAVEL, ADMINISTRADOR | Relatorio de utilizacao |

## Maquina de estados da reserva

    SOLICITADA -> APROVADA -> EM_USO -> CONCLUIDA
    SOLICITADA -> REJEITADA
    SOLICITADA -> CANCELADA
    APROVADA   -> CANCELADA
    APROVADA   -> NAO_COMPARECEU

Estados finais: CONCLUIDA, REJEITADA, CANCELADA, NAO_COMPARECEU.
Toda transicao gera um registro de auditoria. Transicao invalida devolve 409.

## Regras criticas

- Fim deve ser depois do inicio.
- Reservas do mesmo recurso nao se sobrepoem. O banco garante com constraint de exclusao.
- Duas solicitacoes simultaneas produzem no maximo uma reserva aceita.
- Sala em manutencao, material em manutencao e professor inativo nao podem ser reservados.
- Somente RESPONSAVEL e ADMINISTRADOR aprovam, rejeitam, iniciam, concluem e registram ausencia.
- Solicitante so altera e cancela a propria reserva.
- Reserva com inicio no passado nao pode ser alterada nem cancelada.

## Exemplo de uso

Criar reserva.

    TOKEN=$(curl -s -X POST http://localhost:8080/auth/login \
      -H "Content-Type: application/json" \
      -d '{"email":"sol@teste.com","senha":"123456"}' | sed -E 's/.*"token":"([^"]+)".*/\1/')

    curl -X POST http://localhost:8080/reservas \
      -H "Content-Type: application/json" \
      -H "Authorization: Bearer $TOKEN" \
      -d '{"salaId":3,"inicio":"2030-01-01T10:00:00-03:00","fim":"2030-01-01T11:00:00-03:00"}'

Aprovar a reserva.

    TOKEN_RES=$(curl -s -X POST http://localhost:8080/auth/login \
      -H "Content-Type: application/json" \
      -d '{"email":"res@teste.com","senha":"123456"}' | sed -E 's/.*"token":"([^"]+)".*/\1/')

    curl -X POST http://localhost:8080/reservas/1/aprovar \
      -H "Authorization: Bearer $TOKEN_RES" \
      -H "Content-Type: application/json" \
      -d '{"motivo":"Aprovado"}'

## Estrutura do repositorio

- src/main/java: codigo da aplicacao.
- src/test/java: testes automatizados.
- docs: documentacao do projeto, RTM, matriz de autorizacao, defeitos, adr, performance, seguranca.
- perf: plano JMeter, CSV de usuarios e script de preparacao.
- .github/workflows/ci.yml: pipeline de integracao continua.

## Documentacao adicional

- docs/seguranca/matriz-autorizacao.md: matriz perfil x operacao.
- docs/rtm.md: matriz de rastreabilidade.
- docs/status-requisitos.md: situacao de cada requisito.
- docs/arquitetura.md: diagramas C4 e sequencias.
- docs/defeitos/: defeitos registrados.
- docs/performance/: plano e resultado do teste de carga.
- docs/relatorio-qualidade.md: relatorio final de qualidade.
- docs/decisao-qualidade.md: decisao do grupo sobre liberar ou nao.

## Como rodar o teste de carga

    ~/ferramentas/apache-jmeter-5.6.3/bin/jmeter -n -t perf/reservas.jmx \
      -l resultados/rodada-01.jtl -e -o resultados/rodada-01

Resultado em resultados/rodada-01/index.html. Analise escrita em docs/performance/resultado-01.md.

## Como rodar o pipeline local

O comando que roda no GitHub Actions e o mesmo do build local:

    ./mvnw -B verify
