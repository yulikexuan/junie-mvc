package spring.start.here.juniemvc.web.model;

public record BeerInventoryDto(
        Integer id,
        Integer version,
        Integer beerId,
        Integer quantityOnHand
) {}
