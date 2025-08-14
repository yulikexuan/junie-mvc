package spring.start.here.juniemvc.web.model;


import java.util.Set;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;


public record BeerOrderDto(
        Integer id, // Read-only
        Integer version,
        Integer customerId,
        String customerRef, //reference information from customer
        String orderStatus, // enum status of the order, NEW, PAID, CANCELLED, INPROCESS, COMPLETE.
        String orderStatusCallbackUrl,
        @NotEmpty(message = "Beer order must have at least one beer order line")
        @Valid
        Set<BeerOrderLineDto> orderLines
) {}
