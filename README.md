# Baozi Store – API REST

Trabalho de Desenvolvimento Web Back-End (UNINTER).
API REST com **Java 17 + Spring Boot 3 + Spring Data JPA + H2/MySQL**.

## Como executar

Pré-requisitos: JDK 17+ (Maven já vem embutido via IDE; ou instale o Maven).

```bash
mvn spring-boot:run
```

Ou abra a pasta no IntelliJ / Eclipse / VS Code (STS) e rode a classe `BaoziStoreApplication`.

A API sobe em `http://localhost:8081`.

## Banco de dados

* **Padrão: H2** (arquivo `./data/baozidb`) – não precisa instalar nada.
  Console: `http://localhost:8081/h2-console` (JDBC URL `jdbc:h2:file:./data/baozidb`, user `sa`, senha vazia).
* **MySQL (opcional):** veja o bloco comentado em `src/main/resources/application.properties`.

## Entidades

| Entidade | Campos |
|----------|--------|
| Produto  | id (Long), nome (String), preco (BigDecimal), estoque (Boolean) |
| Cliente  | id (Long), nome (String), clienteDesde (LocalDate) |
| Pedido   | id (Long), cliente (→ Cliente / coluna `cliente_id`), produto (→ Produto / coluna `produto_id`), quantidade (Integer) |

## Endpoints

Para `/clientes`, `/produtos` e `/pedidos`:

| Método | Rota | Descrição |
|--------|------|-----------|
| POST   | `/{recurso}`      | Criar |
| GET    | `/{recurso}/{id}` | Consultar por ID |
| GET    | `/{recurso}`      | Listar todos |
| PUT    | `/{recurso}/{id}` | Atualizar (opcional) |
| DELETE | `/{recurso}/{id}` | Apagar |

## Exemplos de JSON

**POST /clientes**
```json
{ "nome": "Heleno Bins 4087639", "clienteDesde": "2026-03-15" }
```
**POST /produtos**
```json
{ "nome": "Baozi Tradicional de Carne Suína", "preco": 12.50, "estoque": true }
```
**POST /pedidos**
```json
{ "cliente": { "id": 1 }, "produto": { "id": 1 }, "quantidade": 4 }
```

Importe `postman/BaoziStore.postman_collection.json` no Postman para testar tudo.

## Estrutura

```
src/main/java/br/com/baozistore
├── BaoziStoreApplication.java
├── model        (Cliente, Produto, Pedido)
├── repository   (ClienteRepository, ProdutoRepository, PedidoRepository)
└── controller   (ClienteController, ProdutoController, PedidoController)
```
