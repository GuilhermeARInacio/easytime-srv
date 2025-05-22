# Easytime - Sistema de Marcação de Ponto (SRV)

## Descrição do Projeto
Easytime é um sistema de marcação de ponto desenvolvido com uma arquitetura **BFF (Back For Front)**. Este projeto é responsável por gerenciar a autenticação de usuários e fornecer um **token JWT** para acesso seguro às funcionalidades do sistema. O seguinte repositório descreve a camada de serviço do sistema Easytime.

O projeto foi desenvolvido em **Java** utilizando o framework **Spring Boot** e implementa autenticação e segurança com **Spring Security**.

## Funcionalidades
- **Autenticação de Usuário**:
    - Rota de login que recebe as credenciais (usuário e senha).
    - Validação de senha com regras específicas.
- **CRUD de Usuário**:
    - Rota de cadastro de um novo usuário, deve ser feito por um usuario já cadastrado.
    - Rota de listagem de todos os usuários cadastrados.
    - Rota que retorna um usuário de acordo com o ID informado.
    - Rota para excluir um usuário de acordo com o ID informado.
    - Validação de campos com regras específicas.
- **Troca de senha**:
    - Rota para enviar um código de validação para um email válido.
    - Rota para validar o código enviado e permitir a troca de senha.
- **Batimento de ponto**:
    - Rota de batimento de ponto, onde o usuário pode registrar seu ponto de entrada ou saída.
- **Consulta de ponto**:
    - Rota de consulta de ponto, onde o usuário pode informar login e um periodo de tempo para visualizar os pontos registrados no periodo.
- **Alteração de registro de ponto**:
    - Rota de alteração de registro de ponto, onde o usuário informa o login e o ID do registro e pode alterar os horários de entrada ou saída ou a data.

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
- `src/main/java/easytime/srv/api/infra`: Contém a configuração de segurança, exceptions personalizadas e configurações do SpringDoc.
- `src/main/java/easytime/srv/api/tables`: Contém as entidades do banco de dados.
- `src/main/java/easytime/srv/api/utils`: Contém classes utilitárias, como o gerador de senhas.

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

### CRUD de Usuários `/users`
**PUT** `/create`

- **Descrição**: Cria um usuário no sistema
    - **Request Body**:
      ```json
      {
          "nome": "string",
          "email": "string",
          "login": "string",
          "password": "string",
          "sector": "string",
          "jobTitle": "string",
          "role": "string",
          "isActive": true,
          "valid": true
      }
      ```
- **Response**:
    - **201 Created**: Cria o usuário.
      ```json
      {
          "nome": "string",
          "email": "string",
          "login": "string",
          "password": "string",
          "sector": "string",
          "jobTitle": "string",
          "role": "string",
          "isActive": true,
          "valid": true
      }
      ```
    - **400 Bad Request**: Retorna uma mensagem de erro caso alguma informação seja inválida e não aceita pelo sistema.

**GET** `/list`

- **Descrição**: Retorna uma lista com todos os usuários cadastrados no sistema.

- **Response**:
    - **200 OK**: Retorna lista dos usuários.
      ```json
      [{
          "nome": "string",
          "email": "string",
          "login": "string",
          "password": "string",
          "sector": "string",
          "jobTitle": "string",
           "role": "string",
          "isActive": true,
          "valid": true
      },
      {
          "nome": "string",
          "email": "string",
          "login": "string",
          "password": "string",
          "sector": "string",
          "jobTitle": "string",
          "role": "string",
          "isActive": true,
          "valid": true
      }]
      ```
    - **404 Not Found**: Retorna uma mensagem de erro dizendo que o sistema não achou nenhum usuário.

**GET** `/getById/{id}`

- **Descrição**: Envia um id específico e retorno o usuário correspondente.
- **Path Variable**:
   ```
    id: Integer
   ```

- **Response**:
    - **200 OK**: Retorna usuário.
      ```json
      {
          "nome": "string",
          "email": "string",
          "login": "string",
          "password": "string",
          "sector": "string",
          "jobTitle": "string",
          "role": "string",
          "isActive": true,
          "valid": true
      }
      ```
    - **404 Not Found**: Retorna uma mensagem de erro dizendo que o sistema não achou nenhum usuário.

**DELETE** `/delete/{id}`
- **Descrição**: Remove um usuário baseado em um id passado.
- **Path Variable**:
     ```
      id: Integer
     ```
- **Response**:
    - **200 OK**: Remove o usuário do sistema.
    - **404 Not Found**: Retorna uma mensagem de erro dizendo que o sistema não achou nenhum usuário.

### Redefinição de senha
**POST** `/sendEmail`

