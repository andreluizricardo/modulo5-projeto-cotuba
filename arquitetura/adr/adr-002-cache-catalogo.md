# ADR 002: Introdução de Cache em memória para o Catálogo de E-books

## Status 

Em proposta.

## Contexto

Lentidão e timeouts na plataforma de geração de ebook.

## Opções Consideras

1. **Scale Up do Banco de dados (Aumentar Máquina):** Comprar um servidor de banco de dados com mais CPU e mais RAM.

2. **Materialized Views no Banco Relacional:** Pré-calcular os resultados no próprios banco de dados.

3. **Introdução de Cache em memória:** Utilizar um banco de dados chave-valor em memória (como Redis ou Memcached).

## Decisão

Decidido opção 3 (Introdução de Cache em memória)
AWS Elasticache com Redis como mecanismo de cache

## Consequências

### Positivas
* **Queda drástica na latência**
* **Aliviar banco de dados principal**
* **Escalabilidade de leitura**: Escalar instâncias do Redis.

### Negativas

* **Dados desatuaqlizados (Stale data)**
* **Complexidade na infra**

