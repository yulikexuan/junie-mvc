# Requirements for Adding DTOs to the Beer API

## Background
The current implementation directly exposes JPA entities in the REST API, which violates the principle of separating the web layer from the persistence layer. This creates tight coupling between the API contract and the database schema, making future changes difficult and potentially exposing sensitive information.

## Objectives
1. Implement Data Transfer Objects (DTOs) to properly separate the web layer from the persistence layer
2. Ensure proper validation of incoming data
3. Follow Spring Boot best practices for API design
4. Maintain backward compatibility with existing API consumers

## Detailed Requirements

### 1. Create DTO Classes
- Create a new package `spring.start.here.juniemvc.web.model` to contain all DTO classes
- Create the following DTO classes:
  - `BeerDto`: For general beer representation in the API
  - `BeerListDto`: For returning collections of beers (with pagination support)
  - `BeerUpsertDto`: For create/update operations (without id, createdDate, and updateDate fields)
- Use Lombok annotations (`@Getter`, `@Setter`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`) for all DTO classes
- Add proper validation annotations from Jakarta Validation (e.g., `@NotNull`, `@NotBlank`, `@Size`, `@Positive`) to ensure data integrity

### 2. Implement Object Mapping
- Create a new package `spring.start.here.juniemvc.web.mappers` for mapper interfaces
- Implement a `BeerMapper` interface using MapStruct to convert between entities and DTOs
- Configure the mapper to:
  - Map between `Beer` and `BeerDto` in both directions
  - Map from `BeerUpsertDto` to `Beer`, ignoring id, createdDate, and updateDate fields
  - Use appropriate qualifiers for any custom mapping logic

### 3. Update Service Layer
- Modify the `BeerService` interface and `BeerServiceImpl` to:
  - Accept DTO objects as input parameters instead of entities
  - Return DTO objects instead of entities
  - Use the mapper for converting between DTOs and entities
- Ensure all service methods maintain their current functionality
- Add proper validation before processing

### 4. Update Controller Layer
- Update `BeerController` to:
  - Accept and return DTO objects instead of entities
  - Use `BeerUpsertDto` for POST and PUT operations
  - Return `BeerDto` for GET operations
  - Return `BeerListDto` for collection endpoints
  - Add proper validation annotations to request parameters
  - Maintain the same URL structure and HTTP status codes

### 5. Error Handling
- Implement proper error handling for validation errors
- Return appropriate HTTP status codes and error messages
- Follow the Problem Details for HTTP APIs format (RFC 7807) for error responses

### 6. Documentation
- Add proper JavaDoc comments to all new classes and methods
- Update existing JavaDoc comments to reflect the changes

### 7. Testing
- Update existing tests to work with the new DTO classes
- Add new tests to verify the mapping logic
- Ensure all tests pass with the new implementation

## Implementation Guidelines
- Follow the constructor injection pattern for all dependencies
- Use package-private visibility where appropriate
- Ensure proper transaction boundaries
- Follow REST API design principles
- Use snake_case for JSON property names consistently
- Implement pagination for collection endpoints