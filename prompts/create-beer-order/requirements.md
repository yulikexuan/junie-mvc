# Beer Order System Requirements

## Overview

This document outlines the requirements for implementing a Beer Order System based on the provided ERD diagram. The system will manage beers, customers, orders, order lines, and inventory using Spring Boot and JPA with Lombok.

## Entity Structure

The system consists of the following core entities:

1. **Beer** - The product being sold
2. **Customer** - The person or business placing orders
3. **BeerOrder** - An order placed by a customer
4. **BeerOrderLine** - Individual line items in an order
5. **BeerInventory** - Inventory tracking for beers

## Implementation Requirements

### 1. Entity Implementation

#### 1.1 General Guidelines

- Use Lombok annotations to reduce boilerplate code:
  - `@Getter` and `@Setter` for accessor methods
  - `@NoArgsConstructor` and `@AllArgsConstructor` for constructors
  - `@Builder` for the builder pattern
  - Exclude bidirectional relationships from `@ToString` to prevent infinite recursion
  - Configure `@EqualsAndHashCode` carefully, typically including only the ID or natural keys

- Use JPA annotations for entity mapping:
  - `@Entity` to mark classes as JPA entities
  - `@Table(name = "table_name")` to specify database table names using snake_case
  - `@Id` and `@GeneratedValue` for primary keys
  - Relationship annotations: `@OneToOne`, `@OneToMany`, `@ManyToOne`, `@ManyToMany`
  - Use `@Version` for optimistic locking

- Follow best practices:
  - Use bidirectional relationships where appropriate
  - Configure cascade operations carefully
  - Set fetch types to LAZY for collections to avoid performance issues
  - Implement proper equals and hashCode methods

#### 1.2 Beer Entity

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "beer")
public class Beer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Version
    private Integer version;

    private String beerName;
    private String beerStyle;
    
    @Column(unique = true)
    private String upc;
    
    private Integer quantityOnHand;
    private BigDecimal price;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;
    
    // One Beer can have many BeerInventory records
    @OneToMany(mappedBy = "beer")
    @Builder.Default
    @ToString.Exclude
    private Set<BeerInventory> inventories = new HashSet<>();
    
    // One Beer can be in many OrderLines
    @OneToMany(mappedBy = "beer")
    @Builder.Default
    @ToString.Exclude
    private Set<BeerOrderLine> orderLines = new HashSet<>();
}
```

#### 1.3 Customer Entity

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Version
    private Integer version;

    private String name;
    private String email;
    private String phone;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;
    
    // One Customer can have many BeerOrders
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    @Builder.Default
    @ToString.Exclude
    private Set<BeerOrder> beerOrders = new HashSet<>();
}
```

#### 1.4 BeerOrder Entity

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "beer_order")
public class BeerOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Version
    private Integer version;

    private String orderStatus;
    private String orderStatusCallbackUrl;
    
    // Many BeerOrders belong to one Customer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    // One BeerOrder can have many BeerOrderLines
    @OneToMany(mappedBy = "beerOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private Set<BeerOrderLine> beerOrderLines = new HashSet<>();

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;
}
```

#### 1.5 BeerOrderLine Entity

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "beer_order_line")
public class BeerOrderLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Version
    private Integer version;

    private Integer orderQuantity;
    private Integer quantityAllocated;
    
    // Many BeerOrderLines belong to one BeerOrder
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beer_order_id")
    private BeerOrder beerOrder;
    
    // Many BeerOrderLines reference one Beer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beer_id")
    private Beer beer;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;
}
```

#### 1.6 BeerInventory Entity

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "beer_inventory")
public class BeerInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Version
    private Integer version;

    private Integer quantityOnHand;
    
    // Many BeerInventory records belong to one Beer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "beer_id")
    private Beer beer;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updateDate;
}
```

### 2. Repository Implementation

Create repository interfaces for each entity:

```java
@Repository
public interface BeerRepository extends JpaRepository<Beer, Integer> {
    // Custom query methods as needed
}

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    // Custom query methods as needed
}

@Repository
public interface BeerOrderRepository extends JpaRepository<BeerOrder, Integer> {
    List<BeerOrder> findAllByCustomer(Customer customer);
}

