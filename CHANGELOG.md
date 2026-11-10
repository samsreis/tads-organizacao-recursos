# Changelog

## v1.3.8 - Resolução das pendências da auditoria

### Testes e qualidade

- Injetado `java.time.Clock` em `AuditoriaService` para controlar a origem do horario registrado.
- Adicionados testes autenticados para `GET /salas/{id}` e `GET /professores/{id}`.
- Asseridos os valores exatos de horas reservadas para sala e professor no relatorio de utilizacao.
- Adicionado o cenario Gherkin de aprovacao em `src/test/resources/features/aprovacao.feature`.
- Adicionada verificacao concorrente de reservas do mesmo professor em salas distintas.
- Fortalecida a verificacao do cancelamento em uso e registrada a mutacao correspondente em `ReservaService`.
- Substituido o teste de rollback sem falha real por verificacao que simula falha na persistencia da auditoria.
- Asserido o valor exato de conflitos evitados no relatorio de utilizacao.
- Atualizados os relatorios de teste, qualidade, rastreabilidade e decisao com medições locais verificadas.

### Desempenho

- Corrigido o perfil JMeter para autenticar uma vez por usuario, conforme o plano.
- Tornado configuravel o caminho do CSV e ajustada a assercao para que HTTP 409 seja sucesso esperado somente quando o status corresponder a 201 ou 409.
- Registrada a rodada no Codespace em `docs/performance/resultado-02.md`.

### Publicacao

- `./mvnw -B verify` aprovado localmente; publicação no GitHub não realizada nesta rodada por falta de autenticação.
