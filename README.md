# Comunicação API

API REST para agendamento e gerenciamento de comunicações, desenvolvida com Spring Boot e MySQL.
O sistema permite agendar mensagens, consultar status e cancelar comunicações, com envio automático via integração com serviço externo de e-mail.

---

## Tecnologias

- Java 11
- Spring Boot 2.7.0
- Spring Data JPA
- Spring Cloud OpenFeign
- MySQL 8.0
- Lombok
- MapStruct
- Springdoc OpenAPI (Swagger)
- Docker / Docker Compose
- JUnit 5 + Mockito

---

## Como rodar o projeto

### Pré-requisitos
- Docker Desktop instalado e rodando

### Subindo a aplicação

```bash
docker compose up --build
```

A aplicação estará disponível em: `http://localhost:8080`

Documentação Swagger: `http://localhost:8080/swagger-ui/index.html`

> O `--build` é necessário na primeira vez ou após alterações no código.
> Nas execuções seguintes, pode usar apenas `docker compose up`.

---

## Endpoints

### POST /comunicacao/agendar
Agenda uma nova comunicação. O status é definido automaticamente como `PENDENTE`.

**Request Body:**
```json
{
  "dataHoraEnvio": "2026-04-24 13:56:20",
  "nomeDestinatario": "Alan Ferreira",
  "emailDestinatario": "alan@email.com",
  "telefoneDestinatario": "85986546543",
  "mensagem": "Sua mensagem aqui",
  "modoDeEnvio": "EMAIL"
}
```

**Respostas:**
| Status | Descrição |
|--------|-----------|
| `200 OK` | Comunicação agendada com sucesso |
| `400 Bad Request` | JSON inválido ou ausente |
| `409 Conflict` | Já existe uma comunicação com este e-mail |
| `500 Internal Server Error` | Erro interno do servidor |

---

### GET /comunicacao
Busca o status de uma comunicação pelo e-mail do destinatário.

**Query Param:**
- `emailDestinatario` (obrigatório) — E-mail do destinatário

**Exemplo:**
```
GET /comunicacao?emailDestinatario=alan@email.com
```

**Respostas:**
| Status | Descrição |
|--------|-----------|
| `200 OK` | Retorna os dados da comunicação |
| `404 Not Found` | Comunicação não encontrada |

---

### PATCH /comunicacao/cancelar
Cancela uma comunicação agendada, alterando seu status para `CANCELADO`.

**Query Param:**
- `emailDestinatario` (obrigatório) — E-mail do destinatário

**Exemplo:**
```
PATCH /comunicacao/cancelar?emailDestinatario=alan@email.com
```

**Respostas:**
| Status | Descrição |
|--------|-----------|
| `200 OK` | Comunicação cancelada com sucesso |
| `404 Not Found` | Comunicação não encontrada |

---

## Enums

### Modos de Envio (`modoDeEnvio`)
| Valor | Descrição |
|-------|-----------|
| `EMAIL` | Envio por e-mail |
| `SMS` | Envio por SMS |
| `PUSH` | Notificação push |
| `WHATSAPP` | Envio via WhatsApp |

### Status de Envio (`statusEnvio`)
| Valor | Descrição |
|-------|-----------|
| `PENDENTE` | Aguardando envio |
| `ENVIADO` | Comunicação enviada com sucesso |
| `CANCELADO` | Comunicação cancelada |

---

## Envio Automático

A aplicação possui uma tarefa agendada (`CronService`) que executa a cada minuto buscando todas as comunicações com status `PENDENTE` e as envia via integração com um serviço externo de e-mail (`EmailClient` via OpenFeign).

Após o envio, o status é atualizado automaticamente para `ENVIADO`.

A URL do serviço externo é configurada via `application.properties`:
```properties
notificacao.url=http://localhost:8082/email/mensagem
```

