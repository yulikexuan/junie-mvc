# Implementation Plan for Adding DTOs to the Beer API

## Overview
This plan outlines the steps to implement Data Transfer Objects (DTOs) in the Beer API to properly separate the web layer from the persistence layer, following Spring Boot best practices.

## Detailed Implementation Plan

### 1. Add Required Dependencies
- Add MapStruct dependency to pom.xml:
  ```xml
  <!-- MapStruct -->
  <dependency>
      <groupId>org.mapstruct</groupId>
      <artifactId>mapstruct</artifactId>
      <version>1.5.5.Final</version>
  </dependency>
  ```
- Update maven-compiler-plugin configuration to include MapStruct processor:
  ```xml
  <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-compiler-plugin</artifactId>
      <configuration>
          <annotationProcessorPaths>
              <path>
                  <groupId>org.projectlombok</groupId>
                  <artifactId>lombok</artifactId>
                  <version>${lombok.version}</version>
              </path>
              <path>
                  <groupId>org.mapstruct</groupId>
                  <artifactId>mapstruct-processor</artifactId>
                  <version>1.5.5.Final</version>
              </path>
              <path>
                  <groupId>org.projectlombok</groupId>
                  <artifactId>lombok-mapstruct-binding</artifactId>
                  <version>0.2.0</version>
              </path>
          </annotationProcessorPaths>
      </configuration>
  </plugin>
  ```

### 2. Create DTO Classes
- Create a new package `spring.start.here.juniemvc.web.model`
- Create the following DTO classes:

#### BeerDto.java
```java
package spring.start.here.juniemvc.web.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerDto {
    
    private Integer id;
    private Integer version;
    
    @NotBlank
    @Size(min = 3, max = 100)
    private String beerName;
    
    @NotBlank
    private String beerStyle;
    
    @NotBlank
    @Size(min = 3, max = 13)
    private String upc;
    
    @NotNull
    @Positive
    private Integer quantityOnHand;
    
    @NotNull
    @Positive
    private BigDecimal price;
    
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
```

#### BeerUpsertDto.java
```java
package spring.start.here.juniemvc.web.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerUpsertDto {
    
    @NotBlank
    @Size(min = 3, max = 100)
    private String beerName;
    
    @NotBlank
    private String beerStyle;
    
    @NotBlank
    @Size(min = 3, max = 13)
    private String upc;
    
    @NotNull
    @Positive
    private Integer quantityOnHand;
    
    @NotNull
    @Positive
    private BigDecimal price;
}
```

#### BeerListDto.java
```java
package spring.start.here.juniemvc.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerListDto {
    
    private List<BeerDto> beers;
    private int totalPages;
    private int currentPage;
    private long totalElements;
}
```

### 3. Implement Object Mapping
- Create a new package `spring.start.here.juniemvc.web.mappers`
- Create the BeerMapper interface:

#### BeerMapper.java
```java
package spring.start.here.juniemvc.web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import spring.start.here.juniemvc.domain.model.Beer;
import spring.start.here.juniemvc.web.model.BeerDto;
import spring.start.here.juniemvc.web.model.BeerUpsertDto;

@Mapper(componentModel = "spring")
public interface BeerMapper {
    
    BeerDto beerToBeerDto(Beer beer);
    
    Beer beerDtoToBeer(BeerDto beerDto);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "updateDate", ignore = true)
    Beer beerUpsertDtoToBeer(BeerUpsertDto beerUpsertDto);
}
```

### 4. Update Service Layer
- Update the BeerService interface:

