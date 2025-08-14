package spring.start.here.juniemvc.web.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;


public record BeerOrderLineDto(
        Integer id,
        Integer version,
        Integer beerId,
        @NotNull(message = "Order quantity is required")
        @Positive(message = "Order quantity must be positive")
        Integer orderQuantity,
        @PositiveOrZero(message = "Quantity allocated must be zero or positive")
        Integer quantityAllocated
) {}
