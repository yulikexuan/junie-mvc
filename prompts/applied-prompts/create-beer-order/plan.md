# Beer Order System Implementation Plan

## Overview

This document outlines the implementation plan for adding a Beer Order System to the existing project. The system will manage customers, beer orders, order lines, and inventory based on the requirements specified in `requirements.md`.

## Implementation Plan

### 1. Entity Implementation

1. **Update Beer Entity**
   - Add relationships with BeerInventory and BeerOrderLine
   - Add @Table annotation with name "beer"
   - Configure bidirectional relationships

   ```java
   package spring.start.here.juniemvc.domain.model;

   import jakarta.persistence.Column;
   import jakarta.persistence.Entity;
   import jakarta.persistence.GeneratedValue;
   import jakarta.persistence.GenerationType;
   import jakarta.persistence.Id;
   import jakarta.persistence.OneToMany;
   import jakarta.persistence.Table;
   import jakarta.persistence.Version;
   import lombok.AllArgsConstructor;
   import lombok.Builder;
   import lombok.Getter;
   import lombok.NoArgsConstructor;
   import lombok.Setter;
   import lombok.ToString;
   import org.hibernate.annotations.CreationTimestamp;
   import org.hibernate.annotations.UpdateTimestamp;

   import java.math.BigDecimal;
   import java.time.LocalDateTime;
   import java.util.HashSet;
   import java.util.Set;

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

2. **Create Customer Entity**
   - Implement with fields: id, version, name, email, phone, timestamps
   - Add relationship with BeerOrder
   - Configure proper JPA annotations and Lombok annotations

   ```java
   package spring.start.here.juniemvc.domain.model;

   import jakarta.persistence.Column;
   import jakarta.persistence.Entity;
   import jakarta.persistence.GeneratedValue;
   import jakarta.persistence.GenerationType;
   import jakarta.persistence.Id;
   import jakarta.persistence.OneToMany;
   import jakarta.persistence.Table;
   import jakarta.persistence.Version;
   import lombok.AllArgsConstructor;
   import lombok.Builder;
   import lombok.Getter;
   import lombok.NoArgsConstructor;
   import lombok.Setter;
   import lombok.ToString;
   import org.hibernate.annotations.CreationTimestamp;
   import org.hibernate.annotations.UpdateTimestamp;

   import java.time.LocalDateTime;
   import java.util.HashSet;
   import java.util.Set;

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
       @OneToMany(mappedBy = "customer", cascade = jakarta.persistence.CascadeType.ALL)
       @Builder.Default
       @ToString.Exclude
       private Set<BeerOrder> beerOrders = new HashSet<>();
   }
   ```

3. **Create BeerOrder Entity**
   - Implement with fields: id, version, orderStatus, orderStatusCallbackUrl, timestamps
   - Add relationships with Customer and BeerOrderLine
   - Configure proper JPA annotations and Lombok annotations

   ```java
   package spring.start.here.juniemvc.domain.model;

   import jakarta.persistence.CascadeType;
   import jakarta.persistence.Column;
   import jakarta.persistence.Entity;
   import jakarta.persistence.FetchType;
   import jakarta.persistence.GeneratedValue;
   import jakarta.persistence.GenerationType;
   import jakarta.persistence.Id;
   import jakarta.persistence.JoinColumn;
   import jakarta.persistence.ManyToOne;
   import jakarta.persistence.OneToMany;
   import jakarta.persistence.Table;
   import jakarta.persistence.Version;
   import lombok.AllArgsConstructor;
   import lombok.Builder;
   import lombok.Getter;
   import lombok.NoArgsConstructor;
   import lombok.Setter;
   import lombok.ToString;
   import org.hibernate.annotations.CreationTimestamp;
   import org.hibernate.annotations.UpdateTimestamp;

   import java.time.LocalDateTime;
   import java.util.HashSet;
   import java.util.Set;

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

4. **Create BeerOrderLine Entity**
   - Implement with fields: id, version, orderQuantity, quantityAllocated, timestamps
   - Add relationships with BeerOrder and Beer
   - Configure proper JPA annotations and Lombok annotations

   ```java
   package spring.start.here.juniemvc.domain.model;

   import jakarta.persistence.Column;
   import jakarta.persistence.Entity;
   import jakarta.persistence.FetchType;
   import jakarta.persistence.GeneratedValue;
   import jakarta.persistence.GenerationType;
   import jakarta.persistence.Id;
   import jakarta.persistence.JoinColumn;
   import jakarta.persistence.ManyToOne;
   import jakarta.persistence.Table;
   import jakarta.persistence.Version;
   import lombok.AllArgsConstructor;
   import lombok.Builder;
   import lombok.Getter;
   import lombok.NoArgsConstructor;
   import lombok.Setter;
   import org.hibernate.annotations.CreationTimestamp;
   import org.hibernate.annotations.UpdateTimestamp;

   import java.time.LocalDateTime;

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

5. **Create BeerInventory Entity**
   - Implement with fields: id, version, quantityOnHand, timestamps
   - Add relationship with Beer
   - Configure proper JPA annotations and Lombok annotations

   ```java
   package spring.start.here.juniemvc.domain.model;

   import jakarta.persistence.Column;
   import jakarta.persistence.Entity;
   import jakarta.persistence.FetchType;
   import jakarta.persistence.GeneratedValue;
   import jakarta.persistence.GenerationType;
   import jakarta.persistence.Id;
   import jakarta.persistence.JoinColumn;
   import jakarta.persistence.ManyToOne;
   import jakarta.persistence.Table;
   import jakarta.persistence.Version;
   import lombok.AllArgsConstructor;
   import lombok.Builder;
   import lombok.Getter;
   import lombok.NoArgsConstructor;
   import lombok.Setter;
   import org.hibernate.annotations.CreationTimestamp;
   import org.hibernate.annotations.UpdateTimestamp;

   import java.time.LocalDateTime;

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

1. **Create CustomerRepository**
   - Extend JpaRepository with Customer entity and Integer ID

   ```java
   package spring.start.here.juniemvc.repository;

   import org.springframework.data.jpa.repository.JpaRepository;
   import org.springframework.stereotype.Repository;
   import spring.start.here.juniemvc.domain.model.Customer;

   @Repository
   public interface CustomerRepository extends JpaRepository<Customer, Integer> {
       // Custom query methods can be added as needed
   }
   ```

2. **Create BeerOrderRepository**
   - Extend JpaRepository with BeerOrder entity and Integer ID
   - Add method to find orders by customer

   ```java
   package spring.start.here.juniemvc.repository;

   import org.springframework.data.jpa.repository.JpaRepository;
   import org.springframework.stereotype.Repository;
   import spring.start.here.juniemvc.domain.model.BeerOrder;
   import spring.start.here.juniemvc.domain.model.Customer;

   import java.util.List;

   @Repository
   public interface BeerOrderRepository extends JpaRepository<BeerOrder, Integer> {
       /**
        * Find all beer orders for a specific customer
        * @param customer the customer
        * @return list of beer orders
        */
       List<BeerOrder> findAllByCustomer(Customer customer);
   }
   ```

3. **Create BeerOrderLineRepository**
   - Extend JpaRepository with BeerOrderLine entity and Integer ID

   ```java
   package spring.start.here.juniemvc.repository;

   import org.springframework.data.jpa.repository.JpaRepository;
   import org.springframework.stereotype.Repository;
   import spring.start.here.juniemvc.domain.model.BeerOrderLine;

   @Repository
   public interface BeerOrderLineRepository extends JpaRepository<BeerOrderLine, Integer> {
       // Custom query methods can be added as needed
   }
   ```

