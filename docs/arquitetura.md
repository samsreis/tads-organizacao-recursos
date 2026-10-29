# Arquitetura do Sistema de Organizacao de Recursos

Data: 2026-10-29.

## Contexto (C4 nivel 1)

```mermaid
flowchart LR
    Sol[Solicitante] --> Sistema[Sistema]
    Resp[Responsavel] --> Sistema
    Adm[Administrador] --> Sistema
    Sistema --> Provedor[Provedor de notificacao externo]
```

## Containers (C4 nivel 2)

```mermaid
flowchart LR
    Usuario[Usuario] -->|HTTPS com JWT| API[Aplicacao Spring Boot]
    API -->|JDBC| Banco[(PostgreSQL 16)]
    API -->|HTTP POST /notificar| Provedor[Provedor externo]
```


## Componentes (C4 nivel 3)

```mermaid
flowchart TB
    Sec[SecurityConfig e JwtFiltro]
    Auth[AuthController]
    SalaC[SalaController]
    ProfC[ProfessorController]
    MatC[MaterialController]
    ResC[ReservaController]
    MovC[MovimentacaoMaterialController]
    RelC[RelatorioController]
    ResS[ReservaService]
    MovS[MovimentacaoMaterialService]
    Aud[AuditoriaService]
    NotifS[NotificadorService]
    Repos[Repositories JPA]
    Inic[InicializadorBanco]
    Banco[(PostgreSQL 16)]
    Sec --> Auth
    Sec --> SalaC
    Sec --> ProfC
    Sec --> MatC
    Sec --> ResC
    Sec --> MovC
    Sec --> RelC
    ResC --> ResS
    ResS --> Aud
    ResS --> NotifS
    ResS --> Repos
    MovC --> MovS
    MovS --> Repos
    RelC --> Repos
    Inic --> Banco
    Repos --> Banco
```


## Sequencia critica 1: criacao de reserva

```mermaid
sequenceDiagram
    participant S as Solicitante
    participant C as ReservaController
    participant V as ReservaService
    participant B as Banco
    S->>C: POST /reservas com JWT
    C->>V: criar email e request
    V->>B: SELECT FOR UPDATE na sala
    V->>B: INSERT reserva
    B-->>V: OK ou conflito
    V-->>C: Reserva
    C-->>S: 201 ou 409
```

## Sequencia critica 2: aprovacao com auditoria e notificacao

```mermaid
sequenceDiagram
    participant R as Responsavel
    participant C as ReservaController
    participant V as ReservaService
    participant A as AuditoriaService
    participant N as NotificadorService
    R->>C: POST /reservas/id/aprovar
    C->>V: transicionar APROVADA
    V->>V: valida TRANSICOES
    V->>A: registrar SOLICITADA para APROVADA
    V->>N: notificar
    V-->>C: Reserva
    C-->>R: 200 ou 409
```

## Sequencia critica 3: dupla reserva simultanea

```mermaid
sequenceDiagram
    participant T1 as Thread 1
    participant T2 as Thread 2
    participant B as Banco
    T1->>B: SELECT FOR UPDATE sala
    T2->>B: espera no lock
    T1->>B: INSERT reserva
    B-->>T1: OK
    T1->>B: COMMIT libera lock
    T2->>B: INSERT reserva
    B-->>T2: conflito de exclusao
    T2-->>T2: 409
```

## Requisito, decisao, componente, evidencia

| Requisito | Decisao | Componente | Evidencia |
|---|---|---|---|
| RF-01 autenticacao | JWT stateless | SecurityConfig e JwtFiltro | AutorizacaoPorPerfilTest |
| RF-05 sobreposicao | constraint de exclusao | InicializadorBanco | SobreposicaoTest |
| RF-06 dupla reserva | lock pessimista mais constraint | SalaRepository e ReservaService | ConcorrenciaTest |
| RF-07 aprovacao | maquina de estados | ReservaService TRANSICOES | MaquinaEstadosTest |
| RF-09 retirada e devolucao | saldo por movimentacoes | MovimentacaoMaterialService | MovimentacaoMaterialTest |
| RF-10 auditoria | MANDATORY na mesma transacao | AuditoriaService | AuditoriaTest |
| RF-11 notificacao | RestClient com timeout e tolerancia a falha | NotificadorService | NotificacaoTest |

## Riscos residuais

- Notificacao falha silenciosa: o fluxo principal nao e afetado, mas o usuario nao recebe aviso.
- Uma unica rodada de carga.
- Interface HTML parcial.
