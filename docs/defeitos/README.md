# Registro de defeitos

| ID | Titulo | Severidade | Prioridade | Situacao | Commit de correcao |
|---|---|---|---|---|---|
| DEF-01 | Sem token retornava 403 em vez de 401 | media | P1 | fechado | c0a8bcc |
| DEF-02 | Erro de validacao sem mensagem amigavel | media | P1 | fechado | 420677d |
| DEF-03 | Cadastro publico aceitava perfil privilegiado | critica | P0 | fechado | 771e5d1 |
| DEF-04 | Mensagem antiga permanecia na tela ao trocar de usuario | baixa | P3 | fechado | fix/frontend-estados-v1.3.2 |
| DEF-05 | Lista de aprovacao mostrava reservas ja finalizadas | media | P2 | fechado | fix/frontend-estados-v1.3.2 |
| DEF-06 | Botoes de acao ignoravam a maquina de estados | media | P2 | fechado | fix/frontend-estados-v1.3.2 |

Defeitos adicionais surgidos no desenvolvimento:

- Intermitencia do teste de concorrencia (corrigido em e62e2b8 com lock pessimista na sala, ver ADR-004).
