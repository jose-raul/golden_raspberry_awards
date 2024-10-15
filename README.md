# Desafio Outsera

Este é um projeto desenvolvido com Spring Boot 3.3.4 utilizando Java 21. A aplicação expõe um endpoint que calcula os produtores com os maiores e menores intervalos entre vitórias consecutivas no prêmio de pior filme. O projeto inclui testes de integração para garantir o correto funcionamento da aplicação em diferentes cenários.

## Sumário
- [Pré-requisitos](#pré-requisitos)
- [Configuração do Projeto](#configuração-do-projeto)
  - [Clonando o Repositório](#clonando-o-repositório)
  - [Compilando o Projeto](#compilando-o-projeto)
- [Executando a Aplicação](#executando-a-aplicação)
- [Executando os Testes de Integração](#executando-os-testes-de-integração)
  - [Pré-requisitos para os Testes](#pré-requisitos-para-os-testes)
  - [Executando os Testes](#executando-os-testes)
- [Documentação da API](#documentação-da-api)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)

## Pré-requisitos

Antes de começar, certifique-se de ter as seguintes ferramentas instaladas em seu ambiente:

- **Java 21**
- **Maven 3.6** ou superior
- **Git** (opcional, para clonar o repositório)

## Configuração do Projeto

### Clonando o Repositório

Para obter o código-fonte do projeto, execute os seguintes comandos no terminal:

```
git clone https://github.com/seu-usuario/seu-repositorio.git
cd seu-repositorio
```

## Compilando o Projeto
Na raiz do projeto, execute o seguinte comando:
```
mvn clean install
```
Este comando realizará as seguintes ações:

- Baixar todas as dependências necessárias.
- Compilar o código-fonte.
- Executar os testes unitários e de integração automaticamente.

## Executando a Aplicação
Para iniciar a aplicação, execute o comando:
```
mvn spring-boot:run
```
A aplicação estará disponível em: http://localhost:8080

## Importação Automática do CSV
Ao iniciar, a aplicação importa automaticamente os dados do arquivo `movielist.csv`, que está localizado em `src/main/resources`. Este arquivo contém a lista de filmes que será processada pela aplicação. 

## Executando os Testes de Integração
### Pré-requisitos para os Testes
Os testes de integração estão localizados em `src/test/java` e verificam o comportamento da aplicação em diversos cenários. Os arquivos CSV de teste estão localizados em `src/test/resources`.

Durante os testes, o perfil `test` é ativado.

### Executando os Testes
Para executar todos os testes (unitários e de integração), utilize o comando:
```
mvn test
```
Ou, para executar apenas os testes de integração:
```
mvn -Dtest=AwardControllerIntegrationTest test
```
## Documentação da API
A aplicação utiliza o Springdoc OpenAPI para gerar a documentação interativa da API. Após iniciar a aplicação, a documentação pode ser acessada em:
http://localhost:8080/swagger-ui.html

## Tecnologias Utilizadas
- Java 21
- Spring Boot 3.3.4
- Spring Web
- Spring Data JPA
- Hibernate
- H2 Database (para testes)
- Maven
- JUnit 5
- MockMvc
- Hamcrest
- Jakarta Persistence API 3.1.0
- Springdoc OpenAPI 2.1.0
