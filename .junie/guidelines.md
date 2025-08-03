# Junie MVC Project Guidelines

## Project Overview

Junie MVC is a Spring Boot application that demonstrates a RESTful API for managing beer data. The application follows the Model-View-Controller (MVC) architectural pattern and provides CRUD operations for beer entities.

### Tech Stack

- **Java 21**: Core programming language
- **Spring Boot 3.5.4**: Application framework
- **Spring Data JPA**: Data access layer
- **Spring MVC**: Web layer
- **H2 Database**: In-memory database
- **Flyway**: Database migration
- **Lombok**: Reduces boilerplate code
- **Maven**: Build and dependency management
- **JUnit 5**: Testing framework
- **Mockito**: Mocking framework for testing
- **AssertJ**: Fluent assertions for testing

## Project Structure

```
junie-mvc/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── spring/start/here/juniemvc/
│   │   │       ├── domain/model/       # Domain entities
│   │   │       ├── repository/         # Data access layer
│   │   │       ├── service/            # Business logic layer
│   │   │       ├── web/controller/     # REST controllers
│   │   │       └── JunieMvcApplication.java  # Main application class
│   │   └── resources/
│   │       ├── application.properties  # Application configuration
│   │       └── db/migration/           # Flyway database migrations
│   └── test/
│       └── java/
│           └── spring/start/here/juniemvc/
│               ├── repository/         # Repository tests
│               ├── service/            # Service tests
│               └── web/controller/     # Controller tests
├── .mvn/                               # Maven wrapper configuration
├── HELP.md                             # Spring Boot help documentation
└── pom.xml                             # Maven project configuration
```

## Building and Running the Application

### Prerequisites

- Java 21 JDK
- Maven (or use the included Maven wrapper)

### Build the Application

```bash
# Using Maven
mvn clean install

# Using Maven wrapper
./mvnw clean install
```

### Run the Application

```bash
# Using Maven
mvn spring-boot:run

# Using Maven wrapper
./mvnw spring-boot:run
```

The application will start on port 8080 by default.

### API Endpoints

- `GET /api/v1/beers`: Get all beers
- `GET /api/v1/beers/{id}`: Get a beer by ID
- `POST /api/v1/beers`: Create a new beer
- `PUT /api/v1/beers/{id}`: Update an existing beer
- `DELETE /api/v1/beers/{id}`: Delete a beer

## Testing Guidelines

The project follows a comprehensive testing approach with tests for each layer:

### Repository Tests

- Use `@DataJpaTest` for testing JPA repositories
- Test all CRUD operations
- Follow the Given-When-Then pattern for test structure

### Service Tests

- Use Mockito to mock dependencies
- Test both success and failure scenarios
- Verify interactions with dependencies

### Controller Tests

- Use MockMvc for testing REST endpoints
- Test HTTP status codes and response bodies
- Test both success and failure scenarios

### Running Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=BeerControllerTest

# Run specific test method
mvn test -Dtest=BeerControllerTest#testGetBeerById
```

## Best Practices

### Code Organization

- Follow the package-by-feature structure
- Keep classes focused on a single responsibility
- Use interfaces for service definitions

### Database

- Use Flyway migrations for database schema changes
- Define entity relationships clearly
- Use appropriate JPA annotations

### API Design

- Follow RESTful principles
- Use appropriate HTTP methods and status codes
- Document API endpoints with clear comments

### Testing

- Write tests for all layers (repository, service, controller)
- Use the Given-When-Then pattern for test structure
- Mock dependencies to isolate the unit under test

### Error Handling

- Use appropriate exception handling
- Return meaningful error messages
- Use proper HTTP status codes for errors

### Logging

- Use appropriate logging levels
- Include relevant context in log messages
- Avoid logging sensitive information