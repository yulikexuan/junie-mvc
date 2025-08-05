package spring.start.here.juniemvc.web.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerDto {

    private Integer id;
    private Integer version;

    @NotBlank
    @Size(min = 3, max = 100)
    private String beerName;

    @NotBlank
    private String beerStyle;

    @NotBlank
    @Size(min = 3, max = 13)
    private String upc;

    @NotNull
    @Positive
    private Integer quantityOnHand;

    @NotNull
    @Positive
    private BigDecimal price;

    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}