#### BeerService.java
```java
package spring.start.here.juniemvc.service;

import spring.start.here.juniemvc.web.model.BeerDto;
import spring.start.here.juniemvc.web.model.BeerListDto;
import spring.start.here.juniemvc.web.model.BeerUpsertDto;

import java.util.Optional;

/**
 * Service interface for Beer operations
 */
public interface BeerService {

    /**
     * Save a beer
     * @param beerUpsertDto the beer to save
     * @return the saved beer
     */
    BeerDto saveBeer(BeerUpsertDto beerUpsertDto);

    /**
     * Get a beer by ID
     * @param id the beer ID
     * @return the beer if found
     */
    Optional<BeerDto> getBeerById(Integer id);

    /**
     * Get all beers
     * @return list of all beers with pagination
     */
    BeerListDto getAllBeers(Integer pageNumber, Integer pageSize);

    /**
     * Update an existing beer
     * @param id the beer ID to update
     * @param beerUpsertDto the updated beer data
     * @return the updated beer if found, empty otherwise
     */
    Optional<BeerDto> updateBeer(Integer id, BeerUpsertDto beerUpsertDto);

    /**
     * Delete a beer by ID
     * @param id the beer ID to delete
     * @return true if beer was deleted, false if not found
     */
    boolean deleteBeer(Integer id);
}
```

- Update the BeerServiceImpl class:

#### BeerServiceImpl.java
```java
package spring.start.here.juniemvc.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.start.here.juniemvc.domain.model.Beer;
import spring.start.here.juniemvc.repository.BeerRepository;
import spring.start.here.juniemvc.web.mappers.BeerMapper;
import spring.start.here.juniemvc.web.model.BeerDto;
import spring.start.here.juniemvc.web.model.BeerListDto;
import spring.start.here.juniemvc.web.model.BeerUpsertDto;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of BeerService interface
 */
@Service
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    public BeerServiceImpl(BeerRepository beerRepository, BeerMapper beerMapper) {
        this.beerRepository = beerRepository;
        this.beerMapper = beerMapper;
    }

    @Override
    @Transactional
    public BeerDto saveBeer(BeerUpsertDto beerUpsertDto) {
        Beer beer = beerMapper.beerUpsertDtoToBeer(beerUpsertDto);
        Beer savedBeer = beerRepository.save(beer);
        return beerMapper.beerToBeerDto(savedBeer);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BeerDto> getBeerById(Integer id) {
        return beerRepository.findById(id)
                .map(beerMapper::beerToBeerDto);
    }

    @Override
    @Transactional(readOnly = true)
    public BeerListDto getAllBeers(Integer pageNumber, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(
                pageNumber != null ? pageNumber : 0,
                pageSize != null ? pageSize : 25);
        
        Page<Beer> beerPage = beerRepository.findAll(pageRequest);
        
        return BeerListDto.builder()
                .beers(beerPage.getContent().stream()
                        .map(beerMapper::beerToBeerDto)
                        .collect(Collectors.toList()))
                .totalPages(beerPage.getTotalPages())
                .currentPage(beerPage.getNumber())
                .totalElements(beerPage.getTotalElements())
                .build();
    }

    @Override
    @Transactional
    public Optional<BeerDto> updateBeer(Integer id, BeerUpsertDto beerUpsertDto) {
        return beerRepository.findById(id)
                .map(existingBeer -> {
                    // Update the existing beer with new values
                    existingBeer.setBeerName(beerUpsertDto.getBeerName());
                    existingBeer.setBeerStyle(beerUpsertDto.getBeerStyle());
                    existingBeer.setUpc(beerUpsertDto.getUpc());
                    existingBeer.setPrice(beerUpsertDto.getPrice());
                    existingBeer.setQuantityOnHand(beerUpsertDto.getQuantityOnHand());

                    // Save the updated beer
                    Beer savedBeer = beerRepository.save(existingBeer);
                    return beerMapper.beerToBeerDto(savedBeer);
                });
    }

    @Override
    @Transactional
    public boolean deleteBeer(Integer id) {
        return beerRepository.findById(id)
                .map(beer -> {
                    beerRepository.delete(beer);
                    return true;
                })
                .orElse(false);
    }
}
```

### 5. Update Controller Layer
- Update the BeerController class:

