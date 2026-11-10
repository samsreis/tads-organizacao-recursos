# Defeito 03: cadastro publico aceitava perfil privilegiado

## Identificacao
- ID: DEF-03
- Severidade: critica
- Prioridade: P0
- Encontrado em: revisao externa do codigo
- Corrigido em: commit 771e5d1

## Recurso observavel
Endpoint POST /auth/registrar.

## Ambiente
- branch main
- Spring Boot 3.5.16
- Java 21

## Pre-condicoes
Nenhuma.

## Passos minimos
1. Enviar POST /auth/registrar com corpo contendo "perfil":"ADMINISTRADOR".
2. Fazer login com a conta criada.
3. Acessar um endpoint restrito a ADMINISTRADOR.

## Resultado obtido
Conta criada com perfil ADMINISTRADOR e acesso liberado a rotas privilegiadas.

## Resultado esperado
Conta criada como SOLICITANTE. Perfis privilegiados so podem ser criados pela carga inicial ou por endpoint protegido.

## Evidencia
AuthControllerTest#cadastroComPerfilAdministradorGravaSolicitante falha no commit com a regra quebrada e passa no commit 771e5d1. Mutacao manual aplicada e revertida.

## Causa raiz
AuthController gravava req.perfil() direto no usuario, e RegistroRequest so exigia que o campo nao fosse nulo.

## Correcao
AuthController passa a gravar Perfil.SOLICITANTE no cadastro publico, com comentario explicando a decisao.

## Teste de regressao
AuthControllerTest#cadastroComPerfilAdministradorGravaSolicitante consulta o banco e verifica que o perfil gravado e SOLICITANTE.

## Licao
Falha de autorizacao e limitador de nota na especificacao. Cadastro publico nunca deve aceitar perfil do cliente.

## Situacao
Fechado. Verificado no commit 771e5d1.
