# Licoes aprendidas

## O que funcionou

- Constraint de exclusao no banco foi decisiva. Nenhuma reserva duplicada passou.
- Lock pessimista estabilizou o teste de concorrencia.
- Testcontainers garantiu que constraint e transacao funcionassem como em producao.
- TratadorGlobal eliminou vazamento de stack trace.
- Auditoria na mesma transacao da mudanca de estado evitou registro orfao.

## O que quase deu errado

- JwtFiltro como @Component causava 403 em POST /reservas por dupla inscricao do filtro. Solucao: instanciar dentro do SecurityConfig.
- Ordem das regras do SecurityConfig e decisiva. Regra generica antes da especifica gera 403 inesperado.
- JMeter resolve caminho de CSV relativo ao diretorio do .jmx. Caminho absoluto e mais seguro.
- JMeter exige domain, port e path separados. URL inteira em path nao funciona.

## O que fazer diferente

- Escrever a matriz de autorizacao antes dos controllers, nao depois.
- Automatizar o calculo de percentis do JTL num script versionado, em vez de Python pontual.
- Ter o README pronto desde a Semana 35.

## Regras novas para o proximo projeto

- Toda rota nova entra na matriz e no SecurityConfig na mesma PR.
- Toda regra critica tem pelo menos um teste de mutacao manual.
- Todo PR tem que passar no verify local antes do push.
