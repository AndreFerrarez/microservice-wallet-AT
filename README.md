# Microsserviços Reativos com Spring Boot e Kubernetes

Este projeto implementa uma arquitetura de microsserviços para gestão de carteira de criptomoedas, demonstrando comunicação entre serviços, persistência reativa (Non-blocking I/O) e orquestração em nuvem.

## 🏛️ Arquitetura do Sistema

O sistema é composto por 3 containers orquestrados via Kubernetes:

1.  **Wallet Service (Consumidor):**
    * Microsserviço principal (API).
    * Gerencia o saldo e persiste transações.
    * Consome o *Quotation Service* via HTTP (WebClient).
    * Acessível via LoadBalancer na porta **80**.

2.  **Quotation Service (Provedor):**
    * Microsserviço Proxy.
    * Consulta valores reais de criptomoedas em APIs externas (CoinGecko).
    * Acessível internamente no cluster.

3.  **Database Service (Persistência):**
    * Banco de dados **PostgreSQL 15**.
    * Gerenciado via **Flyway** (migrações) e acessado via driver **R2DBC**.

---

## 🛠️ Tecnologias Utilizadas

* **Java 17** & **Spring Boot 3.3.x**
* **Spring WebFlux** (Netty Server)
* **Spring Data R2DBC** & **PostgreSQL**
* **Docker** & **Kubernetes**
* **Testcontainers** & **Mockito**
* **Flyway Migration**

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
* Java 17+
* Maven
* Docker Desktop (com Kubernetes habilitado e rodando)

### Passo 1: Construir as Imagens Docker (Build)
Como o Kubernetes está configurado para usar imagens locais (`imagePullPolicy: Never`), é **obrigatório** gerar as imagens antes de rodar o deploy.

Abra o terminal na raiz do projeto (`AT`) e execute:

```bash
# 1. Build da Wallet
cd crypto-wallet-reactive
mvn clean package -DskipTests
docker build -t crypto-wallet:latest .

# 2. Build da Cotação
cd ../crypto-quotation
mvn clean package -DskipTests
docker build -t crypto-quotation:latest .

# Voltar para a raiz
cd ..

```

### Passo 2: Deploy no Kubernetes (Orquestração)

Com as imagens criadas, suba toda a infraestrutura com um único comando:

Bash

```
kubectl apply -f k8s-full-deployment.yaml

```

### Passo 3: Monitorar a Inicialização

Aguarde até que todos os pods estejam com status `Running`:

Bash

```
kubectl get pods -w

```

* * * * *

🧪 Testando a Aplicação (Endpoints)
-----------------------------------

A aplicação estará disponível em `http://localhost` (Porta 80).

### 1\. Consultar Cotação (Teste de Integração entre Serviços)

Verifica se a Wallet consegue conversar com o serviço de Cotação.

-   **URL:** `http://localhost/wallet/btc-price`

-   **Método:** `GET`

-   **Retorno esperado:** JSON com o valor do Bitcoin.

### 2\. Criar uma Transação (Teste de Persistência)

Grava um depósito no Banco de Dados PostgreSQL.

-   **URL:** `http://localhost/wallet`

-   **Método:** `POST`

-   **Header:** `Content-Type: application/json`

-   **Body:**

JSON

```
{
  "tipo": "DEPOSITO",
  "valor": 500.00,
  "moeda": "BRL"
}

```

**Comando cURL para teste rápido:**

Bash

```
curl -X POST http://localhost/wallet\
-H "Content-Type: application/json"\
-d '{"tipo": "DEPOSITO", "valor": 500.00, "moeda": "BRL"}'

```

### 3\. Listar Transações

Confirma que os dados foram salvos e podem ser lidos.

-   **URL:** `http://localhost/wallet`

-   **Método:** `GET`

* * * * *

✅ Evidências de Testes (QA)
---------------------------

O projeto garante cobertura de testes superior a 80% utilizando:

-   **Testes Unitários:** Com `@WebFluxTest` e `@MockBean` para isolar a camada web.

-   **Testes de Integração:** Com `Testcontainers` para validar o fluxo real de banco de dados.

Para rodar os testes, execute na pasta de cada microsserviço:

Bash

```
mvn clean test

```

* * * * *

🧹 Como Parar o Projeto
-----------------------

Para remover os serviços do Kubernetes e liberar recursos:

Bash

```
kubectl delete -f k8s-full-deployment.yaml

```

👤 Autor
--------

**André Augusto Ferrarez** Desenvolvido para o Assessment de Microsserviços e DevOps.