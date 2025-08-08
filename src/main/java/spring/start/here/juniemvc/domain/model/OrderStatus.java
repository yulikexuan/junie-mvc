package spring.start.here.juniemvc.domain.model;

public enum OrderStatus {
    NEW,
    VALIDATED,
    VALIDATION_PENDING,
    VALIDATION_EXCEPTION,
    ALLOCATION_PENDING,
    ALLOCATED,
    ALLOCATION_EXCEPTION,
    CANCELLED,
    PENDING_INVENTORY,
    PICKED_UP,
    DELIVERED,
    DELIVERY_EXCEPTION
}
