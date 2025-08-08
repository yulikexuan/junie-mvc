package spring.start.here.juniemvc.web.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BeerOrderLineUpsertDto(
        @NotNull Integer beerId,
        @NotNull @Positive Integer orderQuantity
) {}
