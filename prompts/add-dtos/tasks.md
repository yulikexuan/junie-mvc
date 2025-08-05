# Task List for Adding DTOs to the Beer API

## 1. Add Required Dependencies
- [ ] Add MapStruct dependency to pom.xml
- [ ] Update maven-compiler-plugin configuration to include MapStruct processor
- [ ] Add lombok-mapstruct-binding dependency

## 2. Create DTO Classes
- [ ] Create package `spring.start.here.juniemvc.web.model`
- [ ] Create `BeerDto.java` class with validation annotations
- [ ] Create `BeerUpsertDto.java` class with validation annotations
- [ ] Create `BeerListDto.java` class for paginated responses

## 3. Implement Object Mapping
- [ ] Create package `spring.start.here.juniemvc.web.mappers`
- [ ] Create `BeerMapper.java` interface with MapStruct annotations
- [ ] Define mapping methods for converting between entities and DTOs

## 4. Update Service Layer
- [ ] Update `BeerService.java` interface to use DTOs
- [ ] Update `BeerServiceImpl.java` to implement the updated interface
- [ ] Add proper transaction boundaries with `@Transactional` annotations
- [ ] Implement pagination for the `getAllBeers` method

## 5. Update Controller Layer
- [ ] Update `BeerController.java` to accept and return DTOs
- [ ] Add validation for request bodies using `@Valid` annotation
- [ ] Update response status codes and error handling
- [ ] Implement pagination parameters for collection endpoints

## 6. Implement Error Handling
- [ ] Create package `spring.start.here.juniemvc.web.exception`
- [ ] Create `GlobalExceptionHandler.java` class with `@ControllerAdvice`
- [ ] Implement RFC 7807 Problem Details for error responses
- [ ] Add specific handling for validation errors

## 7. Update Tests
- [ ] Update `BeerControllerTest.java` to use DTOs
- [ ] Update `BeerServiceImplTest.java` to use DTOs
- [ ] Add tests for validation failures
- [ ] Add tests for pagination functionality

## 8. Final Verification
- [ ] Build the project to ensure all components compile correctly
- [ ] Run all tests to verify functionality
- [ ] Manually test API endpoints using a tool like Postman
- [ ] Review code for adherence to Spring Boot best practices