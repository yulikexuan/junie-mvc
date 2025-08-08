package spring.start.here.juniemvc.web.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record BeerOrderUpsertDto(
        @NotNull Integer customerId,
        @NotBlank @Size(max = 100) String customerRef,
        @Size(max = 255) String orderStatusCallbackUrl,
        @NotNull @Valid List<BeerOrderLineUpsertDto> orderLines
) {}
