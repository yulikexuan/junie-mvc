package spring.start.here.juniemvc.web.model;

public record CustomerDto(
        Integer id,
        Integer version,
        String name,
        String email,
        String phone
) {}
