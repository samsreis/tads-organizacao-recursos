# Plano de testes

Data: 2026-10-29.

## Escopo

- Todos os endpoints publicos e protegidos.
- Todas as regras criticas.
- Fluxo de reserva de ponta a ponta: criar, aprovar, iniciar, concluir.
- Fluxo de material: retirar e devolver.
- Fluxo de autorizacao por perfil.
- Fluxo de concorrencia.

## Fora de escopo

- Interface HTML.
- Carga acima de 50 usuarios.
- Pen test formal.

## Abordagem

- API em caixa-preta com MockMvc e tokens reais.
- Integracao com banco real via Testcontainers.
- Integracao externa com WireMock.
- Carga com JMeter.
- Mutacao manual para provar que os testes protegem.

## Criterios de entrada

- Build local verde.
- Docker disponivel.

## Criterios de saida

- Cobertura de linha >= 80% e branch >= 70%.
- Todos os testes verdes.
- RTM fechada.
- Mutacao manual registrada.

## Criterios de suspensao

- Falha de infraestrutura local que impeça o build.

## Riscos

- Testcontainers depende de Docker.
- Tempo de build cresce com a suite.
