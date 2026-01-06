# ms-study

Projeto de estudo em **Java 21** focado em construir uma base sólida de **microservices** com **APIs REST CRUD**, **mensageria**, **cache** e **infra local com Docker**.

> ✅ Objetivo: praticar conceitos e integração entre serviços (sincrono + assíncrono), organização de camadas e boas práticas (testes, documentação e configuração).

---

## ✨ O que tem nesse projeto

- **API REST CRUD** (Spring Boot)
- **Arquitetura em microservices** (ex.: `user` e `email`)
- **Mensageria com RabbitMQ** (eventos assíncronos)
- **PostgreSQL + JPA/Hibernate**
- **Cache com Redis** (Spring Cache)
- **Swagger / OpenAPI** (documentação da API)
- **Docker + Docker Compose** (ambiente local completo)
- **Testes com JUnit** (e mocks quando necessário)
- **Lombok** (redução de boilerplate)

---

## 🧱 Visão geral da arquitetura (resumo)

Fluxo típico (exemplo didático):

- Serviço **User** expõe endpoints CRUD
- Ao criar/atualizar algo relevante, publica um **evento** no **RabbitMQ**
- Serviço **Email** consome a mensagem e processa (ex.: “simular envio de email” / registrar envio / logs)
- **Redis** acelera leituras (cache) e reduz acesso ao banco
- **PostgreSQL** armazena os dados persistentes


---

📨 Mensageria (RabbitMQ)

O objetivo é praticar comunicação assíncrona entre microserviços, reduzindo acoplamento.

Boas práticas que você pode aplicar aqui:

- enviar eventos (“UserCreated”, “UserUpdated”, “UserDelete”)
- payloads pequenos e objetivos
- retries / DLQ (em evolução, se quiser praticar)

- ---

📌 Nota

Este repositório é exclusivamente para estudos e está em evolução contínua.
