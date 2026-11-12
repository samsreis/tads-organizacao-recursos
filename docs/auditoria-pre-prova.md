# Auditoria externa pre-prova oral

Escopo: revisao somente leitura da entrega `v1.4.2`, commit
`ea9bc644dbed08446595b3484aa11fc9f6832e6e` (branch `main`).
O run GitHub Actions `43153065512` foi confirmado pela CLI como concluido com
sucesso na branch/tag `v1.4.2`, apontando para esse commit. A tag local tambem
aponta para o mesmo SHA.

| Item | Situacao | Evidencia (caminho e linha) | Acao antes da prova |
|---|---|---|---|
| 1. Analise SonarCloud | conforme | A API `https://sonarcloud.io/api/project_analyses/search?project=samsreis_tads-organizacao-recursos&ps=20` mostra como analise mais recente a revisao `ea9bc644dbed08446595b3484aa11fc9f6832e6e`, em `2026-11-12 04:11:55 UTC`. A analise anterior em `7d92b83436b1edd885fa8e08c8da4260a800750d` e de `2026-11-12 03:51:02 UTC`. `git diff 7d92b83 ea9bc64` lista somente 14 arquivos Markdown; nao houve mudanca de codigo de aplicacao entre esses SHAs. A afirmacao antiga sobre analise pendente consta em `docs/decisao-qualidade.md:44` e `docs/evidencias/README.md:45`. | Na apresentacao, citar a analise mais recente e o SHA integral; corrigir antes as afirmacoes antigas nos materiais de evidencia e decisao. |
| 2. Links e evidencias | divergente | `docs/evidencias/README.md:5,37` contem link de CI filtrado pela tag `v1.4.2`; o CI foi confirmado como sucesso pelo `gh run view`, mas o link de filtro respondeu HTTP 404 sem autenticacao. O permalink do run e `https://github.com/samsreis/tads-organizacao-recursos/actions/runs/43153065512` (tambem HTTP 404 sem autenticacao), portanto acesso externo nao ficou verificavel. JaCoCo aparece como caminho local, nao link direto, em `docs/evidencias/README.md:33,41`; existe artefato `relatorios-104` anexado ao run. SonarCloud tem link para o painel geral em `docs/evidencias/README.md:45,54`, mas nao para a revisao; o texto em `:45` diz que nao houve analise recente, embora a API confirme `ea9bc64`. Performance e listada como caminho de documento, sem link externo ou associacao explicita a tag/commit, em `docs/evidencias/README.md:28-30,49`; o arquivo `docs/performance/resultado-02.md` existe. | Tornar o CI acessivel ao avaliador externo ou fornecer evidencia exportavel; incluir acesso direto ao artefato JaCoCo e links/pinos de versao para o SonarCloud e o resultado de performance. Atualizar a declaracao Sonar desatualizada. |
| 3. Roteiro de apresentacao | conforme | `docs/prova-oral/ensaio.md:7-32` cobre os seis blocos: problema e risco, arquitetura, demonstracao, testes e CI, RTM e relatorio, limitacoes. A tag `v1.4.2` e citada em `docs/prova-oral/ensaio.md:54`. O roteiro ainda informa 145 execucoes e que CI remoto depende de autenticacao em `:21-23`; `docs/evidencias/README.md:5` informa 147 execucoes e o run remoto foi confirmado verde. | Manter os seis blocos e a tag; alinhar a contagem de testes e atualizar a fala sobre CI remoto para nao contradizer as evidencias atuais. |
| 4. Banco de perguntas | ausente | `docs/prova-oral/perguntas.md:10-12` cobre constraint de exclusao e dupla reserva; `:26-34` cobre autorizacao por perfil e posse; `:54-55` cobre mutacao manual; `:63-65` cobre cobertura de branch. Nao ha pergunta e resposta sobre manutencao bloqueando reserva nem sobre a causa raiz de um defeito real. | Acrescentar perguntas com respostas curtas e referencias verificaveis para manutencao e para um defeito real documentado, incluindo sua causa raiz. |
| 5. README e comandos | conforme | `README.md:9-26` informa Docker Compose v2 e mostra `docker compose up -d db`, `./mvnw spring-boot:run` e o health check em `/actuator/health`. `README.md:28-35` documenta Docker para Testcontainers e `./mvnw -B verify`. | Nenhuma correcao necessaria para os comandos solicitados; executar o roteiro em clone limpo antes da prova, se ainda nao feito. |
| 6. Decisao de qualidade | divergente | Os dois riscos aceitos tem responsavel em `docs/decisao-qualidade.md:32-35`. A evidencia do criterio de CI diz que o remoto nao foi executado em `:22`, contradizendo a conclusao que diz CI verde em `:43`. A conclusao e a linha `:44` tambem mantem a analise Sonar no SHA anterior e diz que a branch aguarda nova analise, contrario a API atual. | Atualizar as evidencias e a conclusao da decisao para refletir o run verde no SHA da tag e a analise Sonar de `ea9bc64`; manter identificados responsaveis e riscos aceitos. |

## Limites da verificacao

- O resultado do run e seus metadados foram consultados autenticadamente pela
  GitHub CLI; os URLs do GitHub testados sem autenticacao retornaram HTTP 404.
  Assim, o sucesso esta confirmado, mas nao foi possivel confirmar que o
  avaliador externo consiga abrir esses links.
- O artefato JaCoCo esta listado entre os artefatos do run; o README nao fornece
  um link direto para ele. O relatorio local `target/site/jacoco/index.html`
  existe neste checkout.
- O resultado de performance esta presente no repositorio, mas o indice de
  evidencias nao o vincula diretamente a `v1.4.2` ou ao SHA completo.
