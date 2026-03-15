# GED Backend

API REST para **Gerenciamento Eletrônico de Documentos (GED)** desenvolvida com **Java e Spring Boot**.

O sistema permite armazenar, versionar e consultar documentos , garantindo controle de acesso através de autenticação e autorização.

O projeto foi desenvolvido com foco em **boas práticas de arquitetura backend**, utilizando:

* Spring Boot
* Spring Security
* JWT
* PostgreSQL
* Maven
* Docker

---

# Arquitetura

O projeto segue uma arquitetura em camadas para separar responsabilidades:

```
Controller → Service → Repository → Database
```

### Controller

Responsável por expor os endpoints REST da aplicação.

Recebe as requisições HTTP e delega a lógica de negócio para os serviços.

Exemplos:

* DocumentController
* AuthController
* UserController
* StudentController

---

### Service

Camada onde ficam as **regras de negócio**.

Responsabilidades:

* Login e geração de token utilizando JWT
* validação de dados
* manipulação de documentos
* controle de versão


---

### Repository

Responsável pelo acesso ao banco de dados.

Utiliza **Spring Data JPA** para simplificar operações CRUD.

Exemplo:

```java
public interface DocumentRepository extends JpaRepository<Document, Long> {
}
```

---

# Estrutura do Projeto

```
ged-backend
│
├── src
│   ├── main
│   │
│   │   ├── java
│   │   │   └── br/com/ged
│   │   │
│   │   │   ├── controllers
│   │   │   │   ├── AuthController
│   │   │   │   ├── DocumentController
│   │   │   │   └── UserController
│   │   │   │
│   │   │   ├── services
│   │   │   │   ├── DocumentService
│   │   │   │   └── UserService
│   │   │   │
│   │   │   ├── repositories
│   │   │   │   ├── DocumentRepository
│   │   │   │   └── UserRepository
│   │   │   │
│   │   │   ├── domains
│   │   │   │   ├── Document
│   │   │   │   ├── DocumentVersion
│   │   │   │   ├── User
│   │   │   │   └── Roles
│   │   │   │
│   │   │   ├── security
│   │   │   │   ├── SecurityConfig
│   │   │   │   ├── SecurityFilter
│   │   │   │   └── JwtService
│   │   │   │
│   │   │   └── GedApplication
│   │   │
│   │   └── resources
│   │       ├── application.properties
│   │       └── migrations
│
├── Dockerfile
├── pom.xml
└── README.md
```

---

# Entidades

## User

Representa os usuários que podem acessar o sistema.

Campos principais:

| Campo    | Descrição         |
|----------|-------------------|
| id       | identificador     |
| username | nome de usuário   ||
| password | senha             |
| profiles | Perfis do usuario |

Relacionamento:

```
User → Roles
```

---

## Roles

Define permissões de acesso.

Exemplos:

* ADMIN
* USER

---


Relacionamento:

```
Document → DocumentVersion
```

---

## DocumentVersion

Controla o versionamento de documentos.

Cada alteração em um documento gera uma nova versão.

Campos principais:

| Campo     | Descrição          |
| --------- | ------------------ |
| id        | identificador      |
| version   | número da versão   |
| filePath  | caminho do arquivo |
| createdAt | data da versão     |

---

# Segurança

O projeto utiliza **Spring Security com autenticação JWT**.

Fluxo de autenticação:

```
Login → geração de token JWT → acesso aos endpoints protegidos
```

O token deve ser enviado no header:

```
Authorization: Bearer TOKEN
```

---

# Como executar o projeto

## Pré-requisitos

* Java 21
* Maven
* PostgreSQL
* Docker (opcional)

---

## Clonar o repositório

```bash
git clone https://github.com/GustavoPinheiro170/ged-backend.git
```

---

## Entrar na pasta

```bash
cd ged-backend
```

---

## Configurar banco de dados

Edite:

```
src/main/resources/application.properties
```

Exemplo:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ged
spring.datasource.username=postgres
spring.datasource.password=postgres
```

---

## Executar aplicação

```bash
mvn spring-boot:run
```

```bash
./mvnw spring-boot:run
```



# Após Inicialização

Será gerado 2 usuarios sendo um ADMIN outro USER, com senha padrão 123456,
também será gerado 3 documentos ficticios para visualização no Dashboard


---

# Documentação da API

Após iniciar a aplicação:

```
http://localhost:8080/swagger-ui.html
```

---

# Exemplos de endpoints

### Login

```
POST /auth/login
```

### Criar usuário

```
POST /users
```

### Upload de documento

```
POST /documents
```

### Download de documento

```
GET /documents/{id}
```

---

# Autor

Gustavo Pinheiro

Projeto desenvolvido para estudo e prática de **desenvolvimento backend com Java e Spring Boot**.
