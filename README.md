# comunicacao_api

API REST para agendamento, consulta de status e cancelamento de comunicacoes (email, SMS, push e WhatsApp).

## Stack

- Java 11
- Spring Boot 2.7.0
- Spring Web
- Spring Data JPA
- MySQL
- Lombok
- Springdoc OpenAPI (Swagger UI)
- Maven Wrapper

## Arquitetura atual

O projeto segue separacao por camadas:

- `api`: controllers e DTOs
- `business`: regras de negocio (`service`) e conversao DTO <-> Entity (`converter`)
- `infraestructure`: entidades JPA, enums, repositorio e tratamento global de excecoes

## Requisitos

- JDK 11
- MySQL em execucao local

## Configuracao

Arquivo: `src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/comunicacao1?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=2010
spring.jpa.hibernate.ddl-auto=update
spring.mvc.pathmatch.matching-strategy=ant_path_matcher
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL57Dialect
spring.jpa.properties.hibernate.globally_quoted_identifiers=true
```

> Recomendado: alterar credenciais antes de subir o projeto para qualquer ambiente compartilhado.

## Como rodar

Na raiz do projeto:

```bash
./mvnw spring-boot:run
```

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

## Swagger / OpenAPI

Com a aplicacao rodando:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- JSON OpenAPI: `http://localhost:8080/v3/api-docs`

## Endpoints

Base path: `/comunicacao`

| Metodo | Rota | Descricao |
|---|---|---|
| POST | `/comunicacao/agendar` | Agenda uma nova comunicacao |
| GET | `/comunicacao?emailDestinatario={email}` | Consulta status da comunicacao por email |
| PATCH | `/comunicacao/cancelar?emailDestinatario={email}` | Cancela uma comunicacao existente |

### Exemplo - agendar comunicacao

`POST /comunicacao/agendar`

```json
{
  "dataHoraEnvio": "2026-04-15 22:42:59",
  "nomeDestinatario": "Alan",
  "emailDestinatario": "alan@email.com",
  "telefoneDestinatario": "11999999999",
  "mensagem": "Lembrete da tarefa",
  "modoDeEnvio": "EMAIL"
}
```

## Regras de negocio atuais

- `statusEnvio` e definido no backend como `PENDENTE` no agendamento.
- Nao e permitido cadastrar duas comunicacoes com o mesmo `emailDestinatario`.
- Ao cancelar, o status e alterado para `CANCELADO`.
- Se o email nao for encontrado, a API retorna erro de recurso nao encontrado.

## Enums

### `ModoEnvioEnum`

- `EMAIL`
- `SMS`
- `PUSH`
- `WHATSAPP`

### `StatusEnvioEnum`

- `PENDENTE`
- `ENVIADO`
- `CANCELADO`

## Respostas de erro (handler global)

- `400 Bad Request` - dados invalidos / JSON invalido
- `404 Not Found` - mensagem nao encontrada
- `409 Conflict` - email ja cadastrado
- `502 Bad Gateway` - erro de servico externo

## Testes

```bash
./mvnw test
```

No Windows:

```powershell
.\mvnw.cmd test
```
