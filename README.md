# Microsserviços Reativos com Spring Boot e Kubernetes

Este projeto implementa uma arquitetura de microsserviços para gestão de carteira de criptomoedas, demonstrando comunicação entre serviços, persistência reativa e orquestração em nuvem.

## 🏛️ Arquitetura

O sistema é composto por 3 containers orquestrados via Kubernetes:
1.  **Wallet Service (Consumidor - Porta 80):** API principal para o usuário. Persiste dados e consome o serviço de cotação.
2.  **Quotation Service (Provedor - Porta 8081 Interna):** Proxy que busca valores reais de criptomoedas em APIs externas.
3.  **Database (PostgreSQL - Porta 5432):** Banco de dados relacional gerenciado via Flyway.

## 🚀 Como Reproduzir o Projeto

### Pré-requisitos
* Docker Desktop (com Kubernetes habilitado)
* Java 17
* Maven

### Passo 1: Clonar e Preparar
Baixe o repositório e navegue até a pasta raiz.

### Passo 2: Build das Aplicações
Gere os executáveis (JAR) e as imagens Docker locais:

```bash
# 1. Build da Wallet
cd crypto-wallet-reactive
mvn clean package -DskipTests
docker build -t crypto-wallet:latest .

# 2. Build da Cotação
cd ../crypto-quotation
mvn clean package -DskipTests
docker build -t crypto-quotation:latest .
cd ..


### Passo 3: Deploy no Kubernetes
Aplique o manifesto de orquestração unificado:

```Bash
kubectl apply -f k8s-full-deployment.yaml

Aguarde os pods iniciarem (status Running):

```Bash
kubectl get pods -w


### Testar
Acesse no navegador: http://localhost/wallet/btc-price Retorno esperado: JSON contendo a cotação atual do Bitcoin.