# Ensaio individual da prova oral

Roteiro individual de 8 minutos.

## Estrutura do roteiro

1. Problema e risco (1 min)
   - Sistema aloca salas, professores e materiais sem conflito.
   - Riscos: dupla reserva, sobreposicao, autorizacao, manutencao.

2. Arquitetura (1 min 30)
   - Spring Boot 3.5.16, Java 21, PostgreSQL 16.
   - C4 em docs/arquitetura.md.
   - Constraints de exclusao no banco, lock pessimista, JWT.

3. Demonstracao (2 min)
   - Login, filtro de sala, criar reserva, aprovar, iniciar, concluir.
   - Interface em /index.html.

4. Testes e CI (1 min 30)
   - 145 execucoes de teste, Testcontainers, MockMvc, WireMock.
   - Cobertura local JaCoCo: 93,6% linha e 78,3% branch.
   - Verify local aprovado; PR e CI remotos dependem de autenticacao GitHub.

5. RTM e relatorio (1 min)
   - docs/rtm.md liga requisito a teste e evidencia.
   - docs/relatorio-qualidade.md com numeros reais.

6. Limitacoes (1 min)
   - JMeter no Codespace: login p95 209,05 ms, salas 17,45 ms e reserva 39,9 ms nesta unica rodada.
   - Carga unica em ambiente local.
   - Interface minima, sem animacao.

## Pontuacao do ensaio

Preencher por integrante apos rodar o roteiro cronometrado.

| Integrante | Tempo | Pontos fortes | Lacunas |
|------------|-------|---------------|---------|
| Samuel Silva dos Reis | 8 min | Explicou o fluxo de CI e o TDD da autorizacao. | Faltou citar o ADR do lock pessimista e as constraints do banco. |
| Samuel Silva dos Reis | nao cronometrado | Ensaio realizado em 2026-11-09 sobre o roteiro de 8 minutos, sem registro de tempo por bloco. | Sem lacuna declarada no ensaio. Plano de estudo mantido. |

## Lacunas mais frequentes

1. Esquecer os numeros exatos de cobertura (93,6% linha e 78,3% branch no JaCoCo local).
2. Nao explicar a diferenca entre WireMock e Testcontainers.
3. Nao citar a constraint de exclusao do banco de dados.

## Plano de estudo ate a Semana 47

1. Ler o docs/rtm.md em voz alta uma vez por dia.
2. Revisar o ADR da constraint de exclusao e do lock pessimista.
3. Ensaiar a demonstracao do frontend no Codespaces com cronometro.
4. Explicar que a v1.4.2 e a candidata validada localmente e que a tag v1.4.2 foi publicada com CI verde.

## Guia de comandos da apresentacao

### O que eu rodo

Bloco 3 - subir banco e aplicacao:
docker compose up -d db
./mvnw spring-boot:run > /tmp/app.log 2>&1 &
sleep 40
curl -sS http://localhost:8080/actuator/health

Criar tres usuarios de teste:
curl -sS -X POST http://localhost:8080/auth/registrar -H "Content-Type: application/json" -d '{"nome":"Solicitante Teste","email":"sol@teste.invalid","senha":"123456","perfil":"SOLICITANTE"}'
curl -sS -X POST http://localhost:8080/auth/registrar -H "Content-Type: application/json" -d '{"nome":"Responsavel Teste","email":"resp@teste.invalid","senha":"123456","perfil":"RESPONSAVEL"}'
curl -sS -X POST http://localhost:8080/auth/registrar -H "Content-Type: application/json" -d '{"nome":"Admin Teste","email":"adm@teste.invalid","senha":"123456","perfil":"ADMINISTRADOR"}'

Elevar perfis (cadastro publico forca SOLICITANTE, comportamento do DEF-03):
docker exec -it recursos-db psql -U recursos -d recursos -c "UPDATE usuarios SET perfil='RESPONSAVEL' WHERE email='resp@teste.invalid'; UPDATE usuarios SET perfil='ADMINISTRADOR' WHERE email='adm@teste.invalid';"

