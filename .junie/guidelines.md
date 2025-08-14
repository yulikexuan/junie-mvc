# Spring Boot Guidelines

## 1. Prefer Constructor Injection over Field/Setter Injection
* Declare all the mandatory dependencies as `final` fields and inject them through the constructor.
* Spring will auto-detect if there is only one constructor, no need to add `@Autowired` on the constructor.
* Avoid field/setter injection in production code.

**Explanation:**

* Making all the required dependencies as `final` fields and injecting them through constructor make sure that the object is always in a properly initialized state using the plain Java language feature itself. No need to rely on any framework-specific initialization mechanism.
* You can write unit tests without relying on reflection-based initialization or mocking.
* The constructor-based injection clearly communicates what are the dependencies of a class without having to look into the source code.
* Spring Boot provides extension points as builders such as `RestClient.Builder`, `ChatClient.Builder`, etc. Using constructor-injection, we can do the customization and initialize the actual dependency.

```java
@Service
public class OrderService {
   private final OrderRepository orderRepository;
   private final RestClient restClient;

   public OrderService(OrderRepository orderRepository, 
                       RestClient.Builder builder) {
       this.orderRepository = orderRepository;
       this.restClient = builder
               .baseUrl("http://catalog-service.com")
               .requestInterceptor(new ClientCredentialTokenInterceptor())
               .build();
   }

   //... methods
}
```

## 2. Prefer package-private over public for Spring components
* Declare Controllers, their request-handling methods, `@Configuration` classes and `@Bean` methods with default (package-private) visibility whenever possible. There's no obligation to make everything `public`.

**Explanation:**

* Keeping classes and methods package-private reinforces encapsulation and abstraction by hiding implementation details from the rest of your application.
* Spring Boot's classpath scanning will still detect and invoke package-private components (for example, invoking your `@Bean` methods or controller handlers), so you can safely restrict visibility to only what clients truly need. This approach confines your internal APIs to a single package while still allowing the framework to wire up beans and handle HTTP requests.

## 3. Organize Configuration with Typed Properties
* Group application-specific configuration properties with a common prefix in `application.properties` or `.yml`.
* Bind them to `@ConfigurationProperties` classes with validation annotations so that the application will fail fast if the configuration is invalid.
* Prefer environment variables instead of profiles for passing different configuration properties for different environments.

**Explanation:**

* By grouping and binding configuration in a single `@ConfigurationProperties` bean, you centralize both the property names and their validation rules.
  In contrast, using `@Value("${…}")` across many components forces you to update each injection point whenever a key or validation requirement changes.
* Overusing profiles to customize the application configuration may lead to unexpected issues due to the order of profiles specified.
  As you can enable multiple profiles with different combinations, making sense of the effective application configuration becomes tricky.

## 4. Define Clear Transaction Boundaries
* Define each Service-layer method as a transactional unit.
* Annotate query-only methods with `@Transactional(readOnly = true)`.
* Annotate data-modifying methods with `@Transactional`.
* Limit the code inside each transaction to the smallest necessary scope.

**Explanation:**

* **Single Unit of Work:** Group all database operations for a given use case into one atomic unit, which in Spring Boot is typically a `@Service` annotated class method. This ensures that either all operations succeed or none do.
* **Connection Reuse:** A `@Transactional` method runs on a single database connection for its entire scope, avoiding the overhead of acquiring and returning connections from the connection pool for each operation.
* **Read-only Optimizations:** Marking methods as `readOnly = true` disables unnecessary dirty-checking and flushes, improving performance for pure reads.
* **Reduced Contention:** Keeping transactions as brief as possible minimizes lock duration, lowering the chance of contention in high-traffic applications.

## 5. Disable Open Session in View Pattern
* While using Spring Data JPA, disable the Open Session in View filter by setting ` spring.jpa.open-in-view=false` in `application.properties/yml.`

**Explanation:**

* Open Session In View (OSIV) filter transparently enables loading the lazy associations while rendering the view or serializing JPA entities. This may lead to the N + 1 Select problem.
* Disabling OSIV forces you to fetch exactly the associations you need via fetch joins, entity graphs, or explicit queries, and hence you can avoid unexpected N + 1 selects and `LazyInitializationExceptions`.

## 6. Separate Web Layer from Persistence Layer
* Don't expose entities directly as responses in controllers.
* Define explicit request and response record (DTO) classes instead.
* Apply Jakarta Validation annotations on your request records to enforce input rules.

**Explanation:**

