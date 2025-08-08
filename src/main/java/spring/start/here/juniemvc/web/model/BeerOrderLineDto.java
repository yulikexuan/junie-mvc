package spring.start.here.juniemvc.web.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BeerOrderLineDto(
        Integer id,
        Integer version,
        Integer beerId,
        Integer orderQuantity,
        Integer quantityAllocated
) {}