#### BeerController.java
```java
package spring.start.here.juniemvc.web.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.start.here.juniemvc.web.model.BeerDto;
import spring.start.here.juniemvc.web.model.BeerListDto;
import spring.start.here.juniemvc.web.model.BeerUpsertDto;
import spring.start.here.juniemvc.service.BeerService;

/**
 * REST Controller for Beer operations
 */
@RestController
@RequestMapping("/api/v1/beers")
public class BeerController {

    private final BeerService beerService;

    public BeerController(BeerService beerService) {
        this.beerService = beerService;
    }

    /**
     * Create a new beer
     * @param beerUpsertDto the beer to create
     * @return the created beer
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BeerDto createBeer(@Valid @RequestBody BeerUpsertDto beerUpsertDto) {
        return beerService.saveBeer(beerUpsertDto);
    }

    /**
     * Get a beer by ID
     * @param beerId the beer ID
     * @return the beer if found, or 404 if not found
     */
    @GetMapping("/{beerId}")
    public ResponseEntity<BeerDto> getBeerById(@PathVariable("beerId") Integer beerId) {
        return beerService.getBeerById(beerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all beers with pagination
     * @param pageNumber the page number (0-based)
     * @param pageSize the page size
     * @return list of all beers with pagination information
     */
    @GetMapping
    public BeerListDto getAllBeers(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize) {
        return beerService.getAllBeers(pageNumber, pageSize);
    }

    /**
     * Update an existing beer
     * @param beerId the beer ID to update
     * @param beerUpsertDto the updated beer data
     * @return the updated beer if found, or 404 if not found
     */
    @PutMapping("/{beerId}")
    public ResponseEntity<BeerDto> updateBeer(
            @PathVariable("beerId") Integer beerId,
            @Valid @RequestBody BeerUpsertDto beerUpsertDto) {
        return beerService.updateBeer(beerId, beerUpsertDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete a beer by ID
     * @param beerId the beer ID to delete
     * @return 204 No Content if deleted, or 404 if not found
     */
    @DeleteMapping("/{beerId}")
    public ResponseEntity<Void> deleteBeer(@PathVariable("beerId") Integer beerId) {
        return beerService.deleteBeer(beerId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
```

### 6. Implement Error Handling
- Create a new package `spring.start.here.juniemvc.web.exception`
- Create a global exception handler:

#### GlobalExceptionHandler.java
```java
package spring.start.here.juniemvc.web.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Validation failed");
        
        problemDetail.setTitle("Validation Error");
        problemDetail.setType(URI.create("https://api.juniemvc.com/errors/validation"));
        problemDetail.setProperty("timestamp", Instant.now());
        
        Map<String, String> validationErrors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        
        problemDetail.setProperty("errors", validationErrors);
        
        return new ResponseEntity<>(problemDetail, headers, status);
    }
    
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        
        problemDetail.setTitle("Server Error");
        problemDetail.setType(URI.create("https://api.juniemvc.com/errors/server"));
        problemDetail.setProperty("timestamp", Instant.now());
        
        return problemDetail;
    }
}
```

### 7. Update Tests
- Update the BeerControllerTest to use DTOs:

