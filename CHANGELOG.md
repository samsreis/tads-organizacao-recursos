# Changelog

## 1.3.3 - candidata, pendente de CI e publicacao

### Testes e qualidade

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

- Esta versao ainda nao foi publicada. PR, CI remoto, merge e tag v1.3.3 estao pendentes.
