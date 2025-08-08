# Beer Order System Implementation Tasks

## 1. Entity Implementation

- [ ] 1.1. Update Beer Entity
   - [ ] 1.1.1. Add relationships with BeerInventory and BeerOrderLine
   - [ ] 1.1.2. Add @Table annotation with name "beer"
   - [ ] 1.1.3. Configure bidirectional relationships

- [ ] 1.2. Create Customer Entity
   - [ ] 1.2.1. Implement with fields: id, version, name, email, phone, timestamps
   - [ ] 1.2.2. Add relationship with BeerOrder
   - [ ] 1.2.3. Configure proper JPA annotations and Lombok annotations

- [ ] 1.3. Create BeerOrder Entity
   - [ ] 1.3.1. Implement with fields: id, version, orderStatus, orderStatusCallbackUrl, timestamps
   - [ ] 1.3.2. Add relationships with Customer and BeerOrderLine
   - [ ] 1.3.3. Configure proper JPA annotations and Lombok annotations

- [ ] 1.4. Create BeerOrderLine Entity
   - [ ] 1.4.1. Implement with fields: id, version, orderQuantity, quantityAllocated, timestamps
   - [ ] 1.4.2. Add relationships with BeerOrder and Beer
   - [ ] 1.4.3. Configure proper JPA annotations and Lombok annotations

- [ ] 1.5. Create BeerInventory Entity
   - [ ] 1.5.1. Implement with fields: id, version, quantityOnHand, timestamps
   - [ ] 1.5.2. Add relationship with Beer
   - [ ] 1.5.3. Configure proper JPA annotations and Lombok annotations

- [ ] 1.6. Create OrderStatus Enum
   - [ ] 1.6.1. Define statuses: NEW, VALIDATED, VALIDATION_PENDING, VALIDATION_EXCEPTION, ALLOCATION_PENDING, ALLOCATED, ALLOCATION_EXCEPTION, CANCELLED, PENDING_INVENTORY, PICKED_UP, DELIVERED, DELIVERY_EXCEPTION

## 2. Repository Implementation

- [ ] 2.1. Create CustomerRepository
   - [ ] 2.1.1. Extend JpaRepository
   - [ ] 2.1.2. Add custom query methods if needed

- [ ] 2.2. Create BeerOrderRepository
   - [ ] 2.2.1. Extend JpaRepository
   - [ ] 2.2.2. Add custom query methods for finding orders by customer

- [ ] 2.3. Create BeerOrderLineRepository
   - [ ] 2.3.1. Extend JpaRepository
   - [ ] 2.3.2. Add custom query methods if needed

- [ ] 2.4. Create BeerInventoryRepository
   - [ ] 2.4.1. Extend JpaRepository
   - [ ] 2.4.2. Add custom query methods for finding inventory by beer

## 3. DTO Implementation

- [ ] 3.1. Create CustomerDto
   - [ ] 3.1.1. Implement as a record with fields: id, version, name, email, phone

- [ ] 3.2. Create CustomerUpsertDto
   - [ ] 3.2.1. Implement as a record with fields: name, email, phone
   - [ ] 3.2.2. Add validation annotations

- [ ] 3.3. Create BeerOrderDto
   - [ ] 3.3.1. Implement as a record with fields: id, version, customerId, customerRef, orderStatus, orderStatusCallbackUrl, orderLines
   - [ ] 3.3.2. Include BeerOrderLineDto list

- [ ] 3.4. Create BeerOrderLineDto
   - [ ] 3.4.1. Implement as a record with fields: id, version, beerId, orderQuantity, quantityAllocated

- [ ] 3.5. Create BeerOrderUpsertDto
   - [ ] 3.5.1. Implement as a record with fields: customerId, customerRef, orderStatusCallbackUrl, orderLines
   - [ ] 3.5.2. Include BeerOrderLineUpsertDto list
   - [ ] 3.5.3. Add validation annotations

- [ ] 3.6. Create BeerOrderLineUpsertDto
   - [ ] 3.6.1. Implement as a record with fields: beerId, orderQuantity
   - [ ] 3.6.2. Add validation annotations

- [ ] 3.7. Create BeerInventoryDto
   - [ ] 3.7.1. Implement as a record with fields: id, version, beerId, quantityOnHand

## 4. Mapper Implementation

- [ ] 4.1. Create CustomerMapper
   - [ ] 4.1.1. Implement using MapStruct
   - [ ] 4.1.2. Add methods for mapping between Customer and CustomerDto/CustomerUpsertDto