#### BeerControllerTest.java
```java
package spring.start.here.juniemvc.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import spring.start.here.juniemvc.service.BeerService;
import spring.start.here.juniemvc.web.model.BeerDto;
import spring.start.here.juniemvc.web.model.BeerListDto;
import spring.start.here.juniemvc.web.model.BeerUpsertDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BeerService beerService;

    BeerDto validBeerDto;
    BeerUpsertDto validBeerUpsertDto;

    @BeforeEach
    void setUp() {
        validBeerDto = BeerDto.builder()
                .id(1)
                .version(1)
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456789012")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        validBeerUpsertDto = BeerUpsertDto.builder()
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456789012")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();
    }

    @Test
    void testGetBeerById() throws Exception {
        given(beerService.getBeerById(anyInt())).willReturn(Optional.of(validBeerDto));

        mockMvc.perform(get("/api/v1/beers/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(validBeerDto.getId()))
                .andExpect(jsonPath("$.beerName").value(validBeerDto.getBeerName()));
    }

    @Test
    void testGetBeerByIdNotFound() throws Exception {
        given(beerService.getBeerById(anyInt())).willReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/beers/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllBeers() throws Exception {
        BeerListDto beerListDto = BeerListDto.builder()
                .beers(List.of(validBeerDto))
                .currentPage(0)
                .totalPages(1)
                .totalElements(1)
                .build();
        
        given(beerService.getAllBeers(any(), any())).willReturn(beerListDto);

        mockMvc.perform(get("/api/v1/beers")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.beers").isArray())
                .andExpect(jsonPath("$.beers[0].id").value(validBeerDto.getId()))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void testCreateBeer() throws Exception {
        given(beerService.saveBeer(any(BeerUpsertDto.class))).willReturn(validBeerDto);

        mockMvc.perform(post("/api/v1/beers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validBeerUpsertDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(validBeerDto.getId()))
                .andExpect(jsonPath("$.beerName").value(validBeerDto.getBeerName()));
    }

    @Test
    void testUpdateBeer() throws Exception {
        given(beerService.updateBeer(anyInt(), any(BeerUpsertDto.class))).willReturn(Optional.of(validBeerDto));

        mockMvc.perform(put("/api/v1/beers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validBeerUpsertDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(validBeerDto.getId()))
                .andExpect(jsonPath("$.beerName").value(validBeerDto.getBeerName()));
    }

    @Test
    void testUpdateBeerNotFound() throws Exception {
        given(beerService.updateBeer(anyInt(), any(BeerUpsertDto.class))).willReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/beers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validBeerUpsertDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteBeer() throws Exception {
        given(beerService.deleteBeer(anyInt())).willReturn(true);

        mockMvc.perform(delete("/api/v1/beers/1"))
                .andExpect(status().isNoContent());

        verify(beerService).deleteBeer(1);
    }

    @Test
    void testDeleteBeerNotFound() throws Exception {
        given(beerService.deleteBeer(anyInt())).willReturn(false);

        mockMvc.perform(delete("/api/v1/beers/1"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void testValidationFailure() throws Exception {
        BeerUpsertDto invalidBeerDto = BeerUpsertDto.builder()
                .beerName("") // Invalid: blank name
                .beerStyle("IPA")
                .upc("123456789012")
                .price(new BigDecimal("-1.0")) // Invalid: negative price
                .quantityOnHand(-5) // Invalid: negative quantity
                .build();

        mockMvc.perform(post("/api/v1/beers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidBeerDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").exists())
                .andExpect(jsonPath("$.title").value("Validation Error"))
                .andExpect(jsonPath("$.errors").exists());
    }
}
```

- Update the BeerServiceImplTest to use DTOs:

