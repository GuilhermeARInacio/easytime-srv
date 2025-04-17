[//]: # (BackEnd For Frontend &#40;BFF&#41; do projeto de gerenciamento de ponto eletrônico EasyTime.)
# Easytime - Sistema de Marcação de Ponto (SRV)

## Descrição do Projeto
Easytime é um sistema de marcação de ponto desenvolvido com uma arquitetura **BFF (Back For Front)**. Este projeto é responsável por gerenciar a autenticação de usuários e fornecer um **token JWT** para acesso seguro às funcionalidades do sistema. O seguinte repositório descreve a camada de serviço do sistema Easytime.

O projeto foi desenvolvido em **Java** utilizando o framework **Spring Boot** e implementa autenticação e segurança com **Spring Security**.

## Funcionalidades
- **Autenticação de Usuário**:
    - Rota de login que recebe as credenciais (usuário e senha).
    - Validação de senha com regras específicas.

## Tecnologias Utilizadas
- **Java 17**
- **Spring Boot**
- **Spring Security**
- **Maven** (gerenciador de dependências)
- **JWT** (JSON Web Token)

## Estrutura do Projeto
- `src/main/java/easytime/srv/api/controller`: Contém os controladores REST, como o controlador de autenticação.
- `src/main/java/easytime/srv/api/model`: Contém os DTOs (Data Transfer Objects) utilizados para transferência de dados.
- `src/main/java/easytime/srv/api/service`: Contém as regras de negócio e serviços.
- `src/main/java/easytime/srv/api/validacoes`: Contém as validações específicas, como validação de senha.

## Endpoints

### Login
**POST** `/login`

- **Descrição**: Realiza a autenticação do usuário e retorna um token JWT.
- **Request Body**:
  ```json
  {
    "usuario": "string",
    "senha": "string"
  }
  ```
- **Response**:
    - **200 OK**: Retorna o token JWT.
      ```json
      {
        "token": "string"
      }
      ```
    - **400 Bad Request**: Retorna uma mensagem de erro caso as credenciais sejam inválidas.
    - **401 Unauthorized**: Retorna uma mensagem de erro caso o usuário e senha não existam no banco de dados.

## Regras de Validação de Senha
- Não pode estar vazia ou em branco.
- Deve ter no mínimo 8 caracteres.
- Deve conter pelo menos:
    - Uma letra.
    - Um número.
    - Um caractere especial (ex.: `!@#$%^&*()`).

## Como Executar o Projeto
1. Certifique-se de ter o **Java 17** e o **Maven** instalados.
2. Clone o repositório:
   ```bash
   git clone <URL_DO_REPOSITORIO>
   ```
3. Navegue até o diretório do projeto:
   ```bash
   cd easytime
   ```
4. Execute o projeto:
   ```bash
   mvn spring-boot:run
   ```
5. O projeto estará disponível em: `http://localhost:8080`.

## Testes
Para executar os testes, utilize o comando:
```bash
mvn test
```