- [ ] 4.2. Create BeerOrderMapper
   - [ ] 4.2.1. Implement using MapStruct
   - [ ] 4.2.2. Add methods for mapping between BeerOrder and BeerOrderDto/BeerOrderUpsertDto

- [ ] 4.3. Create BeerOrderLineMapper
   - [ ] 4.3.1. Implement using MapStruct
   - [ ] 4.3.2. Add methods for mapping between BeerOrderLine and BeerOrderLineDto/BeerOrderLineUpsertDto

- [ ] 4.4. Create BeerInventoryMapper
   - [ ] 4.4.1. Implement using MapStruct
   - [ ] 4.4.2. Add methods for mapping between BeerInventory and BeerInventoryDto

## 5. Service Implementation

- [ ] 5.1. Create CustomerService Interface
   - [ ] 5.1.1. Define methods for CRUD operations

- [ ] 5.2. Implement CustomerServiceImpl
   - [ ] 5.2.1. Implement all methods from CustomerService interface
   - [ ] 5.2.2. Add proper transaction management
   - [ ] 5.2.3. Handle exceptions appropriately

- [ ] 5.3. Create BeerOrderService Interface
   - [ ] 5.3.1. Define methods for CRUD operations
   - [ ] 5.3.2. Define methods for order processing

- [ ] 5.4. Implement BeerOrderServiceImpl
   - [ ] 5.4.1. Implement all methods from BeerOrderService interface
   - [ ] 5.4.2. Add proper transaction management
   - [ ] 5.4.3. Handle exceptions appropriately
   - [ ] 5.4.4. Implement order validation and allocation logic

- [ ] 5.5. Create BeerInventoryService Interface
   - [ ] 5.5.1. Define methods for inventory management

- [ ] 5.6. Implement BeerInventoryServiceImpl
   - [ ] 5.6.1. Implement all methods from BeerInventoryService interface
   - [ ] 5.6.2. Add proper transaction management
   - [ ] 5.6.3. Handle exceptions appropriately
   - [ ] 5.6.4. Implement inventory allocation logic

## 6. Controller Implementation

- [ ] 6.1. Create CustomerController
   - [ ] 6.1.1. Implement REST endpoints for CRUD operations
   - [ ] 6.1.2. Add proper request mapping and response status annotations
   - [ ] 6.1.3. Implement validation handling

- [ ] 6.2. Create BeerOrderController
   - [ ] 6.2.1. Implement REST endpoints for CRUD operations
   - [ ] 6.2.2. Add proper request mapping and response status annotations
   - [ ] 6.2.3. Implement validation handling
   - [ ] 6.2.4. Add endpoints for order processing

- [ ] 6.3. Create BeerInventoryController
   - [ ] 6.3.1. Implement REST endpoints for inventory management
   - [ ] 6.3.2. Add proper request mapping and response status annotations
   - [ ] 6.3.3. Implement validation handling

## 7. Database Migration

- [ ] 7.1. Create Flyway Migration Scripts
   - [ ] 7.1.1. Create script for customer table
   - [ ] 7.1.2. Create script for beer_order table
   - [ ] 7.1.3. Create script for beer_order_line table
   - [ ] 7.1.4. Create script for beer_inventory table

## 8. Testing

- [ ] 8.1. Write Unit Tests for Repositories
   - [ ] 8.1.1. Test CustomerRepository
   - [ ] 8.1.2. Test BeerOrderRepository
   - [ ] 8.1.3. Test BeerOrderLineRepository
   - [ ] 8.1.4. Test BeerInventoryRepository

- [ ] 8.2. Write Unit Tests for Services
   - [ ] 8.2.1. Test CustomerService
   - [ ] 8.2.2. Test BeerOrderService
   - [ ] 8.2.3. Test BeerInventoryService

- [ ] 8.3. Write Integration Tests for Controllers
   - [ ] 8.3.1. Test CustomerController
   - [ ] 8.3.2. Test BeerOrderController
   - [ ] 8.3.3. Test BeerInventoryController

- [ ] 8.4. Write End-to-End Tests
   - [ ] 8.4.1. Test complete order flow from creation to delivery

## 9. Additional Tasks
- [ ] 9.1. Verify all functionality works as expected
- [ ] 9.2. Ensure proper error handling for validation errors
- [ ] 9.3. Review code for any missed conversion points
- [ ] 9.4. Update documentation if necessary