* Returning or binding directly to entities couples your public API to your database schema, making future changes riskier.
* DTOs let you clearly declare exactly which fields clients can send or receive, improving clarity and security.
* With dedicated DTOs per use case, you can annotate fields for validation without relying on complex validation groups.
* Use Java bean mapper libraries to simplify DTO conversions. Prefer MapStruct library that can generate bean mapper implementation at compile time so that there won't be runtime reflection overhead.

## 7. Follow REST API Design Principles
* **Versioned, resource-oriented URLs:** Structure your endpoints as `/api/v{version}/resources` (e.g. `/api/v1/orders`).
* **Consistent patterns for collections and sub-resources:** Keep URL conventions uniform (for example, `/posts` for posts collection and `/posts/{slug}/comments` for comments of a specific post).
* **Explicit HTTP status codes via ResponseEntity:** Use `ResponseEntity<T>` to return the correct status (e.g. 200 OK, 201 Created, 404 Not Found) along with the response body.
* Use pagination for collection resources that may contain an unbounded number of items.
* The JSON payload must use a JSON object as a top-level data structure to allow for future extension.
* Use snake_case or camelCase for JSON property names consistently.

**Explanation:**

* **Predictability and discoverability:** Adhering to well-known REST conventions makes your API intuitive. Clients can guess URLs and behaviors without extensive documentation.
* **Reliable client integrations:** Standardized URL structures, status codes, and headers enable consumers to build against your API with confidence, knowing exactly what each response will look like.
* For more comprehensive REST API Guidelines, please refer [Zalando RESTful API and Event Guidelines](https://opensource.zalando.com/restful-api-guidelines/).

## 8. Use Command Objects for Business Operations
* Create purpose-built command records (e.g., `CreateOrderCommand`) to wrap input data.
* Accept these commands in your service methods to drive creation or update workflows.

**Explanation:**

* Using the use-case specific Command and Query objects clearly communicates what input data is expected from the caller.
  Otherwise, the caller had to guess whether they should create and pass the unique key or created_date, or they will be generated by the server/database.

## 9. Centralize Exception Handling
* Define a global handler class annotated with `@ControllerAdvice` (or `@RestControllerAdvice` for REST APIs) using `@ExceptionHandler` methods to handle specific exceptions.
* Return consistent error responses. Consider using the ProblemDetails response format ([RFC 9457](https://www.rfc-editor.org/rfc/rfc9457)).

**Explanation:**

* We should always handle all possible exceptions and return a standard error response instead of throwing exceptions.
* It is better to centralize the exception handling in a `GlobalExceptionHandler` using `(Rest)ControllerAdvice` instead of duplicating the try/catch exception handling logic across the controllers.

## 10. Actuator
* Expose only essential actuator endpoints (such as `/health`, `/info`, `/metrics`) without requiring authentication. All the other actuator endpoints must be secured.

**Explanation:**

* Endpoints like `/actuator/health` and `/actuator/metrics` are critical for external health checks and metric collection (e.g., by Prometheus). Allowing these to be accessed anonymously ensures monitoring tools can function without extra credentials. All the remaining endpoints should be secured.
* In non-production environments (DEV, QA), you can expose additional actuator endpoints such as `/actuator/beans`, `/actuator/loggers` for debugging purpose.

## 11. Internationalization with ResourceBundles
* Externalize all user-facing text such as labels, prompts, and messages into ResourceBundles rather than embedding them in code.

**Explanation:**

* Hardcoded strings make it difficult to support multiple languages. By placing your labels, error messages, and other text in locale-specific ResourceBundle files, you can maintain separate translations for each language.
* At runtime, Spring can load the appropriate bundle based on the user's locale or a preference setting, making it simple to add new languages and switch between them dynamically.

## 12. Use Testcontainers for integration tests
* Spin up real services (databases, message brokers, etc.) in your integration tests to mirror production environments.

**Explanation:**

* Most of the modern applications use a wide range of technologies such as SQL/NoSQL databases, key-value stores, message brokers, etc. Instead of using in-memory variants or mocks, Testcontainers can spin up those dependencies as Docker containers and allow you to test using the same type of dependencies that you will use in the production. This reduces environment inconsistencies and increases confidence in your integration tests.
* Always use docker images with a specific version of the dependency that you are using in production instead of using the `latest` tag.

## 13. Use random port for integration tests
* When writing integration tests, start the application on a random available port to avoid port conflicts by annotating the test class with:

    ```java
    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
    ```

**Explanation:**

* **Avoid conflicts in CI/CD:** In your CI/CD environment, there can be multiple builds running in parallel on the same server/agent. In such cases, it is better to run the integration tests using a random available port rather than a fixed port to avoid port conflicts.

## 14. Logging
* **Use a proper logging framework.**  
  Never use `System.out.println()` for application logging. Rely on SLF4J (or a compatible abstraction) and your chosen backend (Logback, Log4j2, etc.).

* **Protect sensitive data.**  
  Ensure that no credentials, personal information, or other confidential details ever appear in log output.

* **Guard expensive log calls.**  
  When building verbose messages at `DEBUG` or `TRACE` level, especially those involving method calls or complex string concatenations, wrap them in a level check or use suppliers:

```java
if (logger.isDebugEnabled()) {
    logger.debug("Detailed state: {}", computeExpensiveDetails());
}

// using Supplier/Lambda expression
logger.atDebug()
	.setMessage("Detailed state: {}")
	.addArgument(() -> computeExpensiveDetails())
    .log();
```

**Explanation:**

* **Flexible verbosity control:** A logging framework lets you adjust what gets logged and where with the support for tuning log levels per environment (development, testing, production).

* **Rich contextual metadata:** Beyond the message itself, you can capture class/method names, thread IDs, process IDs, and any custom context via MDC, aiding diagnosis.

* **Multiple outputs and formats:** Direct logs to consoles, rolling files, databases, or remote systems, and choose formats like JSON for seamless ingestion into ELK, Loki, or other log-analysis tools.

* **Better tooling and analysis:** Structured logs and controlled log levels make it easier to filter noise, automate alerts, and visualize application behavior in real time.

## 15. Flyway database migrations
* Spring Boot auto-detects Flyway on the classpath and runs migrations automatically at application startup, before JPA/Hibernate initializes the schema. Flyway records applied migrations in the `flyway_schema_history` table to ensure idempotency.

* Default migration directory:
  - Classpath location: `classpath:db/migration`
  - In a standard Maven project, place scripts under: `src/main/resources/db/migration`
  - This project already uses: `src/main/resources/db/migration`

* Versioned migration naming:
  - File pattern: `V{version}__{description}.sql` (note the double underscore)
  - Versions are dot/underscore-separated integers (e.g., `1`, `1.1`, `2_3`)
  - Descriptions are free text; spaces become underscores in the filename
  - Examples:
    - `V1__init_schema.sql`
    - `V1.1__add_beer_table.sql`
    - `V2_0__add_constraints.sql`

* Repeatable migrations:
  - File pattern: `R__{description}.sql`
  - Re-executed automatically when their checksum changes (useful for views, functions, seed data)

* Basic configuration (optional) via `application.properties/yml`:
  - Change locations: `spring.flyway.locations=classpath:db/migration,classpath:db/extra`
  - Baseline an existing schema: `spring.flyway.baseline-on-migrate=true`
  - Cleaning is disabled by default for safety in Spring Boot. If needed locally: `spring.flyway.clean-disabled=false` (use with care)

* Use H2 compliant SQL syntax for database migrations.

* When altering tables to add a property with a foreign key constraint, 
  add the new column first and then add the foreign key constraint in a second SQL statement.

For more, see Flyway docs: https://flywaydb.org/documentation/ and Spring Boot Flyway docs: https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto.data-initialization. 

### Benefits of using Flyway with Spring Boot
* Predictable, versioned schema changes: Each change is an immutable, ordered migration file (V1, V1.1, …). Flyway applies them exactly once and records them in `flyway_schema_history`, ensuring idempotency and avoiding drift.
* Environment consistency and reproducibility: The same migrations run across DEV/QA/PROD and in CI, so every environment’s schema is derived from the same source of truth.
* Seamless Spring Boot integration: Migrations execute automatically on startup before JPA/Hibernate, ensuring entities map to an up-to-date schema without manual steps.
* Auditable and traceable: Migration scripts live in version control, can be code-reviewed, and Flyway’s history table gives a clear audit trail (who/what/when applied).
* Supports repeatable changes: `R__*.sql` scripts make it easy to maintain views, functions, and seed/reference data without creating artificial version bumps.
* Handles existing databases: Baseline and repair operations help you adopt Flyway for legacy schemas and fix checksum mismatches safely.
* Test-friendly: Integration tests (e.g., with Testcontainers) can bring up a fresh database and apply the same migrations for deterministic, production-like tests.
* Improves team collaboration: Clear conventions and small, incremental scripts reduce merge conflicts and make onboarding new developers straightforward.
* Enables forward-only, low-downtime strategies: Encourages incremental, backward-compatible schema evolution patterns that work well with blue/green or rolling deploys.


## 16. Use Project Lombok
* Use Lombok to reduce boilerplate code.
* Enable annotation processing for your IDE to generate boilerplate code for you.
* When adding builder to a class, if the class extends another class, add `@SuperBuilder` for the builder.


## 17. Use Mapstruct for Type Conversions
* Use Mapstruct to convert between domain objects and DTOs.
* Use `@Mapper` to configure the mapping between the two classes.
* Use `@Mapping` to configure the mapping between the two fields.
* After modifying a Mapper, recompile the project to generate the new Mapper implementation.
* Use Mappers to update existing entities.


## 18. Service Operations
* When updating existing entities, use Mappers to update existing entities. 
* The entity should be fetched from the database, and then updated using the mapper before saving the entity back to the database.


## 19. OpenAPI Specification (API Documentation)
* Location: The API documentation lives under `openapi\openapi`. The root definition is `openapi\openapi\openapi.yaml`.
* Tooling: We use Redocly CLI for preview, bundling, and linting.
  - `openapi\package.json` scripts:
    - `start`: `redocly preview-docs`
    - `build`: `redocly bundle -o dist/bundle.yaml`
    - `test`: `redocly lint`

### 19.1. Structure and file references
* Root spec (`openapi.yaml`) references path items under `paths/` and components under `components/` using `$ref`.
* Example path references from `paths` section in the root spec:
  - `'/users/{username}': $ref: 'paths/users_{username}.yaml'`
  - `'/user': $ref: 'paths/user.yaml'`
  - `'/user/list': $ref: 'paths/user-status.yaml'`
  - `'/echo': $ref: 'paths/echo.yaml'`
* Component references in operations/webhooks use `$ref` to files under `components/`:
  - Schemas: `$ref: '../components/schemas/User.yaml'`
  - Headers: `$ref: '../components/headers/ExpiresAfter.yaml'`
  - Responses: `$ref: '../components/responses/Problem.yaml'`

### 19.2. File naming conventions
* Path operations (files under `openapi\openapi\paths`):
  - Prefer mapping from the URL path to a filename by:
    - Removing the leading slash (/),
    - Replacing remaining slashes with underscores `_`,
    - Keeping path parameters with curly braces, e.g. `{id}` remains `{id}`.
  - Examples:
    - `/users/{username}` -> `users_{username}.yaml`
    - `/user` -> `user.yaml`
    - `/pathItemWithExamples` -> `pathItemWithExamples.yaml`
  - It is acceptable to choose a clearer kebab-case name when it improves readability, especially for sub-resources, e.g. `/user/list` -> `user-status.yaml`.
* Components (files under `openapi\openapi\components`):
  - Schemas go in `components\schemas\*.yaml` (PascalCase recommended to match schema names), e.g. `User.yaml`, `Admin.yaml`.
  - Headers go in `components\headers\*.yaml`, e.g. `ExpiresAfter.yaml`.
  - Responses go in `components\responses\*.yaml`, e.g. `Problem.yaml`.
  - Security schemes are defined inline in the root spec under `components.securitySchemes` (not split into files in this project).

### 19.3. Defining and using components
* Schemas: Define each schema as its own YAML file and reference with `$ref` from requests/responses.
  - Example: `schema: { $ref: '../components/schemas/User.yaml' }`.
* Headers: Define reusable headers as files and reference them from responses.
  - Example: `X-Expires-After: { $ref: '../components/headers/ExpiresAfter.yaml' }`.
* Responses: Define common response objects (like Problem Details) and reuse them with `$ref`.
  - Example: `'400': { $ref: '../components/responses/Problem.yaml' }`.
* Webhooks: May also reference shared schemas from the same `components/schemas` folder.

### 19.4. How to validate/test the OpenAPI specification
To lint/validate the OpenAPI definition with Redocly:
1. Ensure Node.js is installed (v16+ recommended).
2. Open a terminal and change directory to the `openapi` folder:
   - Windows PowerShell: `cd .\openapi`
3. Install dependencies (first time only): `npm install`
4. Run tests (linter): `npm test`
   - This executes `redocly lint` against the spec in `openapi\openapi`.

Optional:
- Preview docs locally: `npm start`
- Build a bundled spec: `npm run build` (outputs `openapi\dist\bundle.yaml`)
