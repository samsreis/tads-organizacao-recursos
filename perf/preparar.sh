#!/bin/bash
set -e
BASE=http://localhost:8080

echo "Aguardando aplicacao responder..."
for i in $(seq 1 60); do
  if curl -s -o /dev/null -w "%{http_code}" $BASE/actuator/health | grep -q 200; then
    echo "OK - app respondendo"
    break
  fi
  sleep 2
done

echo "Criando usuarios SOLICITANTE de teste (ignora se ja existem)..."
for email in sol@teste.com sol2@teste.com; do
  curl -s -o /dev/null -X POST $BASE/auth/registrar \
    -H "Content-Type: application/json" \
    -d "{\"email\":\"$email\",\"senha\":\"123456\",\"nome\":\"$email\",\"perfil\":\"SOLICITANTE\"}" || true
done

echo "O cadastro publico cria sempre SOLICITANTE. Contas RESPONSAVEL e ADMINISTRADOR"
echo "precisam existir no banco antes deste script. Ver README, secao de cadastro."

echo "Fazendo login para pegar token do admin..."
TOKEN=$(curl -s -X POST $BASE/auth/login -H "Content-Type: application/json" \
  -d '{"email":"adm@teste.com","senha":"123456"}' | sed -E 's/.*"token":"([^"]+)".*/\1/')

if [ -z "$TOKEN" ] || [ "$TOKEN" = "null" ]; then
  echo "Falha no login do admin. A conta adm@teste.com precisa existir no banco com perfil ADMINISTRADOR."
  echo "Insira direto no banco com senha BCrypt, conforme o README."
  exit 1
fi

echo "Criando sala base se nao existir..."
curl -s -X POST $BASE/salas \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"nome":"Sala Perf","capacidade":30,"localizacao":"Bloco P","emManutencao":false}' || true

echo ""
echo "Salas existentes:"
curl -s $BASE/salas -H "Authorization: Bearer $TOKEN"