> Este serviço depende da aplicação de notificação rodando em paralelo.
> Repositório: [notificacao](https://github.com/AlanF-Oliveira/notificacao)

---

## Testes Unitários

O projeto possui cobertura de testes unitários com JUnit 5 e Mockito nas seguintes camadas:

### ComunicacaoControllerTest
Testa os endpoints da API utilizando `MockMvc` com `standaloneSetup` e `GlobalExceptionHandler` registrado.

| Teste | Descrição |
|-------|-----------|
| `deveAgendarUsuarioComSucesso` | Agendamento com dados válidos retorna `200` |
| `naoDeveAgendarUsuarioCasoJsonNull` | Body ausente retorna `400` sem chamar a service |
| `naoDeveAgenfarCasoEmailExistente` | E-mail duplicado retorna `409` |
| `deveBuscarStatusComunicacaoComSucesso` | Busca por e-mail existente retorna `200` |
| `naoDeveBuscarStatusDaComunicacaoCasoEmailInexistente` | Busca por e-mail inexistente retorna `404` |
| `deveCancelarStatusDaComunicacaoComSucesso` | Cancelamento com e-mail válido retorna `200` |
| `naoDeveCancelarStatusDaComunicacaoCasoEmailInexistente` | Cancelamento com e-mail inexistente retorna `404` |

### ComunicacaoServiceTest
Testa a lógica de negócio com repositório e converter mockados.

| Teste | Descrição |
|-------|-----------|
| `deveAgendarComunicacao` | Fluxo completo de agendamento funciona corretamente |
| `naoDeveSalvarDTONulo` | DTO nulo lança `BusinessException` |
| `naoDeveSalvarCasoEmailExistente` | E-mail duplicado lança `ConflictException` |
| `deveBuscarStatusComunicacao` | Busca por e-mail existente retorna o DTO correto |
| `naoDeveBuscarCasoEmailNull` | E-mail nulo lança `ResourceNotFoundException` |
| `deveAlterarStatusComunicacao` | Status é alterado para `CANCELADO` corretamente |
| `naoDeveaAlterarStatusComunicacaoCasoEmailNulo` | E-mail nulo lança `ResourceNotFoundException` |
| `deveBuscarMensagemPendente` | Busca mensagens com status `PENDENTE` retorna lista correta |
| `deveMarcarComoEnviado` | Status é alterado para `ENVIADO` corretamente |
| `naoDeveMarcarComoEnviadoCasoEmailNull` | E-mail nulo lança `ResourceNotFoundException` |

### ComunicacaoConverterTest
Testa o mapper MapStruct de conversão entre DTOs e entidade.

| Teste | Descrição |
|-------|-----------|
| `deveConverterParaComunicacaoEntity` | Converte `ComunicacaoInDTO` para `ComunicacaoEntity` corretamente |
| `deveConverterParaDTO` | Converte `ComunicacaoEntity` para `ComunicacaoOutDTO` corretamente |
| `deveConverterParaListaDTO` | Converte lista de entidades para lista de DTOs corretamente |

### CronServiceTest
Testa a tarefa agendada de envio de mensagens pendentes.

| Teste | Descrição |
|-------|-----------|
| `deveBuscarPorMensagensPendentes` | Mensagens pendentes são buscadas, enviadas e marcadas como enviadas |

### EmailServiceTest
Testa o serviço de envio via client Feign.

| Teste | Descrição |
|-------|-----------|
| `deveEnviarMensagem` | Client Feign é chamado corretamente ao enviar mensagem |

---

## CI/CD

O projeto utiliza **GitHub Actions** para integração contínua. O pipeline é executado automaticamente nos seguintes eventos:

**Triggers:**
- Push nas branches `main`, `develop` e `feature/**`
- Pull Request para `main` e `develop`

**Etapas do pipeline:**
1. Checkout do código
2. Configuração do JDK 11 (Temurin)
3. Build e execução dos testes com Maven (`mvn clean package`)

O arquivo de configuração está em `.github/workflows/maven.yml`.

---
