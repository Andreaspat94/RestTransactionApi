# RestTransactionAPI App

This is a Spring Boot application that provides CRUD operations, backed by a MySQL database. The application is Dockerized, making it easy to deploy using Docker Compose.

## Prerequisites
Before you begin, ensure you have met the following requirements:
* Docker and Docker Compose installed on your local machine.
    * [Docker Installation Guide](https://docs.docker.com/engine/install/)
    * [Docker Compose Installation Guide](https://docs.docker.com/compose/install/)
* Java Development Kit (JDK) 22 if you want to run the application without Docker.
    * [JDK Installation Guide](https://docs.oracle.com/en/java/javase/22/install/overview-jdk-installation.html)

## Installation
*1. Clone the Repository:*
```
git clone https://github.com/Andreaspat94/RestTransactionApi.git
cd RestTransactionApi
```
*2. Build JAR:*
```
mvn clean package
```
*3. Build the Docker images:*
* If you have not build the Docker images yet, you can do so by running:
  ```
  docker-compose up --build
  ```

## Running the Application
### Using Docker Compose (Recommended)
*1. Start the Application:*

* In the root directory of the project, run:
  ```
  docker-compose up
  ```
    * This will start both Spring Boot application and the MySQL database in separate containers.

*2. Access the Application:*

* Once the containers are running, the application should be accessible at `http://localhost:8080`.

*3. Stopping the Application:*

* To stop and remove the containers, run:
  ```
  docker-compose down
  ```

### Running Locally (Without Docker)
If you prefer to run the application without Docker:

*1. Ensure MySQL is running:*
* Make sure you have MySQL running locally with the necessary database and user set up.

*2. Configure the Application:*

* Update the `application.properties` file with your local database credentials.

*3. Run the Application with Maven:*

* Run the following command if you are using Linux/Mac:
   ```
   ./mvnw spring-boot:run
   ```

* Or, if you are on Windows Command Prompt:

   ```
   mvnw.cmd spring-boot:run
   ```

This command will compile and run your Spring Boot application.

*4. Access the Application:*
* Once the containers are running, the application should be accessible at `http://localhost:8080`.

*5. Stopping the Application:*
* To stop the application, you can simply press `Ctrl + C` in the terminal where the application is running.

## API Documentation

This application is fully documented with Swagger. To explore and test the API, you can access the Swagger UI once the application is running:

- **[Swagger UI](http://localhost:8080/swagger-ui/index.html#/)**: `http://localhost:8080/swagger-ui/index.html#/`

Swagger provides a comprehensive and interactive interface for all available endpoints, including request and response formats.


## Environment Variable

These are the key environment variable used in the application. If using Docker Compose these are configured in the `docker-compose.yml` file:

* `SPRING_DATASOURCE_URL`: The JDBC URL for connecting to the MySQL database.
* `SPRING_DATASOURCE_USERNAME`: The database username.
* `SPRING_DATASOURCE_PASSWORD`: The database password.
* `MYSQL_ROOT_PASSWORD`: The root password for MySQL.
* `MYSQL_DATABASE`: The name of the database to create in MySQL.
* `MYSQL_USER`: The MySQL user that the application will connect as.
* `MYSQL_PASSWORD`: The password for the MYSQL_USER.

## Built With
* Java 22.0.1 - The programming language used to develop the application.
* Spring Boot 3.3.3 - A framework that simplifies the development of Java applications by providing defaults for code and configuration to create stand-alone, production-grade Spring-based applications.
* MySql 8.0.33 - A popular open-source relational database management system used to store application data.
* Hibernate - An ORM (Object-Relational Mapping) framework used by Spring Data JPA to map Java objects to database tables and manage their relationships.
* Docker - A platform used to containerize the application, allowing it to be easily deployed and run in various environments.
* Maven 3.9.7 - A build automation tool used for managing project dependencies, building the project, and running tests.
* Lombok 1.18 - A Java library that helps to reduce boilerplate code by generating getters, setters, constructors, and other methods at compile time.
* Spring JPA - part of the Spring Data project that simplifies the implementation of JPA-based repositories by providing a common base class and automatically generating CRUD operations
* JUnit 5.10.2 - A popular testing framework for Java, used for writing and running unit tests to ensure the correctness of your code.
* Mockito 5.7.0 - A mocking framework for Java that allows you to create mock objects and define their behavior, used in unit tests to isolate the code being tested.
* Swagger/OpenAPI - A set of tools for building, documenting, and testing RESTful APIs. In this application, Swagger UI is used for API documentation and exploration.


## Additional Notes

*1. Delete the Docker Volume*
If you want to remove the persisted data when you stop the containers, you need to delete the Docker volume.
* To delete the volume:

  ```
  docker-compose down -v
  ```

    * The `-v` flag tells the docker compose to remove the volumes as well as the containers. This will delete all data stored in the volume, so the next time you can start the containers, a fresh database will be created.


*2. Assumptions*

 - A beneficiary has a unique first and last name.
 - All beneficiaries have an account.
 - We are not concerned with which account the withdrawal/deposit is made from.