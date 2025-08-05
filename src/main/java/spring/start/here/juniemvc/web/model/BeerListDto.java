package spring.start.here.juniemvc.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerListDto {

    private List<BeerDto> beers;
    private int totalPages;
    private int currentPage;
    private long totalElements;
}