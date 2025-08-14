# currency-exchange

-------------

## TODO

- CurrencyExchangeController integration tests
- update docker-compose to add application (only redis now)
- exception handler
- initial cache setup
- logs
- metrics
- resilience4j
- properties for different environments
- CI/CD

## Built with

- Java
- Maven
- Spring JPA
- Spring Web
- Redis

## Project setup

### Using Docker - TODO only redis in compose

#### Clone the repository
```console
git clone https://github.com/AdrianBCodes/currency-exchange.git
```

#### Use Docker Compose
```console
docker-compose up -d
```

You can check status of application's stack
```console
docker-compose ps
```

#### Access the application
```console
http://localhost:8080
```

Make sure you have Docker installed on your system before running the above commands.

### Without Docker

For building and running the application you need:

- Java
- Maven
- Redis

Be aware that you need to locally setup Redis on port 6379 or configure `/src/main/resources/application.properties` for your own setup.

There are several ways to run a Spring Boot application on your local machine. One way is to execute the `main` method in the `src/main/java/com/adrianbcodes/currencyexchange/CurrencyExchangeApplication.java` class from your IDE.

Alternatively you can use the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) like so:

```shell
mvn spring-boot:run
```