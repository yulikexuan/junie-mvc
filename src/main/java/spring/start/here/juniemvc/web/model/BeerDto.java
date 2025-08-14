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

    // Read-only
    private Integer id;
    private Integer version;

    @NotBlank
    @Size(min = 3, max = 100)
    private String beerName;

    // Style of the beer, ALE, PALE ALE, IPA, etc.
    @NotBlank
    private String beerStyle;

    // Universal Product Code, a 13-digit number assigned to each unique beer product by the Federal Bar Association
    @NotBlank
    @Size(min = 3, max = 13)
    private String upc;

    @NotNull
    @Positive
    private Integer quantityOnHand;

    @NotNull
    @Positive
    private BigDecimal price;

    // Read-only created date
    private LocalDateTime createdDate;

    // Read-only updated date
    private LocalDateTime updateDate;
}