4. **Create BeerInventoryRepository**
   - Extend JpaRepository with BeerInventory entity and Integer ID
   - Add method to find inventory by beer

   ```java
   package spring.start.here.juniemvc.repository;

   import org.springframework.data.jpa.repository.JpaRepository;
   import org.springframework.stereotype.Repository;
   import spring.start.here.juniemvc.domain.model.Beer;
   import spring.start.here.juniemvc.domain.model.BeerInventory;

   import java.util.List;

   @Repository
   public interface BeerInventoryRepository extends JpaRepository<BeerInventory, Integer> {
       /**
        * Find all inventory records for a specific beer
        * @param beer the beer
        * @return list of beer inventory records
        */
       List<BeerInventory> findAllByBeer(Beer beer);
   }
   ```

### 3. DTO Implementation

1. **Create CustomerDto**
   - Include all relevant fields from Customer entity

   ```java
   package spring.start.here.juniemvc.web.model;

   import lombok.AllArgsConstructor;
   import lombok.Builder;
   import lombok.Getter;
   import lombok.NoArgsConstructor;
   import lombok.Setter;

   import java.time.LocalDateTime;

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

2. **Create BeerOrderDto**
   - Include all relevant fields from BeerOrder entity
   - Include customer information and list of order lines

   ```java
   package spring.start.here.juniemvc.web.model;

   import lombok.AllArgsConstructor;
   import lombok.Builder;
   import lombok.Getter;
   import lombok.NoArgsConstructor;
   import lombok.Setter;

   import java.time.LocalDateTime;
   import java.util.ArrayList;
   import java.util.List;

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
       
       @Builder.Default
       private List<BeerOrderLineDto> beerOrderLines = new ArrayList<>();
   }
   ```

3. **Create BeerOrderLineDto**
   - Include all relevant fields from BeerOrderLine entity
   - Include beer information

   ```java
   package spring.start.here.juniemvc.web.model;

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

