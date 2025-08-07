# Beer Order System Implementation Tasks

## Entity Implementation

1. [ ] Update Beer Entity
   - [ ] Add relationships with BeerInventory and BeerOrderLine
   - [ ] Add @Table annotation with name "beer"
   - [ ] Configure bidirectional relationships

2. [ ] Create Customer Entity
   - [ ] Implement with fields: id, version, name, email, phone, timestamps
   - [ ] Add relationship with BeerOrder
   - [ ] Configure proper JPA annotations and Lombok annotations

3. [ ] Create BeerOrder Entity
   - [ ] Implement with fields: id, version, orderStatus, orderStatusCallbackUrl, timestamps
   - [ ] Add relationships with Customer and BeerOrderLine
   - [ ] Configure proper JPA annotations and Lombok annotations

4. [ ] Create BeerOrderLine Entity
   - [ ] Implement with fields: id, version, orderQuantity, quantityAllocated, timestamps
   - [ ] Add relationships with BeerOrder and Beer
   - [ ] Configure proper JPA annotations and Lombok annotations

5. [ ] Create BeerInventory Entity
   - [ ] Implement with fields: id, version, quantityOnHand, timestamps
   - [ ] Add relationship with Beer
   - [ ] Configure proper JPA annotations and Lombok annotations

6. [ ] Create OrderStatus Enum
   - [ ] Define statuses: NEW, VALIDATED, VALIDATION_PENDING, VALIDATION_EXCEPTION, ALLOCATION_PENDING, ALLOCATED, ALLOCATION_EXCEPTION, CANCELLED, PENDING_INVENTORY, PICKED_UP, DELIVERED, DELIVERY_EXCEPTION

## Repository Implementation

7. [ ] Create CustomerRepository
   - [ ] Extend JpaRepository
   - [ ] Add custom query methods if needed

8. [ ] Create BeerOrderRepository
   - [ ] Extend JpaRepository
   - [ ] Add custom query methods for finding orders by customer

9. [ ] Create BeerOrderLineRepository
   - [ ] Extend JpaRepository
   - [ ] Add custom query methods if needed

10. [ ] Create BeerInventoryRepository
    - [ ] Extend JpaRepository
    - [ ] Add custom query methods for finding inventory by beer

## DTO Implementation

11. [ ] Create CustomerDto
    - [ ] Implement as a record with fields: id, version, name, email, phone

12. [ ] Create CustomerUpsertDto
    - [ ] Implement as a record with fields: name, email, phone
    - [ ] Add validation annotations

13. [ ] Create BeerOrderDto
    - [ ] Implement as a record with fields: id, version, customerId, customerRef, orderStatus, orderStatusCallbackUrl, orderLines
    - [ ] Include BeerOrderLineDto list

14. [ ] Create BeerOrderLineDto
    - [ ] Implement as a record with fields: id, version, beerId, orderQuantity, quantityAllocated

15. [ ] Create BeerOrderUpsertDto
    - [ ] Implement as a record with fields: customerId, customerRef, orderStatusCallbackUrl, orderLines
    - [ ] Include BeerOrderLineUpsertDto list
    - [ ] Add validation annotations

16. [ ] Create BeerOrderLineUpsertDto
    - [ ] Implement as a record with fields: beerId, orderQuantity
    - [ ] Add validation annotations

17. [ ] Create BeerInventoryDto
    - [ ] Implement as a record with fields: id, version, beerId, quantityOnHand

## Mapper Implementation

18. [ ] Create CustomerMapper
    - [ ] Implement using MapStruct
    - [ ] Add methods for mapping between Customer and CustomerDto/CustomerUpsertDto

19. [ ] Create BeerOrderMapper
    - [ ] Implement using MapStruct
    - [ ] Add methods for mapping between BeerOrder and BeerOrderDto/BeerOrderUpsertDto

20. [ ] Create BeerOrderLineMapper
    - [ ] Implement using MapStruct
    - [ ] Add methods for mapping between BeerOrderLine and BeerOrderLineDto/BeerOrderLineUpsertDto

21. [ ] Create BeerInventoryMapper
    - [ ] Implement using MapStruct
    - [ ] Add methods for mapping between BeerInventory and BeerInventoryDto

## Service Implementation

22. [ ] Create CustomerService Interface
    - [ ] Define methods for CRUD operations

23. [ ] Implement CustomerServiceImpl
    - [ ] Implement all methods from CustomerService interface
    - [ ] Add proper transaction management
    - [ ] Handle exceptions appropriately

24. [ ] Create BeerOrderService Interface
    - [ ] Define methods for CRUD operations
    - [ ] Define methods for order processing

25. [ ] Implement BeerOrderServiceImpl
    - [ ] Implement all methods from BeerOrderService interface
    - [ ] Add proper transaction management
    - [ ] Handle exceptions appropriately
    - [ ] Implement order validation and allocation logic

26. [ ] Create BeerInventoryService Interface
    - [ ] Define methods for inventory management

27. [ ] Implement BeerInventoryServiceImpl
    - [ ] Implement all methods from BeerInventoryService interface
    - [ ] Add proper transaction management
    - [ ] Handle exceptions appropriately
    - [ ] Implement inventory allocation logic

## Controller Implementation

28. [ ] Create CustomerController
    - [ ] Implement REST endpoints for CRUD operations
    - [ ] Add proper request mapping and response status annotations
    - [ ] Implement validation handling

29. [ ] Create BeerOrderController
    - [ ] Implement REST endpoints for CRUD operations
    - [ ] Add proper request mapping and response status annotations
    - [ ] Implement validation handling
    - [ ] Add endpoints for order processing

30. [ ] Create BeerInventoryController
    - [ ] Implement REST endpoints for inventory management
    - [ ] Add proper request mapping and response status annotations
    - [ ] Implement validation handling

## Database Migration

31. [ ] Create Flyway Migration Scripts
    - [ ] Create script for customer table
    - [ ] Create script for beer_order table
    - [ ] Create script for beer_order_line table
    - [ ] Create script for beer_inventory table

## Testing

32. [ ] Write Unit Tests for Repositories
    - [ ] Test CustomerRepository
    - [ ] Test BeerOrderRepository
    - [ ] Test BeerOrderLineRepository
    - [ ] Test BeerInventoryRepository

33. [ ] Write Unit Tests for Services
    - [ ] Test CustomerService
    - [ ] Test BeerOrderService
    - [ ] Test BeerInventoryService

34. [ ] Write Integration Tests for Controllers
    - [ ] Test CustomerController
    - [ ] Test BeerOrderController
    - [ ] Test BeerInventoryController

35. [ ] Write End-to-End Tests
    - [ ] Test complete order flow from creation to delivery
