package spring.start.here.juniemvc.web.mappers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import spring.start.here.juniemvc.domain.model.Beer;
import spring.start.here.juniemvc.domain.model.BeerOrderLine;
import spring.start.here.juniemvc.web.model.BeerOrderLineDto;
import spring.start.here.juniemvc.web.model.BeerOrderLineUpsertDto;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BeerOrderLineMapperTest {

    @Autowired
    BeerOrderLineMapper mapper;

    @Test
    void toDto_mapsFields() {
        Beer beer = Beer.builder().id(7).build();
        BeerOrderLine entity = BeerOrderLine.builder()
                .id(100)
                .version(1)
                .beer(beer)
                .orderQuantity(3)
                .quantityAllocated(1)
                .build();

        BeerOrderLineDto dto = mapper.toDto(entity);
        assertThat(dto).isNotNull();
        assertThat(dto.beerId()).isEqualTo(7);
        assertThat(dto.id()).isEqualTo(100);
        assertThat(dto.orderQuantity()).isEqualTo(3);
        assertThat(dto.quantityAllocated()).isEqualTo(1);
    }

    @Test
    void toEntity_mapsFields() {
        BeerOrderLineUpsertDto upsert = new BeerOrderLineUpsertDto(9, 4);
        BeerOrderLine entity = mapper.toEntity(upsert);
        assertThat(entity).isNotNull();
        assertThat(entity.getBeer()).isNotNull();
        assertThat(entity.getBeer().getId()).isEqualTo(9);
        assertThat(entity.getOrderQuantity()).isEqualTo(4);
        // ignored mappings
        assertThat(entity.getId()).isNull();
        assertThat(entity.getVersion()).isNull();
        assertThat(entity.getBeerOrder()).isNull();
    }
}