#### BeerServiceImplTest.java
```java
package spring.start.here.juniemvc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import spring.start.here.juniemvc.domain.model.Beer;
import spring.start.here.juniemvc.repository.BeerRepository;
import spring.start.here.juniemvc.web.mappers.BeerMapper;
import spring.start.here.juniemvc.web.model.BeerDto;
import spring.start.here.juniemvc.web.model.BeerListDto;
import spring.start.here.juniemvc.web.model.BeerUpsertDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BeerServiceImplTest {

    @Mock
    BeerRepository beerRepository;

    @Mock
    BeerMapper beerMapper;

    @InjectMocks
    BeerServiceImpl beerService;

    Beer testBeer;
    BeerDto testBeerDto;
    BeerUpsertDto testBeerUpsertDto;

    @BeforeEach
    void setUp() {
        testBeer = Beer.builder()
                .id(1)
                .version(1)
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456789012")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        testBeerDto = BeerDto.builder()
                .id(1)
                .version(1)
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456789012")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        testBeerUpsertDto = BeerUpsertDto.builder()
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456789012")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();
    }

    @Test
    void testSaveBeer() {
        // Given
        given(beerMapper.beerUpsertDtoToBeer(any(BeerUpsertDto.class))).willReturn(testBeer);
        given(beerRepository.save(any(Beer.class))).willReturn(testBeer);
        given(beerMapper.beerToBeerDto(any(Beer.class))).willReturn(testBeerDto);

        // When
        BeerDto savedBeerDto = beerService.saveBeer(testBeerUpsertDto);

        // Then
        assertThat(savedBeerDto).isNotNull();
        assertThat(savedBeerDto.getId()).isEqualTo(testBeerDto.getId());
        verify(beerRepository).save(any(Beer.class));
    }

    @Test
    void testGetBeerById() {
        // Given
        given(beerRepository.findById(1)).willReturn(Optional.of(testBeer));
        given(beerMapper.beerToBeerDto(testBeer)).willReturn(testBeerDto);

        // When
        Optional<BeerDto> foundBeerDto = beerService.getBeerById(1);

        // Then
        assertThat(foundBeerDto).isPresent();
        assertThat(foundBeerDto.get().getId()).isEqualTo(testBeerDto.getId());
    }

    @Test
    void testGetAllBeers() {
        // Given
        Page<Beer> beerPage = new PageImpl<>(List.of(testBeer));
        given(beerRepository.findAll(any(PageRequest.class))).willReturn(beerPage);
        given(beerMapper.beerToBeerDto(testBeer)).willReturn(testBeerDto);

        // When
        BeerListDto beerListDto = beerService.getAllBeers(0, 10);

        // Then
        assertThat(beerListDto).isNotNull();
        assertThat(beerListDto.getBeers()).hasSize(1);
        assertThat(beerListDto.getTotalPages()).isEqualTo(1);
    }

    @Test
    void testUpdateBeer() {
        // Given
        given(beerRepository.findById(1)).willReturn(Optional.of(testBeer));
        given(beerRepository.save(any(Beer.class))).willReturn(testBeer);
        given(beerMapper.beerToBeerDto(testBeer)).willReturn(testBeerDto);

        // When
        Optional<BeerDto> updatedBeerDto = beerService.updateBeer(1, testBeerUpsertDto);

        // Then
        assertThat(updatedBeerDto).isPresent();
        assertThat(updatedBeerDto.get().getId()).isEqualTo(testBeerDto.getId());
        verify(beerRepository).save(testBeer);
    }

    @Test
    void testDeleteBeer() {
        // Given
        given(beerRepository.findById(1)).willReturn(Optional.of(testBeer));

        // When
        boolean result = beerService.deleteBeer(1);

        // Then
        assertThat(result).isTrue();
        verify(beerRepository).delete(testBeer);
    }
}
```

## Implementation Notes

### Key Changes
1. **DTO Structure**:
   - Created three DTO classes to separate concerns:
     - `BeerDto` for general representation
     - `BeerUpsertDto` for create/update operations (without id, version, and timestamps)
     - `BeerListDto` for paginated collections

2. **Validation**:
   - Added Jakarta Validation annotations to ensure data integrity
   - Implemented a global exception handler for validation errors

3. **Mapping**:
   - Used MapStruct for efficient object mapping
   - Configured mappings to ignore certain fields when converting from DTOs to entities

4. **Service Layer**:
   - Updated to work with DTOs instead of entities
   - Added proper transaction boundaries with `@Transactional` annotations
   - Implemented pagination for the getAllBeers method

5. **Controller Layer**:
   - Updated to accept and return DTOs
   - Added validation for request bodies
   - Maintained the same URL structure and HTTP status codes

6. **Error Handling**:
   - Implemented RFC 7807 Problem Details for error responses
   - Added specific handling for validation errors

### Benefits of This Implementation
1. **Separation of Concerns**: The web layer is now properly separated from the persistence layer
2. **API Stability**: Changes to the database schema won't affect the API contract
3. **Security**: Sensitive fields can be excluded from DTOs
4. **Validation**: Input data is properly validated before processing
5. **Pagination**: Collection endpoints now support pagination
6. **Error Handling**: Consistent error responses following RFC 7807

### Next Steps
1. Consider adding API documentation using Springdoc OpenAPI
2. Implement caching for frequently accessed data
3. Add more comprehensive test coverage
4. Consider implementing versioning strategy for future API changes