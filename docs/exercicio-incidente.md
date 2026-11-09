# Exercicio de incidente simulado

Data: 2026-11-09.
Branch descartavel: exercicio/incidente. Removida ao final. Main nao foi tocada.

## Defeito injetado

Arquivo: ReservaService.java, linha 67.
Troca: if (Boolean.TRUE.equals(sala.getEmManutencao())) por if (sala.getEmManutencao() == null).
Efeito: sala em manutencao deixava de ser bloqueada e a reserva era aceita com 201.

## Deteccao

Teste que denunciou: RegrasReservaTest.salaEmManutencaoRetorna409.
Mensagem: Status expected 409 but was 201.
Tempo ate a deteccao: menos de cinco minutos, pela execucao focada em RegrasReservaTest e SobreposicaoRecursosTest.
Nenhum teste faltava neste caso.

## Conclusao

A suite denunciou o defeito porque o teste nomeia a regra. Restaurado com git checkout do arquivo antes de sair da branch.

## Revisao cruzada

Nao realizada. Depende de repositorio de equipe vizinha, combinado em sala. Trabalho individual, sem equipe par. Registrado como escopo fora da Aula 16.
