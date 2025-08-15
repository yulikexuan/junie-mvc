# Task List for Adding DTOs to the Beer API

## 1. Create DTO Classes
- [x] Create package `spring.start.here.juniemvc.web.model`
- [x] Create `BeerDto.java` class with validation annotations
- [x] Create `BeerUpsertDto.java` class with validation annotations
- [x] Create `BeerListDto.java` class for paginated responses

## 2. Implement Object Mapping
- [x] Create package `spring.start.here.juniemvc.web.mappers`
- [x] Create `BeerMapper.java` interface with MapStruct annotations
- [x] Define mapping methods for converting between entities and DTOs

## 3. Update Service Layer
- [x] Update `BeerService.java` interface to use DTOs
- [x] Update `BeerServiceImpl.java` to implement the updated interface
- [x] Add proper transaction boundaries with `@Transactional` annotations
- [x] Implement pagination for the `getAllBeers` method

## 4. Update Controller Layer
- [x] Update `BeerController.java` to accept and return DTOs
- [x] Add validation for request bodies using `@Valid` annotation
- [x] Update response status codes and error handling
- [x] Implement pagination parameters for collection endpoints

## 5. Implement Error Handling
- [x] Create package `spring.start.here.juniemvc.web.exception`
- [x] Create `GlobalExceptionHandler.java` class with `@ControllerAdvice`
- [x] Implement RFC 7807 Problem Details for error responses
- [x] Add specific handling for validation errors

## 6. Update Tests
- [x] Update `BeerControllerTest.java` to use DTOs
- [x] Update `BeerServiceImplTest.java` to use DTOs
- [x] Add tests for validation failures
- [x] Add tests for pagination functionality

## 7. Final Verification
- [x] Build the project to ensure all components compile correctly
- [x] Run all tests to verify functionality
- [x] Review code for adherence to Spring Boot best practices