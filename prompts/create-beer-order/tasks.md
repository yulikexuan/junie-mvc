# Beer Order System Implementation Tasks

## 1. Entity Implementation

- [x] 1.1. Update Beer Entity
   - [x] 1.1.1. Add relationships with BeerInventory and BeerOrderLine
   - [x] 1.1.2. Add @Table annotation with name "beer"
   - [x] 1.1.3. Configure bidirectional relationships

- [x] 1.2. Create Customer Entity
   - [x] 1.2.1. Implement with fields: id, version, name, email, phone, timestamps
   - [x] 1.2.2. Add a relationship with BeerOrder
   - [x] 1.2.3. Configure proper JPA annotations and Lombok annotations

- [x] 1.3. Create BeerOrder Entity
   - [x] 1.3.1. Implement with fields: id, version, orderStatus, orderStatusCallbackUrl, timestamps
   - [x] 1.3.2. Add relationships with Customer and BeerOrderLine
   - [x] 1.3.3. Configure proper JPA annotations and Lombok annotations

- [x] 1.4. Create BeerOrderLine Entity
   - [x] 1.4.1. Implement with fields: id, version, orderQuantity, quantityAllocated, timestamps
   - [x] 1.4.2. Add relationships with BeerOrder and Beer
   - [x] 1.4.3. Configure proper JPA annotations and Lombok annotations

- [x] 1.5. Create BeerInventory Entity
   - [x] 1.5.1. Implement with fields: id, version, quantityOnHand, timestamps
   - [x] 1.5.2. Add a relationship with Beer
   - [x] 1.5.3. Configure proper JPA annotations and Lombok annotations

- [x] 1.6. Create OrderStatus Enum
   - [x] 1.6.1. Define statuses: NEW, VALIDATED, VALIDATION_PENDING, VALIDATION_EXCEPTION, ALLOCATION_PENDING, ALLOCATED, ALLOCATION_EXCEPTION, CANCELLED, PENDING_INVENTORY, PICKED_UP, DELIVERED, DELIVERY_EXCEPTION

## 2. Repository Implementation

- [x] 2.1. Create CustomerRepository
   - [x] 2.1.1. Extend JpaRepository
   - [x] 2.1.2. Add custom query methods if needed

- [x] 2.2. Create BeerOrderRepository
   - [x] 2.2.1. Extend JpaRepository
   - [x] 2.2.2. Add custom query methods for finding orders by customer

- [x] 2.3. Create BeerOrderLineRepository
   - [x] 2.3.1. Extend JpaRepository
   - [x] 2.3.2. Add custom query methods if needed

- [x] 2.4. Create BeerInventoryRepository
   - [x] 2.4.1. Extend JpaRepository
   - [x] 2.4.2. Add custom query methods for finding inventory by beer

## 3. DTO Implementation

- [x] 3.1. Create CustomerDto
   - [x] 3.1.1. Implement as a record with fields: id, version, name, email, phone

- [x] 3.2. Create CustomerUpsertDto
   - [x] 3.2.1. Implement as a record with fields: name, email, phone
   - [x] 3.2.2. Add validation annotations

- [x] 3.3. Create BeerOrderDto
   - [x] 3.3.1. Implement as a record with fields: id, version, customerId, customerRef, orderStatus, orderStatusCallbackUrl, orderLines
   - [x] 3.3.2. Include BeerOrderLineDto list

- [x] 3.4. Create BeerOrderLineDto
   - [x] 3.4.1. Implement as a record with fields: id, version, beerId, orderQuantity, quantityAllocated

- [x] 3.5. Create BeerOrderUpsertDto
   - [x] 3.5.1. Implement as a record with fields: customerId, customerRef, orderStatusCallbackUrl, orderLines
   - [x] 3.5.2. Include BeerOrderLineUpsertDto list
   - [x] 3.5.3. Add validation annotations

- [x] 3.6. Create BeerOrderLineUpsertDto
   - [x] 3.6.1. Implement as a record with fields: beerId, orderQuantity
   - [x] 3.6.2. Add validation annotations

- [x] 3.7. Create BeerInventoryDto
   - [x] 3.7.1. Implement as a record with fields: id, version, beerId, quantityOnHand

## 4. Mapper Implementation