- **Descrição**: Envia uma requisição com um email, o sistema envia um email com código de validação para começar o processo de redefinição de senha.
- **Request Body**:
  ```json
  {
    "email": "string"
  }
  ```
- **Response**:
    - **200 Ok**: Email enviado.
      ```body
      "Email enviado, verifique sua caixa de entrada ou spam."
      ```
    - **404 Not Found**: Retorna uma mensagem de erro quando sistema não acha o endereço de email enviado pelo usuário.

**POST** `/redefine-senha`

- **Descrição**: Envia uma requisição com um email, o sistema envia um email com código de validação para começar o processo de redefinição de senha.
    - **Request Body**:
      ```json
      {
          "code": "string",
          "email": "string",
          "senha": "string"
      }
      ```
- **Response**:
    - **200 Ok**: Senha redefinida.
      ```body
      "Senha redefinida com sucesso."
      ```
    - **404 Not Found**: Retorna uma mensagem de erro quando sistema não acha o código de validação de senha enviado pelo usuário.
    - **401 Unauthorized**: Retorna uma mensagem de erro quando o código enviado não é válido ou não existe.
    - **400 Bad Request**: Retorna uma mensagem de erro quando a senha enviada pelo usuário não é válida.

### Batimento de ponto
**POST** `/ponto`
- **Descrição**: Realiza o batimento de ponto do usuário.
    - **Request Body**:
      ```json
      {
        "usuario": "string"
      }
      ```
      - **Response**:
          - **200 OK**: Retorna o ponto batido.
            ```json
            {
                "login": "string",
                "data": "2025-05-14",
                "horarioBatida": "12:00:00",
                "status": "PENDENTE"
            }
            ```
          - **400 Bad Request**: Retorna uma mensagem de erro quando o usuário não é válido ou não existe.
          - **401 Unauthorized**: Retorna uma mensagem de erro quando o usuário não está autenticado.

### Deletar registro de ponto
**DELETE** `/ponto/{id}`
- **Descrição**: Deleta o registro de ponto de acordo com o ID informado.
    - **Response**:
        - **200 OK**: Retorna uma mensagem de sucesso.
        - **400 Bad Request**: Retorna uma mensagem de erro caso o ID informado não exista.
        - **401 Unauthorized**: Retorna uma mensagem de erro caso o token esteja inválido ou vazio.

### Consulta de ponto
**POST** `/ponto/consulta`
- **Descrição**: Lista os registros de pontos de um usuario especifico em um periodo de tempo.
    - **Request Body**:
      ```json
      {
          "login": "string",
          "dtInicio": "2025-01-01",
          "dtFinal": "2025-01-10"
      }
      ```
        - **Response**:
            - **200 OK**: Retorna uma lista de pontos.
              ```json
              [
                  {
                      "id": 1,
                      "data": "2025-01-02",
                      "horasTrabalhadas": "1",
                      "entrada1": "08:02:37",
                      "saida1": "12:01:23",
                      "entrada2": "13:00:00",
                      "saida2": "17:01:05",
                      "entrada3": null,
                      "saida3": null,
                      "status": "PENDENTE"
                  },
                  {
                      "id": 2,
                      "data": "2025-01-03",
                      "horasTrabalhadas": "1",
                      "entrada1": "08:00:08",
                      "saida1": "12:17:59",
                      "entrada2": "13:01:10",
                      "saida2": "17:10:02",
                      "entrada3": null,
                      "saida3": null,
                      "status": "PENDENTE"
                  }
              ]
              ```
            - **404 Bad Request**: Retorna uma mensagem de erro quando não há registros no período.
            - **401 Unauthorized**: Retorna uma mensagem de erro quando o usuário não está autenticado ou login não existe.

### Alterar registro de ponto
**PUT** `/ponto/alterar`
- **Descrição**: Realiza a alteração do ponto do usuário.
    - **Request Body**:
      ```json
      {
          "login": "string",
          "idPonto": 1,
          "data": "2025-01-01",
          "entrada1": "08:00:00",
          "saida1": "12:00:00",
          "entrada2": "13:00:00",
          "saida2": "17:00:00",
          "entrada3": null,
          "saida3": null
      }
      ```
        - **Response**:
            - **200 OK**: Retorna o ponto alterado.
              ```json
              {
                  "id": 7,
                  "data": "2025-01-01",
                  "entrada1": "08:00:00",
                  "saida1": "12:00:00",
                  "entrada2": "13:00:00",
                  "saida2": "17:00:00",
                  "entrada3": null,
                  "saida3": null,
                  "status": "PENDENTE"
              }
              ```
            - **400 Bad Request**: Retorna uma mensagem de erro quando os campos estão inválidos.
            - **401 Unauthorized**: Retorna uma mensagem de erro quando o usuário não está autenticado.

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
