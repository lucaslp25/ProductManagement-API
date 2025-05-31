# ProductManagement-API

[![Java](https://img.shields.io/badge/Java-17+-blue?style=flat&logo=openjdk)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green?style=flat&logo=springboot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue?style=flat&logo=postgresql)](https://www.postgresql.org/)
[![H2 Database](https://img.shields.io/badge/H2-Database-orange?style=flat&logo=h2)](https://www.h2database.com/)
[![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=flat&logo=apache-maven)](https://maven.apache.org/)
[![Hibernate](https://img.shields.io/badge/Hibernate-ORM-59666C?style=flat&logo=hibernate)](https://hibernate.org/)
[![Swagger UI](https://img.shields.io/badge/API%20Doc-Swagger%20UI-85EA2D?style=flat&logo=swagger)](https://swagger.io/)
[![Lombok](https://img.shields.io/badge/Lombok-Code-E15053?style=flat&logo=lombok)](https://projectlombok.org/)
[![JUnit 5](https://img.shields.io/badge/JUnit%205-Tests-25A162?style=flat&logo=junit5)](https://junit.org/junit5/)
[![RESTful API](https://img.shields.io/badge/RESTful-API-black?style=flat&logo=restr)](https://en.wikipedia.org/wiki/Representational_state_transfer)
[![WebClient](https://img.shields.io/badge/WebClient-Spring%20WebFlux-65B203?style=flat&logo=spring)](https://docs.spring.io/spring-framework/reference/web/webflux.html)


## 🚀 Project Overview

This **Product Management API** is a Spring Boot backend that helps you manage products and categories. 
It's got full **CRUD** features and has integration with an external API to perform currency conversion, so users can see product prices in different currency types.

## 💡 Key Features & Technologies
* **Core Functionality**: Full **CRUD operations** for `Product` and `Category` entities.
* **Database Management**: Implemented with **Spring Data JPA** and **Hibernate**.
    * Supports **PostgreSQL** for production and **H2 Database** for tests in memory.
* **External Service Integration**:
    * **Currency Conversion API**: Integration with `ExchangeRate-API.com` using **Spring WebClient** to convert product prices to various currencies.
* **Error Handling**:
    * **Global Exception Handling**: Centralized error management using `@ControllerAdvice` for consistent and user-friendly API error responses. 
    * **Custom Exceptions**: Defined custom exceptions for specific errors.
* **API Documentation**: Automated API documentation provided by **OpenAPI 3 (Swagger UI)** via **Springdoc OpenAPI**, making endpoints easy to explore and test.
* **Utility**: **Project Lombok** for reducing repetitive code.
* **Security Practices**: Use of **Environment Variables** for sensitive configurations (like API keys, db passwords) to prevent exposure in code.

  ## 📐 Architecture

This project uses **Layered Architecture** to ensure clear separation and respecting the responsibilities of each class.

* **Controllers**: Get the enter datas and have all endpoints.
* **Services**: Contain the core business rules and logic. This layer is responsible for taking care of the interaction between the repository and the controllers.
* **Repositories**: The lowest layer of the system. They handle CRUD operations and custom queries using Spring Data JPA.
* **Entities**: Represents the entities of business, they will persists on Data base.
* **DTOs (Data Transfer Objects)**: These are entities that are not allowed in the database, they only carry data between the system, making the system less decoupled and offering other advantages.
* **Integrations (External Services Layer)**: Encapsulate logic for interacting with third-party APIs or external systems.



## 🚀 Getting Started

Follow these steps to get a local copy of the project up and running.

### Prerequisites

* **Java 17+** (Recommended: OpenJDK 21+)
* **Apache Maven 3.6+**
* **PostgreSQL 17+** 
* **An API Key from ExchangeRate-API.com**: Sign up for a free API key [here](https://www.exchangerate-api.com/).

### Installation

1.  **Clone the Repository:**
    ```git
    git clone https://github.com/lucaslp25/ProductManagement-API.git
    ```
2.  **Database Setup:**
    * Ensure your PostgreSQL server is running.
    * Create a new database for the project (example ->  `product_management-API`).
    * Have a SQL file on project for references.

    * Update your `application-dev.properties`  with your PostgreSQL credentials. For local development.
        ```properties
        # (application.dev-properties) - the development properties configuration
        spring.datasource.url=jdbc:postgresql://localhost:5432/product_management-API <-- your database name for this project
        spring.datasource.username=postgres <-- your database username
        spring.datasource.password=your_db_password <-- your database password, in my project i´m using a environment variable for security
    

2.  **API Key Configuration:**
    * This project uses environment variables for sensitive API keys.
    * In `application-dev.properties` you should substitute the ${EXCHANGERATE_API_KEY} for your key.
    * **To get yor Key**
    * 1: ENTER in --> https://www.exchangerate-api.com/
    * 2: Sing up in your account and get up free key! 

 **Optional: H2 In-Memory Database (For Quick Testing)**
 
  * This project is pre-configured with H2 Database for quick local testing, if you don´t want configure a postgres database, follow the next steps for execute the project.
  * In file  `application.properties` you should to switch the `spring.profiles.active=dev` for `spring.profiles.active=test`, the test profile is configurate for H2 database.
  * In file `appllication-test.properties` have my default H2 database configurations:
    
  ```properties
  spring.datasource.driverClassname=org.h2.Driver <-- the driver
  spring.datasource.username=lp  <-- the name of H2 database
  spring.datasource.password=8118  <-- the password of H2 database
  spring.datasource.url=jdbc:h2:mem:lp <--the url of H2 database 

  ```
  if you want can change the H2 database data! (username, and password)
  * Substitute the ${EXCHANGERATE_API_KEY} for your free key. (for external API)
  * Ready! When you execute the project, the Class TestConfig will go populate the H2 database and you can acess searching: [localhost:8080/h2-console](http://localhost:8080/h2-console)
  * A window will appear for you to enter the H2 database credentials, and you will put the username, password, and url thats configurate in `application-test.properties`.


## 🔗 API Endpoints

The API documentation can be accessed via **Swagger UI** once the application is running:
[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)


## 📧 Contact

Lucas Lopes - [lp.skt25@gmail.com](mailto:lp.skt25@gmail.com)

Project Link: [https://github.com/lucaslp25/ProductManagement-API](https://github.com/lucaslp25/ProductManagement-API)