Criar sala como admin:
TOKEN=$(curl -sS -X POST http://localhost:8080/auth/login -H "Content-Type: application/json" -d '{"email":"adm@teste.invalid","senha":"123456"}' | sed -E 's/.*"token":"([^"]+)".*/\1/')
curl -sS -X POST http://localhost:8080/salas -H "Content-Type: application/json" -H "Authorization: Bearer $TOKEN" -d '{"nome":"Sala 101","capacidade":30,"localizacao":"Bloco A","emManutencao":false}'

Criar reserva como solicitante (esperado 201):
SOL=$(curl -sS -X POST http://localhost:8080/auth/login -H "Content-Type: application/json" -d '{"email":"sol@teste.invalid","senha":"123456"}' | sed -E 's/.*"token":"([^"]+)".*/\1/')
curl -sS -X POST http://localhost:8080/reservas -H "Content-Type: application/json" -H "Authorization: Bearer $SOL" -d '{"salaId":1,"inicio":"2032-04-01T10:00:00-03:00","fim":"2032-04-01T11:00:00-03:00"}'

Testar sobreposicao (esperado 409):
curl -sS -o /dev/null -w "%{http_code}\n" -X POST http://localhost:8080/reservas -H "Content-Type: application/json" -H "Authorization: Bearer $SOL" -d '{"salaId":1,"inicio":"2032-04-01T10:30:00-03:00","fim":"2032-04-01T11:30:00-03:00"}'

Testar autorizacao (esperado 403):
RESP=$(curl -sS -X POST http://localhost:8080/auth/login -H "Content-Type: application/json" -d '{"email":"resp@teste.invalid","senha":"123456"}' | sed -E 's/.*"token":"([^"]+)".*/\1/')
curl -sS -X POST http://localhost:8080/reservas -H "Content-Type: application/json" -H "Authorization: Bearer $RESP" -d '{"salaId":1,"inicio":"2032-05-01T10:00:00-03:00","fim":"2032-05-01T11:00:00-03:00"}'
curl -sS -o /dev/null -w "%{http_code}\n" -X DELETE http://localhost:8080/reservas/3 -H "Authorization: Bearer $SOL"

Encerrar:
kill %1
docker compose down -v

### O que o professor vai rodar (simular antes)

Clone limpo e verify:
cd /tmp && rm -rf sim-prof && git clone https://github.com/samsreis/tads-organizacao-recursos.git sim-prof && cd sim-prof && git checkout v1.4.3 && git describe --tags && docker compose up -d db && ./mvnw -B verify

Teste de validacao (esperado 400 sem stack trace):
curl -sS -X POST http://localhost:8080/reservas -H "Content-Type: application/json" -H "Authorization: Bearer $SOL" -d '{"salaId":1,"inicio":"2032-04-01T11:00:00-03:00","fim":"2032-04-01T10:00:00-03:00"}'

Mutacao ao vivo (dupla reserva):
cp src/main/java/br/edu/senac/recursos/config/InicializadorBanco.java /tmp/InicializadorBanco.bak
# comentar a linha da constraint de sala no arquivo
./mvnw -B test -Dtest=ConcorrenciaTest
cp /tmp/InicializadorBanco.bak src/main/java/br/edu/senac/recursos/config/InicializadorBanco.java
git diff --stat
./mvnw -B test -Dtest=ConcorrenciaTest

### O que mostrar no navegador

- https://github.com/samsreis/tads-organizacao-recursos/actions?query=branch%3Av1.4.3 - CI verde
- JaCoCo: 93,6% linha, 78,3% branch (target/site/jacoco/index.html)
- SonarCloud: Quality Gate Passed, 89,7%, zero issues
- docs/rtm.md linha Dupla reserva -> ConcorrenciaTest -> run da tag

### Frases de apoio por bloco

Bloco 1: o risco central e dupla reserva e sobreposicao. Se duas pessoas pedirem a mesma sala no mesmo horario, uma reserva e aceita e a outra recusada com mensagem clara.
Bloco 2: duas camadas de defesa. Validacao na aplicacao e constraint de exclusao no banco. A segunda existe porque a primeira sozinha tem janela de corrida sob concorrencia.
Bloco 4: 93,6% de linha e 78,3% de branch, acima de 80% e 70%. O build falha se cair, entao cobertura e gate, nao relatorio.
Bloco 5: cada linha da RTM liga requisito, risco, teste e evidencia.
Bloco 6: trabalho individual, sem revisao por pares. Carga unica local, nao comprova producao. Com mais tempo: revisao por pares, mais rodadas de carga, testes exploratorios.
