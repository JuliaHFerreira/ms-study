# ms-study

Projeto de estudo em **Java 21** focado em construir uma base sólida de **microservices** com **APIs REST CRUD**, **mensageria**, **cache** e **infra local com Docker**.

> ✅ Objetivo: praticar conceitos e integração entre serviços (sincrono + assíncrono), organização de camadas e boas práticas (testes, documentação e configuração).

---

## ✨ O que tem nesse projeto

- **API REST CRUD**
- **Arquitetura em microservices**
- **Mensageria com RabbitMQ**
- **JPA/Hibernate**
- **PostgreSQL**
- **Cache com Redis**
- **Swagger / OpenAPI** (documentação da API)
- **Docker + Docker Compose** (ambiente local completo)
- **Testes com JUnit com mocks**
- **Jacoco** (para ver a porcentagem de teste em código)
- **Lombok**
- **Flyway** (Criar o banco tb_users e deixar populado para testes)

---

### 🔥 Em execução (runtime)

### 👤 User Service
- Expõe endpoints **CRUD REST**
- Persiste e consulta dados no **PostgreSQL** usando **Spring Data JPA/Hibernate** (ORM).
- Estrutura do banco (tabelas/seed) é gerenciada por **Flyway**.
- Usa **Redis** via Spring Cache para acelerar leituras e reduzir acesso ao banco
- Ao criar/atualizar dados relevantes (created, update e deleted), publica **eventos** no **RabbitMQ**


### ✉️ Email Service

- Consome eventos do **RabbitMQ** (ex.: *usuário criado*)
- Processa a mensagem (monta o conteúdo do email/notificação)
- Envia o email (simulado) para o usuário
- Persiste o histórico do envio no **PostgreSQL** (ex.: destinatário, assunto, status, data/hora, payload do evento)

#### Exemplo de email enviado

<img align = "Left" width="420" src="https://media.discordapp.net/attachments/789617283739549756/1458918835217498153/Design_sem_nome_1.png?ex=6961638b&is=6960120b&hm=5caa073d1d9c1888d1f79fb2ddc7214b93b2e967dcb74823499b6ae5abf41163&=&format=webp&quality=lossless" />

<br clear="right" />
<br clear="right" />
<br clear="right" />
<br clear="right" />
<br clear="right" />
<br clear="right" />

### 🗄️ Migrations e dados para teste (Flyway)

- O **Flyway** versiona e aplica as migrations automaticamente para:
    - criar a tabela `tb_users`
    - **popular a base** com dados iniciais (seed) para facilitar testes locais e validações com Redis/cache
- Isso deixa o ambiente reprodutível (toda vez que subir, você tem o schema “no padrão” do projeto).
> Arquivo de migrations: `V1__Init.sql`
`V2__population_base.sql`


### ✅ Qualidade: testes e cobertura (JUnit + mocks + JaCoCo)

- **JUnit** (com mocks) para validar regras de negócio e comportamento isolado
- **JaCoCo** para gerar relatório de cobertura e acompanhar a evolução dos testes
    - ajuda a garantir que o service está realmente coberto e não só “passando teste”


---

## 📌 Nota

Este repositório é exclusivamente para estudos e está em evolução contínua.
