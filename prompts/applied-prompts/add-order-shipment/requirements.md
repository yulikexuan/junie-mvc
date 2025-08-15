## Change Requirements

1. Add a new entity to the project called `BeerOrderShipment`
2. The `BeerOrderShipment` entity has the following properties:
   - shipmentDate - not null
   - carrier
   - trackingNumber
3. The `BeerOrder` entity has a `OneToMany` relationship `BeerOrderShipment` 
4. Add a flyway migration script for the new `BeerOrderShipment` JPA Entity 
5. The path for controller operations should be `/api/v1/beer-orders/{beerOrderId}/shipments`, 
   where the `beerOrderId` is the `id` of the owning `BeerOrder` entity 
6. The controller and service will need the `id` of the `BeerOrder` the `BeerOrderShipment` belongs to 
7. Add Java DTOs, Mappers, Spring Data Repositories, service and service implementation to support a Spring MVC RESTful 
   CRUD controller 
8. Add Tests for all components 
9. Update the OpenAPI documentation for the new controller operations 
10. Verify all tests are passing