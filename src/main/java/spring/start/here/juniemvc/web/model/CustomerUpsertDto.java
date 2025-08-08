package spring.start.here.juniemvc.web.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CustomerUpsertDto(
        @NotBlank String name,
        @Email String email,
        @NotBlank String phone
) {}