@Repository
public interface BeerOrderLineRepository extends JpaRepository<BeerOrderLine, Integer> {
    // Custom query methods as needed
}

@Repository
public interface BeerInventoryRepository extends JpaRepository<BeerInventory, Integer> {
    List<BeerInventory> findAllByBeer(Beer beer);
}
```

### 3. DTO Implementation

Following the principle of separating the web layer from the persistence layer, create DTOs for each entity:

#### 3.1 CustomerDto

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto {
    private Integer id;
    private Integer version;
    private String name;
    private String email;
    private String phone;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
```

#### 3.2 BeerOrderDto

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerOrderDto {
    private Integer id;
    private Integer version;
    private String orderStatus;
    private String orderStatusCallbackUrl;
    private Integer customerId;
    private String customerName;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
    private List<BeerOrderLineDto> beerOrderLines;
}
```

#### 3.3 BeerOrderLineDto

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerOrderLineDto {
    private Integer id;
    private Integer version;
    private Integer orderQuantity;
    private Integer quantityAllocated;
    private Integer beerId;
    private String beerName;
    private String beerStyle;
    private BigDecimal price;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
```

#### 3.4 BeerInventoryDto

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerInventoryDto {
    private Integer id;
    private Integer version;
    private Integer quantityOnHand;
    private Integer beerId;
    private String beerName;
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
```

### 4. Command Objects for Business Operations

Create command objects for business operations:

#### 4.1 CreateBeerOrderCommand

```java
public record CreateBeerOrderCommand(
    @NotNull Integer customerId,
    
    @NotEmpty List<BeerOrderLineCommand> beerOrderLines
) {}
```

#### 4.2 BeerOrderLineCommand

```java
public record BeerOrderLineCommand(
    @NotNull Integer beerId,
    
    @NotNull
    @Positive Integer orderQuantity
) {}
```

### 5. Service Layer Implementation

Implement service interfaces and implementations with proper transaction boundaries:

#### 5.1 BeerOrderService Interface

```java
public interface BeerOrderService {
    BeerOrderDto createBeerOrder(CreateBeerOrderCommand command);
    Optional<BeerOrderDto> getBeerOrderById(Integer id);
    List<BeerOrderDto> getAllBeerOrders(Integer pageNumber, Integer pageSize);
    List<BeerOrderDto> getBeerOrdersByCustomer(Integer customerId, Integer pageNumber, Integer pageSize);
    Optional<BeerOrderDto> updateBeerOrderStatus(Integer id, String orderStatus);
    boolean deleteBeerOrder(Integer id);
}
```

#### 5.2 BeerOrderServiceImpl

```java
@Service
public class BeerOrderServiceImpl implements BeerOrderService {
    private final BeerOrderRepository beerOrderRepository;
    private final CustomerRepository customerRepository;
    private final BeerRepository beerRepository;
    private final BeerOrderMapper beerOrderMapper;

    public BeerOrderServiceImpl(BeerOrderRepository beerOrderRepository,
                               CustomerRepository customerRepository,
                               BeerRepository beerRepository,
                               BeerOrderMapper beerOrderMapper) {
        this.beerOrderRepository = beerOrderRepository;
        this.customerRepository = customerRepository;
        this.beerRepository = beerRepository;
        this.beerOrderMapper = beerOrderMapper;
    }

    @Override
    @Transactional
    public BeerOrderDto createBeerOrder(CreateBeerOrderCommand command) {
        // Implementation
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BeerOrderDto> getBeerOrderById(Integer id) {
        // Implementation
    }

    @Override
    @Transactional(readOnly = true)
    public List<BeerOrderDto> getAllBeerOrders(Integer pageNumber, Integer pageSize) {
        // Implementation
    }

    @Override
    @Transactional(readOnly = true)
    public List<BeerOrderDto> getBeerOrdersByCustomer(Integer customerId, Integer pageNumber, Integer pageSize) {
        // Implementation
    }

    @Override
    @Transactional
    public Optional<BeerOrderDto> updateBeerOrderStatus(Integer id, String orderStatus) {
        // Implementation
    }

    @Override
    @Transactional
    public boolean deleteBeerOrder(Integer id) {
        // Implementation
    }
}
```

### 6. Controller Implementation

Implement REST controllers following REST API design principles:

#### 6.1 BeerOrderController

```java
@RestController
@RequestMapping("/api/v1/beer-orders")
class BeerOrderController {
    private final BeerOrderService beerOrderService;

    BeerOrderController(BeerOrderService beerOrderService) {
        this.beerOrderService = beerOrderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    BeerOrderDto createBeerOrder(@Valid @RequestBody CreateBeerOrderCommand command) {
        return beerOrderService.createBeerOrder(command);
    }

    @GetMapping("/{orderId}")
    ResponseEntity<BeerOrderDto> getBeerOrderById(@PathVariable("orderId") Integer orderId) {
        return beerOrderService.getBeerOrderById(orderId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    List<BeerOrderDto> getAllBeerOrders(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize) {
        return beerOrderService.getAllBeerOrders(pageNumber, pageSize);
    }

    @GetMapping("/customer/{customerId}")
    List<BeerOrderDto> getBeerOrdersByCustomer(
            @PathVariable("customerId") Integer customerId,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize) {
        return beerOrderService.getBeerOrdersByCustomer(customerId, pageNumber, pageSize);
    }

    @PutMapping("/{orderId}/status")
    ResponseEntity<BeerOrderDto> updateBeerOrderStatus(
            @PathVariable("orderId") Integer orderId,
            @RequestParam String orderStatus) {
        return beerOrderService.updateBeerOrderStatus(orderId, orderStatus)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{orderId}")
    ResponseEntity<Void> deleteBeerOrder(@PathVariable("orderId") Integer orderId) {
        return beerOrderService.deleteBeerOrder(orderId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
```

### 7. Exception Handling

Implement centralized exception handling:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationExceptions(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Validation error");
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        problemDetail.setProperty("errors", errors);
        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleEntityNotFound(EntityNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    // Add more exception handlers as needed
}
```

### 8. Database Migration

Use Flyway for database migrations:

```sql
-- V2__create_customer_table.sql
CREATE TABLE customer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    version INT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    created_date TIMESTAMP,
    update_date TIMESTAMP
);

-- V3__create_beer_order_tables.sql
CREATE TABLE beer_order (
    id INT AUTO_INCREMENT PRIMARY KEY,
    version INT,
    order_status VARCHAR(50),
    order_status_callback_url VARCHAR(255),
    customer_id INT,
    created_date TIMESTAMP,
    update_date TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);

CREATE TABLE beer_order_line (
    id INT AUTO_INCREMENT PRIMARY KEY,
    version INT,
    order_quantity INT,
    quantity_allocated INT,
    beer_order_id INT,
    beer_id INT,
    created_date TIMESTAMP,
    update_date TIMESTAMP,
    FOREIGN KEY (beer_order_id) REFERENCES beer_order(id),
    FOREIGN KEY (beer_id) REFERENCES beer(id)
);

CREATE TABLE beer_inventory (
    id INT AUTO_INCREMENT PRIMARY KEY,
    version INT,
    quantity_on_hand INT,
    beer_id INT,
    created_date TIMESTAMP,
    update_date TIMESTAMP,
    FOREIGN KEY (beer_id) REFERENCES beer(id)
);
```

### 9. Testing

Implement comprehensive testing:

1. **Unit Tests**: Test individual components in isolation with mocked dependencies
2. **Integration Tests**: Test interactions between components using Testcontainers
3. **API Tests**: Test REST endpoints using MockMvc or TestRestTemplate

## Implementation Best Practices

1. **Constructor Injection**: Use constructor injection for all dependencies
2. **Package-Private Visibility**: Use package-private visibility for internal components
3. **Typed Properties**: Use `@ConfigurationProperties` for configuration
4. **Transaction Boundaries**: Define clear transaction boundaries at the service layer
5. **Disable OSIV**: Set `spring.jpa.open-in-view=false` in application.properties
6. **Entity-DTO Separation**: Never expose entities directly in controllers
7. **REST API Design**: Follow REST API design principles
8. **Command Objects**: Use command objects for business operations
9. **Exception Handling**: Centralize exception handling
10. **Logging**: Use proper logging with SLF4J