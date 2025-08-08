package spring.start.here.juniemvc.web.model;

import java.util.List;

public record BeerOrderDto(
        Integer id,
        Integer version,
        Integer customerId,
        String customerRef,
        String orderStatus,
        String orderStatusCallbackUrl,
        List<BeerOrderLineDto> orderLines
) {}
