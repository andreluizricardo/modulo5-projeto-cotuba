# ADR 001: Geração de ebooks via Mensageria Assincrona

## Status 

Em proposta.

## Contexto

Lentidão e timeouts na plataforma de geração de ebook.

## Opções Consideras

1. **Manter sincrono e ter escalabilidade horizontal**: adicionar mais servidores. Apenas adiaria o gargalo,
threads continuariam sendo desperdiçadas aguardando a geração dos ebooks. 

2. **Mensageria com filas assincrona**: API principal atua como um publisher de um comando de geração de ebooks
(`GeracaoEbookCommand`) para uma fila no nosso Message Broker.

## Decisão

Decidido pela opção 2 (Mensageria com filas assincrona)

Será indroduzida um Message Broker na infraestrutura do projeto. Message broker escolhido AWS SQS

## Consequências

### Positivas

- Alta disponibilidade e vazão

- Escalabilidade horizontal

### Negativas

- Complexidade na infra

- Mudança na UX