4. **Create BeerInventoryDto**
   - Include all relevant fields from BeerInventory entity
   - Include beer information

   ```java
   package spring.start.here.juniemvc.web.model;

   import lombok.AllArgsConstructor;
   import lombok.Builder;
   import lombok.Getter;
   import lombok.NoArgsConstructor;
   import lombok.Setter;

   import java.time.LocalDateTime;

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

### 4. Command Objects Implementation

1. **Create CreateBeerOrderCommand**
   - Implement as a record with customerId and list of BeerOrderLineCommand
   - Add validation annotations

   ```java
   package spring.start.here.juniemvc.web.model.command;

   import jakarta.validation.Valid;
   import jakarta.validation.constraints.NotEmpty;
   import jakarta.validation.constraints.NotNull;

   import java.util.List;

   /**
    * Command object for creating a new beer order
    */
   public record CreateBeerOrderCommand(
       @NotNull(message = "Customer ID is required")
       Integer customerId,
       
       @NotEmpty(message = "Order must contain at least one beer")
       @Valid
       List<BeerOrderLineCommand> beerOrderLines
   ) {}
   ```

2. **Create BeerOrderLineCommand**
   - Implement as a record with beerId and orderQuantity
   - Add validation annotations

   ```java
   package spring.start.here.juniemvc.web.model.command;

   import jakarta.validation.constraints.NotNull;
   import jakarta.validation.constraints.Positive;

   /**
    * Command object for a beer order line item
    */
   public record BeerOrderLineCommand(
       @NotNull(message = "Beer ID is required")
       Integer beerId,
       
       @NotNull(message = "Order quantity is required")
       @Positive(message = "Order quantity must be positive")
       Integer orderQuantity
   ) {}
   ```

### 5. Mapper Implementation

1. **Create CustomerMapper**
   - Implement methods to convert between Customer entity and CustomerDto

   ```java
   package spring.start.here.juniemvc.web.mappers;

   import org.mapstruct.Mapper;
   import org.mapstruct.Mapping;
   import spring.start.here.juniemvc.domain.model.Customer;
   import spring.start.here.juniemvc.web.model.CustomerDto;

   /**
    * Mapper for converting between Customer entity and CustomerDto
    */
   @Mapper(componentModel = "spring")
   public interface CustomerMapper {

       /**
        * Convert Customer entity to CustomerDto
        * @param customer the customer entity
        * @return the customer DTO
        */
       CustomerDto customerToCustomerDto(Customer customer);

       /**
        * Convert CustomerDto to Customer entity
        * @param customerDto the customer DTO
        * @return the customer entity
        */
       Customer customerDtoToCustomer(CustomerDto customerDto);
   }
   ```

2. **Create BeerOrderMapper**
   - Implement methods to convert between BeerOrder entity and BeerOrderDto
   - Handle nested BeerOrderLine conversion

   ```java
   package spring.start.here.juniemvc.web.mappers;

   import org.mapstruct.Mapper;
   import org.mapstruct.Mapping;
   import spring.start.here.juniemvc.domain.model.BeerOrder;
   import spring.start.here.juniemvc.web.model.BeerOrderDto;

   /**
    * Mapper for converting between BeerOrder entity and BeerOrderDto
    */
   @Mapper(componentModel = "spring", uses = {BeerOrderLineMapper.class})
   public interface BeerOrderMapper {

       /**
        * Convert BeerOrder entity to BeerOrderDto
        * @param beerOrder the beer order entity
        * @return the beer order DTO
        */
       @Mapping(target = "customerId", source = "customer.id")
       @Mapping(target = "customerName", source = "customer.name")
       BeerOrderDto beerOrderToBeerOrderDto(BeerOrder beerOrder);

       /**
        * Convert BeerOrderDto to BeerOrder entity
        * Note: This is a partial mapping as customer needs to be set separately
        * @param beerOrderDto the beer order DTO
        * @return the beer order entity
        */
       @Mapping(target = "customer", ignore = true)
       BeerOrder beerOrderDtoToBeerOrder(BeerOrderDto beerOrderDto);
   }
   ```

3. **Create BeerOrderLineMapper**
   - Implement methods to convert between BeerOrderLine entity and BeerOrderLineDto

   ```java
   package spring.start.here.juniemvc.web.mappers;

   import org.mapstruct.Mapper;
   import org.mapstruct.Mapping;
   import spring.start.here.juniemvc.domain.model.BeerOrderLine;
   import spring.start.here.juniemvc.web.model.BeerOrderLineDto;

   /**
    * Mapper for converting between BeerOrderLine entity and BeerOrderLineDto
    */
   @Mapper(componentModel = "spring")
   public interface BeerOrderLineMapper {

       /**
        * Convert BeerOrderLine entity to BeerOrderLineDto
        * @param beerOrderLine the beer order line entity
        * @return the beer order line DTO
        */
       @Mapping(target = "beerId", source = "beer.id")
       @Mapping(target = "beerName", source = "beer.beerName")
       @Mapping(target = "beerStyle", source = "beer.beerStyle")
       @Mapping(target = "price", source = "beer.price")
       BeerOrderLineDto beerOrderLineToBeerOrderLineDto(BeerOrderLine beerOrderLine);

       /**
        * Convert BeerOrderLineDto to BeerOrderLine entity
        * Note: This is a partial mapping as beer and beerOrder need to be set separately
        * @param beerOrderLineDto the beer order line DTO
        * @return the beer order line entity
        */
       @Mapping(target = "beer", ignore = true)
       @Mapping(target = "beerOrder", ignore = true)
       BeerOrderLine beerOrderLineDtoToBeerOrderLine(BeerOrderLineDto beerOrderLineDto);
   }
   ```

4. **Create BeerInventoryMapper**
   - Implement methods to convert between BeerInventory entity and BeerInventoryDto

   ```java
   package spring.start.here.juniemvc.web.mappers;

   import org.mapstruct.Mapper;
   import org.mapstruct.Mapping;
   import spring.start.here.juniemvc.domain.model.BeerInventory;
   import spring.start.here.juniemvc.web.model.BeerInventoryDto;

   /**
    * Mapper for converting between BeerInventory entity and BeerInventoryDto
    */
   @Mapper(componentModel = "spring")
   public interface BeerInventoryMapper {

       /**
        * Convert BeerInventory entity to BeerInventoryDto
        * @param beerInventory the beer inventory entity
        * @return the beer inventory DTO
        */
       @Mapping(target = "beerId", source = "beer.id")
       @Mapping(target = "beerName", source = "beer.beerName")
       BeerInventoryDto beerInventoryToBeerInventoryDto(BeerInventory beerInventory);

       /**
        * Convert BeerInventoryDto to BeerInventory entity
        * Note: This is a partial mapping as beer needs to be set separately
        * @param beerInventoryDto the beer inventory DTO
        * @return the beer inventory entity
        */
       @Mapping(target = "beer", ignore = true)
       BeerInventory beerInventoryDtoToBeerInventory(BeerInventoryDto beerInventoryDto);
   }
   ```

### 6. Service Layer Implementation

1. **Create CustomerService and Implementation**
   - Implement CRUD operations for Customer entity
   - Define proper transaction boundaries

   ```java
   // CustomerService.java
   package spring.start.here.juniemvc.service;

   import spring.start.here.juniemvc.web.model.CustomerDto;

   import java.util.List;
   import java.util.Optional;

   /**
    * Service interface for Customer operations
    */
   public interface CustomerService {
       /**
        * Create a new customer
        * @param customerDto the customer to create
        * @return the created customer
        */
       CustomerDto createCustomer(CustomerDto customerDto);

       /**
        * Get a customer by ID
        * @param id the customer ID
        * @return the customer if found
        */
       Optional<CustomerDto> getCustomerById(Integer id);

       /**
        * Get all customers with pagination
        * @param pageNumber the page number (0-based)
        * @param pageSize the page size
        * @return list of all customers
        */
       List<CustomerDto> getAllCustomers(Integer pageNumber, Integer pageSize);

       /**
        * Update an existing customer
        * @param id the customer ID to update
        * @param customerDto the updated customer data
        * @return the updated customer if found
        */
       Optional<CustomerDto> updateCustomer(Integer id, CustomerDto customerDto);

       /**
        * Delete a customer by ID
        * @param id the customer ID to delete
        * @return true if customer was deleted, false if not found
        */
       boolean deleteCustomer(Integer id);
   }
   ```

   ```java
   // CustomerServiceImpl.java
   package spring.start.here.juniemvc.service;

   import lombok.RequiredArgsConstructor;
   import org.springframework.data.domain.PageRequest;
   import org.springframework.stereotype.Service;
   import org.springframework.transaction.annotation.Transactional;
   import spring.start.here.juniemvc.domain.model.Customer;
   import spring.start.here.juniemvc.repository.CustomerRepository;
   import spring.start.here.juniemvc.web.mappers.CustomerMapper;
   import spring.start.here.juniemvc.web.model.CustomerDto;

   import java.util.List;
   import java.util.Optional;
   import java.util.stream.Collectors;

   /**
    * Implementation of CustomerService
    */
   @Service
   @RequiredArgsConstructor
   public class CustomerServiceImpl implements CustomerService {
       private final CustomerRepository customerRepository;
       private final CustomerMapper customerMapper;

       @Override
       @Transactional
       public CustomerDto createCustomer(CustomerDto customerDto) {
           Customer customer = customerMapper.customerDtoToCustomer(customerDto);
           Customer savedCustomer = customerRepository.save(customer);
           return customerMapper.customerToCustomerDto(savedCustomer);
       }

       @Override
       @Transactional(readOnly = true)
       public Optional<CustomerDto> getCustomerById(Integer id) {
           return customerRepository.findById(id)
                   .map(customerMapper::customerToCustomerDto);
       }

       @Override
       @Transactional(readOnly = true)
       public List<CustomerDto> getAllCustomers(Integer pageNumber, Integer pageSize) {
           PageRequest pageRequest = PageRequest.of(
                   pageNumber != null ? pageNumber : 0,
                   pageSize != null ? pageSize : 25);

           return customerRepository.findAll(pageRequest)
                   .stream()
                   .map(customerMapper::customerToCustomerDto)
                   .collect(Collectors.toList());
       }

       @Override
       @Transactional
       public Optional<CustomerDto> updateCustomer(Integer id, CustomerDto customerDto) {
           return customerRepository.findById(id)
                   .map(existingCustomer -> {
                       customerDto.setId(id);
                       Customer customer = customerMapper.customerDtoToCustomer(customerDto);
                       Customer savedCustomer = customerRepository.save(customer);
                       return customerMapper.customerToCustomerDto(savedCustomer);
                   });
       }

       @Override
       @Transactional
       public boolean deleteCustomer(Integer id) {
           return customerRepository.findById(id)
                   .map(customer -> {
                       customerRepository.delete(customer);
                       return true;
                   })
                   .orElse(false);
       }
   }
   ```

2. **Create BeerOrderService and Implementation**
   - Implement methods for creating, retrieving, updating, and deleting beer orders
   - Implement method to update order status
   - Define proper transaction boundaries

   ```java
   // BeerOrderService.java
   package spring.start.here.juniemvc.service;

   import spring.start.here.juniemvc.web.model.BeerOrderDto;
   import spring.start.here.juniemvc.web.model.command.CreateBeerOrderCommand;

   import java.util.List;
   import java.util.Optional;

   /**
    * Service interface for BeerOrder operations
    */
   public interface BeerOrderService {
       /**
        * Create a new beer order
        * @param command the command to create a beer order
        * @return the created beer order
        */
       BeerOrderDto createBeerOrder(CreateBeerOrderCommand command);

       /**
        * Get a beer order by ID
        * @param id the beer order ID
        * @return the beer order if found
        */
       Optional<BeerOrderDto> getBeerOrderById(Integer id);

       /**
        * Get all beer orders with pagination
        * @param pageNumber the page number (0-based)
        * @param pageSize the page size
        * @return list of all beer orders
        */
       List<BeerOrderDto> getAllBeerOrders(Integer pageNumber, Integer pageSize);

       /**
        * Get beer orders by customer with pagination
        * @param customerId the customer ID
        * @param pageNumber the page number (0-based)
        * @param pageSize the page size
        * @return list of beer orders for the customer
        */
       List<BeerOrderDto> getBeerOrdersByCustomer(Integer customerId, Integer pageNumber, Integer pageSize);

       /**
        * Update beer order status
        * @param id the beer order ID
        * @param orderStatus the new order status
        * @return the updated beer order if found
        */
       Optional<BeerOrderDto> updateBeerOrderStatus(Integer id, String orderStatus);

       /**
        * Delete a beer order by ID
        * @param id the beer order ID to delete
        * @return true if beer order was deleted, false if not found
        */
       boolean deleteBeerOrder(Integer id);
   }
   ```

   ```java
   // BeerOrderServiceImpl.java
   package spring.start.here.juniemvc.service;

   import lombok.RequiredArgsConstructor;
   import org.springframework.data.domain.PageRequest;
   import org.springframework.stereotype.Service;
   import org.springframework.transaction.annotation.Transactional;
   import spring.start.here.juniemvc.domain.model.Beer;
   import spring.start.here.juniemvc.domain.model.BeerOrder;
   import spring.start.here.juniemvc.domain.model.BeerOrderLine;
   import spring.start.here.juniemvc.domain.model.Customer;
   import spring.start.here.juniemvc.repository.BeerOrderRepository;
   import spring.start.here.juniemvc.repository.BeerRepository;
   import spring.start.here.juniemvc.repository.CustomerRepository;
   import spring.start.here.juniemvc.web.mappers.BeerOrderMapper;
   import spring.start.here.juniemvc.web.model.BeerOrderDto;
   import spring.start.here.juniemvc.web.model.command.BeerOrderLineCommand;
   import spring.start.here.juniemvc.web.model.command.CreateBeerOrderCommand;

   import java.util.HashSet;
   import java.util.List;
   import java.util.Optional;
   import java.util.Set;
   import java.util.stream.Collectors;

   /**
    * Implementation of BeerOrderService
    */
   @Service
   @RequiredArgsConstructor
   public class BeerOrderServiceImpl implements BeerOrderService {
       private final BeerOrderRepository beerOrderRepository;
       private final CustomerRepository customerRepository;
       private final BeerRepository beerRepository;
       private final BeerOrderMapper beerOrderMapper;

       @Override
       @Transactional
       public BeerOrderDto createBeerOrder(CreateBeerOrderCommand command) {
           Customer customer = customerRepository.findById(command.customerId())
                   .orElseThrow(() -> new RuntimeException("Customer not found: " + command.customerId()));

           BeerOrder beerOrder = BeerOrder.builder()
                   .customer(customer)
                   .orderStatus("NEW")
                   .beerOrderLines(new HashSet<>())
                   .build();

           // Add beer order lines
           Set<BeerOrderLine> orderLines = command.beerOrderLines().stream()
                   .map(line -> createOrderLine(beerOrder, line))
                   .collect(Collectors.toSet());

           beerOrder.setBeerOrderLines(orderLines);
           BeerOrder savedBeerOrder = beerOrderRepository.save(beerOrder);

           return beerOrderMapper.beerOrderToBeerOrderDto(savedBeerOrder);
       }

       private BeerOrderLine createOrderLine(BeerOrder beerOrder, BeerOrderLineCommand line) {
           Beer beer = beerRepository.findById(line.beerId())
                   .orElseThrow(() -> new RuntimeException("Beer not found: " + line.beerId()));

           return BeerOrderLine.builder()
                   .beer(beer)
                   .beerOrder(beerOrder)
                   .orderQuantity(line.orderQuantity())
                   .quantityAllocated(0)
                   .build();
       }

       @Override
       @Transactional(readOnly = true)
       public Optional<BeerOrderDto> getBeerOrderById(Integer id) {
           return beerOrderRepository.findById(id)
                   .map(beerOrderMapper::beerOrderToBeerOrderDto);
       }

       @Override
       @Transactional(readOnly = true)
       public List<BeerOrderDto> getAllBeerOrders(Integer pageNumber, Integer pageSize) {
           PageRequest pageRequest = PageRequest.of(
                   pageNumber != null ? pageNumber : 0,
                   pageSize != null ? pageSize : 25);

           return beerOrderRepository.findAll(pageRequest)
                   .stream()
                   .map(beerOrderMapper::beerOrderToBeerOrderDto)
                   .collect(Collectors.toList());
       }

       @Override
       @Transactional(readOnly = true)
       public List<BeerOrderDto> getBeerOrdersByCustomer(Integer customerId, Integer pageNumber, Integer pageSize) {
           Customer customer = customerRepository.findById(customerId)
                   .orElseThrow(() -> new RuntimeException("Customer not found: " + customerId));

           PageRequest pageRequest = PageRequest.of(
                   pageNumber != null ? pageNumber : 0,
                   pageSize != null ? pageSize : 25);

           return beerOrderRepository.findAllByCustomer(customer)
                   .stream()
                   .map(beerOrderMapper::beerOrderToBeerOrderDto)
                   .collect(Collectors.toList());
       }

       @Override
       @Transactional
       public Optional<BeerOrderDto> updateBeerOrderStatus(Integer id, String orderStatus) {
           return beerOrderRepository.findById(id)
                   .map(beerOrder -> {
                       beerOrder.setOrderStatus(orderStatus);
                       return beerOrderMapper.beerOrderToBeerOrderDto(beerOrderRepository.save(beerOrder));
                   });
       }

       @Override
       @Transactional
       public boolean deleteBeerOrder(Integer id) {
           return beerOrderRepository.findById(id)
                   .map(beerOrder -> {
                       beerOrderRepository.delete(beerOrder);
                       return true;
                   })
                   .orElse(false);
       }
   }
   ```

3. **Create BeerInventoryService and Implementation**
   - Implement methods for managing beer inventory
   - Define proper transaction boundaries

   ```java
   // BeerInventoryService.java
   package spring.start.here.juniemvc.service;

   import spring.start.here.juniemvc.web.model.BeerInventoryDto;

   import java.util.List;
   import java.util.Optional;

   /**
    * Service interface for BeerInventory operations
    */
   public interface BeerInventoryService {
       /**
        * Create a new beer inventory record
        * @param beerId the beer ID
        * @param quantityOnHand the quantity on hand
        * @return the created beer inventory
        */
       BeerInventoryDto createBeerInventory(Integer beerId, Integer quantityOnHand);

       /**
        * Get a beer inventory by ID
        * @param id the beer inventory ID
        * @return the beer inventory if found
        */
       Optional<BeerInventoryDto> getBeerInventoryById(Integer id);

       /**
        * Get all inventory records for a beer
        * @param beerId the beer ID
        * @return list of inventory records for the beer
        */
       List<BeerInventoryDto> getBeerInventoryByBeer(Integer beerId);

       /**
        * Update beer inventory quantity
        * @param id the beer inventory ID
        * @param quantityOnHand the new quantity on hand
        * @return the updated beer inventory if found
        */
       Optional<BeerInventoryDto> updateBeerInventoryQuantity(Integer id, Integer quantityOnHand);

       /**
        * Delete a beer inventory by ID
        * @param id the beer inventory ID to delete
        * @return true if beer inventory was deleted, false if not found
        */
       boolean deleteBeerInventory(Integer id);
   }
   ```

   ```java
   // BeerInventoryServiceImpl.java
   package spring.start.here.juniemvc.service;

   import lombok.RequiredArgsConstructor;
   import org.springframework.stereotype.Service;
   import org.springframework.transaction.annotation.Transactional;
   import spring.start.here.juniemvc.domain.model.Beer;
   import spring.start.here.juniemvc.domain.model.BeerInventory;
   import spring.start.here.juniemvc.repository.BeerInventoryRepository;
   import spring.start.here.juniemvc.repository.BeerRepository;
   import spring.start.here.juniemvc.web.mappers.BeerInventoryMapper;
   import spring.start.here.juniemvc.web.model.BeerInventoryDto;

   import java.util.List;
   import java.util.Optional;
   import java.util.stream.Collectors;

   /**
    * Implementation of BeerInventoryService
    */
   @Service
   @RequiredArgsConstructor
   public class BeerInventoryServiceImpl implements BeerInventoryService {
       private final BeerInventoryRepository beerInventoryRepository;
       private final BeerRepository beerRepository;
       private final BeerInventoryMapper beerInventoryMapper;

       @Override
       @Transactional
       public BeerInventoryDto createBeerInventory(Integer beerId, Integer quantityOnHand) {
           Beer beer = beerRepository.findById(beerId)
                   .orElseThrow(() -> new RuntimeException("Beer not found: " + beerId));

           BeerInventory beerInventory = BeerInventory.builder()
                   .beer(beer)
                   .quantityOnHand(quantityOnHand)
                   .build();

           BeerInventory savedBeerInventory = beerInventoryRepository.save(beerInventory);
           return beerInventoryMapper.beerInventoryToBeerInventoryDto(savedBeerInventory);
       }

       @Override
       @Transactional(readOnly = true)
       public Optional<BeerInventoryDto> getBeerInventoryById(Integer id) {
           return beerInventoryRepository.findById(id)
                   .map(beerInventoryMapper::beerInventoryToBeerInventoryDto);
       }

       @Override
       @Transactional(readOnly = true)
       public List<BeerInventoryDto> getBeerInventoryByBeer(Integer beerId) {
           Beer beer = beerRepository.findById(beerId)
                   .orElseThrow(() -> new RuntimeException("Beer not found: " + beerId));

           return beerInventoryRepository.findAllByBeer(beer)
                   .stream()
                   .map(beerInventoryMapper::beerInventoryToBeerInventoryDto)
                   .collect(Collectors.toList());
       }

       @Override
       @Transactional
       public Optional<BeerInventoryDto> updateBeerInventoryQuantity(Integer id, Integer quantityOnHand) {
           return beerInventoryRepository.findById(id)
                   .map(beerInventory -> {
                       beerInventory.setQuantityOnHand(quantityOnHand);
                       return beerInventoryMapper.beerInventoryToBeerInventoryDto(
                               beerInventoryRepository.save(beerInventory));
                   });
       }

       @Override
       @Transactional
       public boolean deleteBeerInventory(Integer id) {
           return beerInventoryRepository.findById(id)
                   .map(beerInventory -> {
                       beerInventoryRepository.delete(beerInventory);
                       return true;
                   })
                   .orElse(false);
       }
   }
   ```

### 7. Controller Implementation

1. **Create CustomerController**
   - Implement REST endpoints for Customer operations
   - Follow REST API design principles

   ```java
   package spring.start.here.juniemvc.web.controller;

   import jakarta.validation.Valid;
   import lombok.RequiredArgsConstructor;
   import org.springframework.http.HttpStatus;
   import org.springframework.http.ResponseEntity;
   import org.springframework.web.bind.annotation.*;
   import spring.start.here.juniemvc.service.CustomerService;
   import spring.start.here.juniemvc.web.model.CustomerDto;

   import java.util.List;

   /**
    * REST Controller for Customer operations
    */
   @RestController
   @RequestMapping("/api/v1/customers")
   @RequiredArgsConstructor
   class CustomerController {
       private final CustomerService customerService;

       /**
        * Create a new customer
        * @param customerDto the customer to create
        * @return the created customer
        */
       @PostMapping
       @ResponseStatus(HttpStatus.CREATED)
       CustomerDto createCustomer(@Valid @RequestBody CustomerDto customerDto) {
           return customerService.createCustomer(customerDto);
       }

       /**
        * Get a customer by ID
        * @param customerId the customer ID
        * @return the customer if found, or 404 if not found
        */
       @GetMapping("/{customerId}")
       ResponseEntity<CustomerDto> getCustomerById(@PathVariable("customerId") Integer customerId) {
           return customerService.getCustomerById(customerId)
                   .map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
       }

       /**
        * Get all customers with pagination
        * @param pageNumber the page number (0-based)
        * @param pageSize the page size
        * @return list of all customers
        */
       @GetMapping
       List<CustomerDto> getAllCustomers(
               @RequestParam(required = false) Integer pageNumber,
               @RequestParam(required = false) Integer pageSize) {
           return customerService.getAllCustomers(pageNumber, pageSize);
       }

       /**
        * Update an existing customer
        * @param customerId the customer ID to update
        * @param customerDto the updated customer data
        * @return the updated customer if found, or 404 if not found
        */
       @PutMapping("/{customerId}")
       ResponseEntity<CustomerDto> updateCustomer(
               @PathVariable("customerId") Integer customerId,
               @Valid @RequestBody CustomerDto customerDto) {
           return customerService.updateCustomer(customerId, customerDto)
                   .map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
       }

       /**
        * Delete a customer by ID
        * @param customerId the customer ID to delete
        * @return 204 No Content if deleted, or 404 if not found
        */
       @DeleteMapping("/{customerId}")
       ResponseEntity<Void> deleteCustomer(@PathVariable("customerId") Integer customerId) {
           return customerService.deleteCustomer(customerId)
                   ? ResponseEntity.noContent().build()
                   : ResponseEntity.notFound().build();
       }
   }
   ```

2. **Create BeerOrderController**
   - Implement REST endpoints for BeerOrder operations
   - Include endpoints for creating orders, retrieving orders, updating status, etc.
   - Follow REST API design principles

   ```java
   package spring.start.here.juniemvc.web.controller;

   import jakarta.validation.Valid;
   import lombok.RequiredArgsConstructor;
   import org.springframework.http.HttpStatus;
   import org.springframework.http.ResponseEntity;
   import org.springframework.web.bind.annotation.*;
   import spring.start.here.juniemvc.service.BeerOrderService;
   import spring.start.here.juniemvc.web.model.BeerOrderDto;
   import spring.start.here.juniemvc.web.model.command.CreateBeerOrderCommand;

   import java.util.List;

   /**
    * REST Controller for BeerOrder operations
    */
   @RestController
   @RequestMapping("/api/v1/beer-orders")
   @RequiredArgsConstructor
   class BeerOrderController {
       private final BeerOrderService beerOrderService;

       /**
        * Create a new beer order
        * @param command the command to create a beer order
        * @return the created beer order
        */
       @PostMapping
       @ResponseStatus(HttpStatus.CREATED)
       BeerOrderDto createBeerOrder(@Valid @RequestBody CreateBeerOrderCommand command) {
           return beerOrderService.createBeerOrder(command);
       }

       /**
        * Get a beer order by ID
        * @param orderId the beer order ID
        * @return the beer order if found, or 404 if not found
        */
       @GetMapping("/{orderId}")
       ResponseEntity<BeerOrderDto> getBeerOrderById(@PathVariable("orderId") Integer orderId) {
           return beerOrderService.getBeerOrderById(orderId)
                   .map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
       }

       /**
        * Get all beer orders with pagination
        * @param pageNumber the page number (0-based)
        * @param pageSize the page size
        * @return list of all beer orders
        */
       @GetMapping
       List<BeerOrderDto> getAllBeerOrders(
               @RequestParam(required = false) Integer pageNumber,
               @RequestParam(required = false) Integer pageSize) {
           return beerOrderService.getAllBeerOrders(pageNumber, pageSize);
       }

       /**
        * Get beer orders by customer with pagination
        * @param customerId the customer ID
        * @param pageNumber the page number (0-based)
        * @param pageSize the page size
        * @return list of beer orders for the customer
        */
       @GetMapping("/customer/{customerId}")
       List<BeerOrderDto> getBeerOrdersByCustomer(
               @PathVariable("customerId") Integer customerId,
               @RequestParam(required = false) Integer pageNumber,
               @RequestParam(required = false) Integer pageSize) {
           return beerOrderService.getBeerOrdersByCustomer(customerId, pageNumber, pageSize);
       }

       /**
        * Update beer order status
        * @param orderId the beer order ID
        * @param orderStatus the new order status
        * @return the updated beer order if found, or 404 if not found
        */
       @PutMapping("/{orderId}/status")
       ResponseEntity<BeerOrderDto> updateBeerOrderStatus(
               @PathVariable("orderId") Integer orderId,
               @RequestParam String orderStatus) {
           return beerOrderService.updateBeerOrderStatus(orderId, orderStatus)
                   .map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
       }

       /**
        * Delete a beer order by ID
        * @param orderId the beer order ID to delete
        * @return 204 No Content if deleted, or 404 if not found
        */
       @DeleteMapping("/{orderId}")
       ResponseEntity<Void> deleteBeerOrder(@PathVariable("orderId") Integer orderId) {
           return beerOrderService.deleteBeerOrder(orderId)
                   ? ResponseEntity.noContent().build()
                   : ResponseEntity.notFound().build();
       }
   }
   ```

3. **Create BeerInventoryController**
   - Implement REST endpoints for BeerInventory operations
   - Follow REST API design principles

   ```java
   package spring.start.here.juniemvc.web.controller;

   import jakarta.validation.Valid;
   import jakarta.validation.constraints.NotNull;
   import jakarta.validation.constraints.Positive;
   import lombok.RequiredArgsConstructor;
   import org.springframework.http.HttpStatus;
   import org.springframework.http.ResponseEntity;
   import org.springframework.web.bind.annotation.*;
   import spring.start.here.juniemvc.service.BeerInventoryService;
   import spring.start.here.juniemvc.web.model.BeerInventoryDto;

   import java.util.List;

   /**
    * REST Controller for BeerInventory operations
    */
   @RestController
   @RequestMapping("/api/v1/beer-inventory")
   @RequiredArgsConstructor
   class BeerInventoryController {
       private final BeerInventoryService beerInventoryService;

       /**
        * Create a new beer inventory record
        * @param beerId the beer ID
        * @param quantityOnHand the quantity on hand
        * @return the created beer inventory
        */
       @PostMapping
       @ResponseStatus(HttpStatus.CREATED)
       BeerInventoryDto createBeerInventory(
               @RequestParam @NotNull Integer beerId,
               @RequestParam @NotNull @Positive Integer quantityOnHand) {
           return beerInventoryService.createBeerInventory(beerId, quantityOnHand);
       }

       /**
        * Get a beer inventory by ID
        * @param inventoryId the beer inventory ID
        * @return the beer inventory if found, or 404 if not found
        */
       @GetMapping("/{inventoryId}")
       ResponseEntity<BeerInventoryDto> getBeerInventoryById(@PathVariable("inventoryId") Integer inventoryId) {
           return beerInventoryService.getBeerInventoryById(inventoryId)
                   .map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
       }

       /**
        * Get all inventory records for a beer
        * @param beerId the beer ID
        * @return list of inventory records for the beer
        */
       @GetMapping("/beer/{beerId}")
       List<BeerInventoryDto> getBeerInventoryByBeer(@PathVariable("beerId") Integer beerId) {
           return beerInventoryService.getBeerInventoryByBeer(beerId);
       }

       /**
        * Update beer inventory quantity
        * @param inventoryId the beer inventory ID
        * @param quantityOnHand the new quantity on hand
        * @return the updated beer inventory if found, or 404 if not found
        */
       @PutMapping("/{inventoryId}")
       ResponseEntity<BeerInventoryDto> updateBeerInventoryQuantity(
               @PathVariable("inventoryId") Integer inventoryId,
               @RequestParam @NotNull @Positive Integer quantityOnHand) {
           return beerInventoryService.updateBeerInventoryQuantity(inventoryId, quantityOnHand)
                   .map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
       }

       /**
        * Delete a beer inventory by ID
        * @param inventoryId the beer inventory ID to delete
        * @return 204 No Content if deleted, or 404 if not found
        */
       @DeleteMapping("/{inventoryId}")
       ResponseEntity<Void> deleteBeerInventory(@PathVariable("inventoryId") Integer inventoryId) {
           return beerInventoryService.deleteBeerInventory(inventoryId)
                   ? ResponseEntity.noContent().build()
                   : ResponseEntity.notFound().build();
       }
   }
   ```

### 8. Database Migration

1. **Create Migration Scripts**
   - Create V2__create_customer_table.sql
   - Create V3__create_beer_order_tables.sql
   - Ensure proper foreign key relationships

   ```sql
   -- V2__create_customer_table.sql
   CREATE TABLE customer (
       id INT AUTO_INCREMENT PRIMARY KEY,
       version INT DEFAULT 0,
       name VARCHAR(100) NOT NULL,
       email VARCHAR(100),
       phone VARCHAR(20),
       created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
   );
   ```

   ```sql
   -- V3__create_beer_order_tables.sql
   -- Beer Order table
   CREATE TABLE beer_order (
       id INT AUTO_INCREMENT PRIMARY KEY,
       version INT DEFAULT 0,
       order_status VARCHAR(50),
       order_status_callback_url VARCHAR(255),
       customer_id INT NOT NULL,
       created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
       FOREIGN KEY (customer_id) REFERENCES customer(id)
   );

   -- Beer Order Line table
   CREATE TABLE beer_order_line (
       id INT AUTO_INCREMENT PRIMARY KEY,
       version INT DEFAULT 0,
       order_quantity INT NOT NULL,
       quantity_allocated INT DEFAULT 0,
       beer_order_id INT NOT NULL,
       beer_id INT NOT NULL,
       created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
       FOREIGN KEY (beer_order_id) REFERENCES beer_order(id),
       FOREIGN KEY (beer_id) REFERENCES beer(id)
   );

   -- Beer Inventory table
   CREATE TABLE beer_inventory (
       id INT AUTO_INCREMENT PRIMARY KEY,
       version INT DEFAULT 0,
       quantity_on_hand INT NOT NULL,
       beer_id INT NOT NULL,
       created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       update_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
       FOREIGN KEY (beer_id) REFERENCES beer(id)
   );
   ```

### 9. Exception Handling

1. **Update GlobalExceptionHandler**
   - Add specific exception handlers for the beer order system
   - Return appropriate HTTP status codes and error messages

   ```java
   package spring.start.here.juniemvc.web.exception;

   import jakarta.persistence.EntityNotFoundException;
   import jakarta.validation.ConstraintViolationException;
   import org.springframework.http.HttpStatus;
   import org.springframework.http.ProblemDetail;
   import org.springframework.http.ResponseEntity;
   import org.springframework.validation.FieldError;
   import org.springframework.web.bind.MethodArgumentNotValidException;
   import org.springframework.web.bind.annotation.ExceptionHandler;
   import org.springframework.web.bind.annotation.RestControllerAdvice;

   import java.util.HashMap;
   import java.util.Map;

   /**
    * Global exception handler for the application
    */
   @RestControllerAdvice
   public class GlobalExceptionHandler {

       /**
        * Handle validation exceptions from @Valid annotations
        */
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

       /**
        * Handle constraint violation exceptions
        */
       @ExceptionHandler(ConstraintViolationException.class)
       public ResponseEntity<ProblemDetail> handleConstraintViolation(ConstraintViolationException ex) {
           ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                   HttpStatus.BAD_REQUEST, "Validation error");
           
           Map<String, String> errors = new HashMap<>();
           ex.getConstraintViolations().forEach(violation -> {
               String fieldName = violation.getPropertyPath().toString();
               String errorMessage = violation.getMessage();
               errors.put(fieldName, errorMessage);
           });
           
           problemDetail.setProperty("errors", errors);
           return ResponseEntity.badRequest().body(problemDetail);
       }

       /**
        * Handle entity not found exceptions
        */
       @ExceptionHandler(EntityNotFoundException.class)
       public ResponseEntity<ProblemDetail> handleEntityNotFound(EntityNotFoundException ex) {
           ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                   HttpStatus.NOT_FOUND, ex.getMessage());
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
       }

       /**
        * Handle custom BeerOrderException
        */
       @ExceptionHandler(BeerOrderException.class)
       public ResponseEntity<ProblemDetail> handleBeerOrderException(BeerOrderException ex) {
           ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                   ex.getStatus(), ex.getMessage());
           return ResponseEntity.status(ex.getStatus()).body(problemDetail);
       }

       /**
        * Handle general exceptions
        */
       @ExceptionHandler(Exception.class)
       public ResponseEntity<ProblemDetail> handleGeneralException(Exception ex) {
           ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                   HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred");
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
       }
   }
   ```

2. **Create Custom Exceptions**
   - Create specific exceptions for the beer order system

   ```java
   package spring.start.here.juniemvc.web.exception;

   import org.springframework.http.HttpStatus;

   /**
    * Custom exception for beer order related errors
    */
   public class BeerOrderException extends RuntimeException {
       private final HttpStatus status;

       public BeerOrderException(String message, HttpStatus status) {
           super(message);
           this.status = status;
       }

       public HttpStatus getStatus() {
           return status;
       }

       /**
        * Factory method for customer not found exception
        */
       public static BeerOrderException customerNotFound(Integer customerId) {
           return new BeerOrderException("Customer not found with ID: " + customerId, HttpStatus.NOT_FOUND);
       }

       /**
        * Factory method for beer not found exception
        */
       public static BeerOrderException beerNotFound(Integer beerId) {
           return new BeerOrderException("Beer not found with ID: " + beerId, HttpStatus.NOT_FOUND);
       }

       /**
        * Factory method for beer order not found exception
        */
       public static BeerOrderException orderNotFound(Integer orderId) {
           return new BeerOrderException("Beer order not found with ID: " + orderId, HttpStatus.NOT_FOUND);
       }

       /**
        * Factory method for insufficient inventory exception
        */
       public static BeerOrderException insufficientInventory(Integer beerId) {
           return new BeerOrderException("Insufficient inventory for beer with ID: " + beerId, 
                   HttpStatus.CONFLICT);
       }
   }
   ```

### 10. Testing

1. **Unit Tests**
   - Create unit tests for service implementations
   - Mock dependencies

   ```java
   package spring.start.here.juniemvc.service;

   import org.junit.jupiter.api.BeforeEach;
   import org.junit.jupiter.api.Test;
   import org.junit.jupiter.api.extension.ExtendWith;
   import org.mockito.InjectMocks;
   import org.mockito.Mock;
   import org.mockito.junit.jupiter.MockitoExtension;
   import org.springframework.data.domain.PageImpl;
   import org.springframework.data.domain.PageRequest;
   import spring.start.here.juniemvc.domain.model.Customer;
   import spring.start.here.juniemvc.repository.CustomerRepository;
   import spring.start.here.juniemvc.web.mappers.CustomerMapper;
   import spring.start.here.juniemvc.web.model.CustomerDto;

   import java.util.Collections;
   import java.util.Optional;

   import static org.assertj.core.api.Assertions.assertThat;
   import static org.mockito.ArgumentMatchers.any;
   import static org.mockito.Mockito.verify;
   import static org.mockito.Mockito.when;

   @ExtendWith(MockitoExtension.class)
   class CustomerServiceImplTest {

       @Mock
       private CustomerRepository customerRepository;

       @Mock
       private CustomerMapper customerMapper;

       @InjectMocks
       private CustomerServiceImpl customerService;

       private Customer customer;
       private CustomerDto customerDto;

       @BeforeEach
       void setUp() {
           customer = Customer.builder()
                   .id(1)
                   .name("Test Customer")
                   .email("test@example.com")
                   .phone("123-456-7890")
                   .build();

           customerDto = CustomerDto.builder()
                   .id(1)
                   .name("Test Customer")
                   .email("test@example.com")
                   .phone("123-456-7890")
                   .build();
       }

       @Test
       void createCustomer() {
           when(customerMapper.customerDtoToCustomer(any(CustomerDto.class))).thenReturn(customer);
           when(customerRepository.save(any(Customer.class))).thenReturn(customer);
           when(customerMapper.customerToCustomerDto(any(Customer.class))).thenReturn(customerDto);

           CustomerDto savedDto = customerService.createCustomer(customerDto);

           assertThat(savedDto).isNotNull();
           assertThat(savedDto.getId()).isEqualTo(customerDto.getId());
           assertThat(savedDto.getName()).isEqualTo(customerDto.getName());
           verify(customerRepository).save(any(Customer.class));
       }

       @Test
       void getCustomerById() {
           when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
           when(customerMapper.customerToCustomerDto(customer)).thenReturn(customerDto);

           Optional<CustomerDto> foundDto = customerService.getCustomerById(1);

           assertThat(foundDto).isPresent();
           assertThat(foundDto.get().getId()).isEqualTo(customerDto.getId());
           verify(customerRepository).findById(1);
       }

       @Test
       void getAllCustomers() {
           PageRequest pageRequest = PageRequest.of(0, 10);
           when(customerRepository.findAll(pageRequest))
                   .thenReturn(new PageImpl<>(Collections.singletonList(customer)));
           when(customerMapper.customerToCustomerDto(customer)).thenReturn(customerDto);

           var customers = customerService.getAllCustomers(0, 10);

           assertThat(customers).hasSize(1);
           assertThat(customers.get(0).getId()).isEqualTo(customerDto.getId());
           verify(customerRepository).findAll(pageRequest);
       }
   }
   ```

2. **Integration Tests**
   - Create integration tests for repositories
   - Use Testcontainers

   ```java
   package spring.start.here.juniemvc.repository;

   import org.junit.jupiter.api.Test;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
   import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
   import org.springframework.test.context.DynamicPropertyRegistry;
   import org.springframework.test.context.DynamicPropertySource;
   import org.testcontainers.containers.MySQLContainer;
   import org.testcontainers.junit.jupiter.Container;
   import org.testcontainers.junit.jupiter.Testcontainers;
   import spring.start.here.juniemvc.domain.model.Customer;
   import spring.start.here.juniemvc.domain.model.BeerOrder;

   import java.util.List;

   import static org.assertj.core.api.Assertions.assertThat;

   @DataJpaTest
   @Testcontainers
   @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
   class BeerOrderRepositoryTest {

       @Container
       static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0")
               .withDatabaseName("testdb")
               .withUsername("test")
               .withPassword("test");

       @DynamicPropertySource
       static void setProperties(DynamicPropertyRegistry registry) {
           registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
           registry.add("spring.datasource.username", mySQLContainer::getUsername);
           registry.add("spring.datasource.password", mySQLContainer::getPassword);
       }

       @Autowired
       private CustomerRepository customerRepository;

       @Autowired
       private BeerOrderRepository beerOrderRepository;

       @Test
       void findAllByCustomer() {
           // Create a customer
           Customer customer = Customer.builder()
                   .name("Test Customer")
                   .email("test@example.com")
                   .phone("123-456-7890")
                   .build();
           Customer savedCustomer = customerRepository.save(customer);

           // Create beer orders for the customer
           BeerOrder order1 = BeerOrder.builder()
                   .customer(savedCustomer)
                   .orderStatus("NEW")
                   .build();
           BeerOrder order2 = BeerOrder.builder()
                   .customer(savedCustomer)
                   .orderStatus("PROCESSING")
                   .build();
           beerOrderRepository.save(order1);
           beerOrderRepository.save(order2);

           // Test findAllByCustomer
           List<BeerOrder> foundOrders = beerOrderRepository.findAllByCustomer(savedCustomer);
           assertThat(foundOrders).hasSize(2);
           assertThat(foundOrders.get(0).getCustomer().getId()).isEqualTo(savedCustomer.getId());
           assertThat(foundOrders.get(1).getCustomer().getId()).isEqualTo(savedCustomer.getId());
       }
   }
   ```

3. **API Tests**
   - Create tests for REST endpoints
   - Use MockMvc or TestRestTemplate

   ```java
   package spring.start.here.juniemvc.web.controller;

   import com.fasterxml.jackson.databind.ObjectMapper;
   import org.junit.jupiter.api.Test;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
   import org.springframework.boot.test.mock.mockito.MockBean;
   import org.springframework.http.MediaType;
   import org.springframework.test.web.servlet.MockMvc;
   import spring.start.here.juniemvc.service.BeerOrderService;
   import spring.start.here.juniemvc.web.model.BeerOrderDto;
   import spring.start.here.juniemvc.web.model.command.BeerOrderLineCommand;
   import spring.start.here.juniemvc.web.model.command.CreateBeerOrderCommand;

   import java.util.Arrays;
   import java.util.List;
   import java.util.Optional;

   import static org.hamcrest.Matchers.hasSize;
   import static org.mockito.ArgumentMatchers.any;
   import static org.mockito.ArgumentMatchers.eq;
   import static org.mockito.Mockito.when;
   import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
   import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

   @WebMvcTest(BeerOrderController.class)
   class BeerOrderControllerTest {

       @Autowired
       private MockMvc mockMvc;

       @Autowired
       private ObjectMapper objectMapper;

       @MockBean
       private BeerOrderService beerOrderService;

       @Test
       void createBeerOrder() throws Exception {
           // Create test data
           CreateBeerOrderCommand command = new CreateBeerOrderCommand(
                   1,
                   List.of(new BeerOrderLineCommand(1, 10))
           );

           BeerOrderDto beerOrderDto = BeerOrderDto.builder()
                   .id(1)
                   .customerId(1)
                   .customerName("Test Customer")
                   .orderStatus("NEW")
                   .build();

           when(beerOrderService.createBeerOrder(any(CreateBeerOrderCommand.class)))
                   .thenReturn(beerOrderDto);

           // Perform the test
           mockMvc.perform(post("/api/v1/beer-orders")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(objectMapper.writeValueAsString(command)))
                   .andExpect(status().isCreated())
                   .andExpect(jsonPath("$.id").value(1))
                   .andExpect(jsonPath("$.customerId").value(1))
                   .andExpect(jsonPath("$.orderStatus").value("NEW"));
       }

       @Test
       void getBeerOrderById() throws Exception {
           BeerOrderDto beerOrderDto = BeerOrderDto.builder()
                   .id(1)
                   .customerId(1)
                   .customerName("Test Customer")
                   .orderStatus("NEW")
                   .build();

           when(beerOrderService.getBeerOrderById(1))
                   .thenReturn(Optional.of(beerOrderDto));

           mockMvc.perform(get("/api/v1/beer-orders/1"))
                   .andExpect(status().isOk())
                   .andExpect(jsonPath("$.id").value(1))
                   .andExpect(jsonPath("$.customerId").value(1))
                   .andExpect(jsonPath("$.orderStatus").value("NEW"));
       }

       @Test
       void getAllBeerOrders() throws Exception {
           List<BeerOrderDto> orders = Arrays.asList(
                   BeerOrderDto.builder().id(1).customerId(1).orderStatus("NEW").build(),
                   BeerOrderDto.builder().id(2).customerId(1).orderStatus("PROCESSING").build()
           );

           when(beerOrderService.getAllBeerOrders(eq(0), eq(10)))
                   .thenReturn(orders);

           mockMvc.perform(get("/api/v1/beer-orders")
                   .param("pageNumber", "0")
                   .param("pageSize", "10"))
                   .andExpect(status().isOk())
                   .andExpect(jsonPath("$", hasSize(2)))
                   .andExpect(jsonPath("$[0].id").value(1))
                   .andExpect(jsonPath("$[1].id").value(2));
       }
   }
   ```

## Implementation Summary

This plan provides a comprehensive implementation guide for adding a Beer Order System to the existing project. The implementation follows best practices and aligns with the project's architecture patterns.

### Key Components

1. **Domain Models (Entities)**
   - Beer (updated with relationships)
   - Customer
   - BeerOrder
   - BeerOrderLine
   - BeerInventory

2. **Repositories**
   - CustomerRepository
   - BeerOrderRepository
   - BeerOrderLineRepository
   - BeerInventoryRepository

3. **DTOs**
   - CustomerDto
   - BeerOrderDto
   - BeerOrderLineDto
   - BeerInventoryDto

4. **Command Objects**
   - CreateBeerOrderCommand
   - BeerOrderLineCommand

5. **Mappers**
   - CustomerMapper
   - BeerOrderMapper
   - BeerOrderLineMapper
   - BeerInventoryMapper

6. **Services**
   - CustomerService
   - BeerOrderService
   - BeerInventoryService

7. **Controllers**
   - CustomerController
   - BeerOrderController
   - BeerInventoryController

8. **Database Migrations**
   - V2__create_customer_table.sql
   - V3__create_beer_order_tables.sql

9. **Exception Handling**
   - GlobalExceptionHandler (updated)
   - BeerOrderException (custom exception)

10. **Tests**
    - Unit tests for services
    - Integration tests for repositories
    - API tests for controllers

### Entity Relationships

1. **Beer to BeerInventory**: One-to-Many
   - One Beer can have multiple BeerInventory records
   - Bidirectional relationship with Beer as the owning side

2. **Beer to BeerOrderLine**: One-to-Many
   - One Beer can be in multiple BeerOrderLine records
   - Bidirectional relationship with Beer as the owning side

3. **Customer to BeerOrder**: One-to-Many
   - One Customer can have multiple BeerOrder records
   - Bidirectional relationship with Customer as the owning side

4. **BeerOrder to BeerOrderLine**: One-to-Many
   - One BeerOrder can have multiple BeerOrderLine records
   - Bidirectional relationship with BeerOrder as the owning side

### Best Practices Applied

1. **Constructor Injection**
   - All dependencies are injected through constructors
   - Services use `@RequiredArgsConstructor` with final fields

2. **Package-Private Visibility**
   - Controllers use package-private visibility for internal components

3. **Transaction Management**
   - `@Transactional` for all service methods that modify data
   - `@Transactional(readOnly = true)` for query-only methods
   - Transaction boundaries kept at the service layer

4. **Entity-DTO Separation**
   - Entities are never exposed directly in controllers
   - DTOs are used for all controller request/response

5. **Command Objects**
   - Purpose-built command records for business operations
   - Validation annotations on command objects

6. **REST API Design**
   - Consistent URL patterns (/api/v1/resource)
   - Proper HTTP status codes
   - Pagination for collection resources

7. **Exception Handling**
   - Centralized exception handling with GlobalExceptionHandler
   - Custom exceptions for specific error cases
   - ProblemDetail response format

8. **Validation**
   - Jakarta Validation annotations on DTOs and command objects
   - Validation at the controller level
   - Meaningful validation error messages

9. **Testing**
   - Unit tests with Mockito
   - Integration tests with Testcontainers
   - API tests with MockMvc

### Implementation Sequence

To ensure a smooth implementation, the components should be implemented in the following order:

1. Entities and Repositories
2. DTOs and Mappers
3. Command Objects
4. Service Layer
5. Controllers
6. Database Migrations
7. Exception Handling
8. Tests

This sequence allows for incremental testing and validation of each layer before moving to the next.