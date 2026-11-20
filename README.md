# Restaurante API

API REST para gerenciar o cardápio e os pedidos de um restaurante.

## Problema e solução

Restaurantes precisam manter categorias e pratos organizados, controlar quais itens estão disponíveis e calcular pedidos sem depender de operações manuais. Esta API centraliza essas informações em serviços reutilizáveis, permitindo que aplicações web, aplicativos móveis ou outros sistemas consultem o cardápio e registrem pedidos por JSON.

As regras de negócio impedem pedidos com pratos indisponíveis e calculam automaticamente o valor total a partir do preço atual e da quantidade. Os itens de pedido também recebem automaticamente o preço do prato cadastrado.

## Principais funcionalidades

- CRUD de categorias, pratos e pedidos.
- Cadastro e exclusão de itens de pedido.
- Consulta de todos os pratos ou somente dos disponíveis.
- Relacionamentos entre categorias, pratos, pedidos e itens.
- Validação dos dados recebidos.
- Erros padronizados em JSON.
- Documentação interativa com Swagger/OpenAPI.

## Tecnologias

Java 17, Spring Boot 4.1.2, Spring Web MVC, Spring Data JPA, Jakarta Bean Validation, PostgreSQL, Lombok, Springdoc OpenAPI e Maven.

## Executar localmente

1. Instale o JDK 17 e o PostgreSQL.
2. Crie o banco de dados:

   ```sql
   CREATE DATABASE restaurante;
   ```

3. Configure as variáveis de ambiente, se os valores locais forem diferentes dos padrões:

   ```powershell
   $env:DB_URL="jdbc:postgresql://localhost:5432/restaurante"
   $env:DB_USERNAME="postgres"
   $env:DB_PASSWORD="sua-senha"
   ```

4. Na pasta que contém o `pom.xml`, execute:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

O Hibernate cria ou atualiza as tabelas ao iniciar.

## Testes automatizados

Com o PostgreSQL e o banco `restaurante` disponíveis, execute:

```powershell
.\mvnw.cmd test
```

A suíte verifica a inicialização da aplicação, validações HTTP, códigos `201`, `204`, `400`, `404` e `422`, cálculo automático do pedido, bloqueio de pratos indisponíveis, associação de categorias e definição automática do preço dos itens. Os testes de controller e service usam mocks e não inserem nem removem dados do banco.

| Recurso | Endereço local |
| --- | --- |
| Interface web | http://localhost:8080/ |
| Swagger UI | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/v3/api-docs |

## Endpoints

Todas as rotas usam o prefixo `/api`. Requisições com corpo devem enviar `Content-Type: application/json`.

| Método | Rota | Operação |
| --- | --- | --- |
| GET, POST | `/api/categorias` | Listar ou cadastrar categorias |
| GET, PUT, DELETE | `/api/categorias/{id}` | Consultar, atualizar ou excluir categoria |
| GET, POST | `/api/pratos` | Listar ou cadastrar pratos |
| GET | `/api/pratos/disponiveis` | Listar pratos disponíveis |
| GET, PUT, DELETE | `/api/pratos/{id}` | Consultar, atualizar ou excluir prato |
| GET, POST | `/api/pedidos` | Listar ou cadastrar pedidos |
| GET, PUT, DELETE | `/api/pedidos/{id}` | Consultar, atualizar ou excluir pedido |
| GET, POST | `/api/itens-pedido` | Listar ou cadastrar itens |
| DELETE | `/api/itens-pedido/{id}` | Excluir item |

## Exemplos de requisições e respostas

### Cadastrar categoria

`POST /api/categorias`

```json
{
  "nome": "Massas",
  "descricao": "Pratos à base de massa"
}
```

Resposta `201 Created`:

```json
{
  "id": 1,
  "nome": "Massas",
  "descricao": "Pratos à base de massa"
}
```

### Cadastrar prato

`POST /api/pratos`

```json
{
  "nome": "Lasanha à bolonhesa",
  "descricao": "Lasanha com molho de carne e queijo",
  "preco": 32.90,
  "disponivel": true,
  "categoria": { "id": 1 }
}
```

Resposta `201 Created`:

```json
{
  "id": 1,
  "nome": "Lasanha à bolonhesa",
  "descricao": "Lasanha com molho de carne e queijo",
  "preco": 32.90,
  "disponivel": true,
  "categoria": {
    "id": 1,
    "nome": "Massas",
    "descricao": "Pratos à base de massa"
  }
}
```

### Cadastrar pedido

`POST /api/pedidos`

```json
{
  "prato": { "id": 1 },
  "quantidade": 2
}
```

O servidor consulta o prato, verifica sua disponibilidade e calcula o total. Resposta `201 Created`:

```json
{
  "id": 1,
  "data": "2026-11-20T19:30:00",
  "status": "ABERTO",
  "valorTotal": 65.80,
  "prato": {
    "id": 1,
    "nome": "Lasanha à bolonhesa",
    "preco": 32.90,
    "disponivel": true,
    "categoria": { "id": 1, "nome": "Massas" }
  },
  "quantidade": 2
}
```

### Cadastrar item de pedido

`POST /api/itens-pedido`

```json
{
  "pedido": { "id": 1 },
  "prato": { "id": 1 },
  "quantidade": 2
}
```

O `precoUnitario` é preenchido automaticamente com o preço cadastrado do prato.

### Resposta de erro

Exemplo de `404 Not Found`:

```json
{
  "timestamp": "2026-11-20T19:30:00",
  "status": 404,
  "erro": "Recurso não encontrado",
  "mensagem": "Prato com ID 99 não encontrado",
  "campos": {}
}
```

## Códigos HTTP

| Código | Significado |
| --- | --- |
| `200 OK` | Consulta ou atualização realizada |
| `201 Created` | Cadastro realizado |
| `204 No Content` | Exclusão realizada |
| `400 Bad Request` | Dados inválidos |
| `404 Not Found` | Registro não encontrado |
| `409 Conflict` | Operação viola relacionamento ou integridade dos dados |
| `422 Unprocessable Entity` | Regra de negócio violada |
| `500 Internal Server Error` | Erro inesperado no servidor |

## Validações principais

- Nome da categoria: 2 a 60 caracteres.
- Nome do prato: 2 a 100 caracteres.
- Preço do prato: mínimo de `0.01`.
- Quantidade do pedido ou item: mínimo de `1`.
- Status aceitos: `ABERTO`, `PREPARANDO`, `CONCLUIDO` e `CANCELADO`.

## Estrutura do projeto

| Pasta | Responsabilidade |
| --- | --- |
| `controller` | Endpoints, códigos HTTP e validação das requisições |
| `service` | Operações e regras de negócio |
| `repository` | Persistência com Spring Data JPA |
| `model` | Entidades e relacionamentos |
| `exception` | Tratamento e padronização dos erros |
| `config` | Configuração do OpenAPI/Swagger |
| `src/main/resources/static` | Interface web opcional |