- [x] 4.1. Create CustomerMapper
   - [x] 4.1.1. Implement using MapStruct
   - [x] 4.1.2. Add methods for mapping between Customer and CustomerDto/CustomerUpsertDto

- [x] 4.2. Create BeerOrderMapper
   - [x] 4.2.1. Implement using MapStruct
   - [x] 4.2.2. Add methods for mapping between BeerOrder and BeerOrderDto/BeerOrderUpsertDto

- [x] 4.3. Create BeerOrderLineMapper
   - [x] 4.3.1. Implement using MapStruct
   - [x] 4.3.2. Add methods for mapping between BeerOrderLine and BeerOrderLineDto/BeerOrderLineUpsertDto

- [x] 4.4. Create BeerInventoryMapper
   - [x] 4.4.1. Implement using MapStruct
   - [x] 4.4.2. Add methods for mapping between BeerInventory and BeerInventoryDto

## 5. Service Implementation

- [x] 5.1. Create CustomerService Interface
   - [x] 5.1.1. Define methods for CRUD operations

- [x] 5.2. Implement CustomerServiceImpl
   - [x] 5.2.1. Implement all methods from CustomerService interface
   - [x] 5.2.2. Add proper transaction management
   - [x] 5.2.3. Handle exceptions appropriately

- [x] 5.3. Create BeerOrderService Interface
   - [x] 5.3.1. Define methods for CRUD operations
   - [x] 5.3.2. Define methods for order processing

- [x] 5.4. Implement BeerOrderServiceImpl
   - [x] 5.4.1. Implement all methods from BeerOrderService interface
   - [x] 5.4.2. Add proper transaction management
   - [x] 5.4.3. Handle exceptions appropriately
   - [x] 5.4.4. Implement order validation and allocation logic

- [x] 5.5. Create BeerInventoryService Interface
   - [x] 5.5.1. Define methods for inventory management

- [x] 5.6. Implement BeerInventoryServiceImpl
   - [x] 5.6.1. Implement all methods from BeerInventoryService interface
   - [x] 5.6.2. Add proper transaction management
   - [x] 5.6.3. Handle exceptions appropriately
   - [x] 5.6.4. Implement inventory allocation logic

## 6. Controller Implementation

- [x] 6.1. Create CustomerController
   - [x] 6.1.1. Implement REST endpoints for CRUD operations
   - [x] 6.1.2. Add proper request mapping and response status annotations
   - [x] 6.1.3. Implement validation handling

- [x] 6.2. Create BeerOrderController
   - [x] 6.2.1. Implement REST endpoints for CRUD operations
   - [x] 6.2.2. Add proper request mapping and response status annotations
   - [x] 6.2.3. Implement validation handling
   - [x] 6.2.4. Add endpoints for order processing

- [x] 6.3. Create BeerInventoryController
   - [x] 6.3.1. Implement REST endpoints for inventory management
   - [x] 6.3.2. Add proper request mapping and response status annotations
   - [x] 6.3.3. Implement validation handling

## 7. Database Migration

- [x] 7.1. Create Flyway Migration Scripts
   - [x] 7.1.1. Create script for customer table
   - [x] 7.1.2. Create script for beer_order table
   - [x] 7.1.3. Create script for beer_order_line table
   - [x] 7.1.4. Create script for beer_inventory table

## 8. Testing

- [x] 8.1. Write Unit Tests for Repositories
   - [x] 8.1.1. Test CustomerRepository
   - [x] 8.1.2. Test BeerOrderRepository
   - [x] 8.1.3. Test BeerOrderLineRepository
   - [x] 8.1.4. Test BeerInventoryRepository

- [x] 8.2. Write Unit Tests for Services
   - [x] 8.2.1. Test CustomerService
   - [x] 8.2.2. Test BeerOrderService
   - [x] 8.2.3. Test BeerInventoryService

- [x] 8.3. Write Integration Tests for Controllers
   - [x] 8.3.1. Test CustomerController
   - [x] 8.3.2. Test BeerOrderController
   - [x] 8.3.3. Test BeerInventoryController

- [x] 8.4. Write End-to-End Tests
   - [x] 8.4.1. Test complete order flow from creation to delivery

## 9. Additional Tasks
- [x] 9.1. Verify all functionality works as expected
- [x] 9.2. Ensure proper error handling for validation errors
- [x] 9.3. Review code for any missed conversion points
- [x] 9.4. Update documentation if necessary