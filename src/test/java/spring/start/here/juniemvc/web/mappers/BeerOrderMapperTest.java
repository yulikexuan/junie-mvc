package spring.start.here.juniemvc.web.mappers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import spring.start.here.juniemvc.domain.model.Beer;
import spring.start.here.juniemvc.domain.model.BeerOrder;
import spring.start.here.juniemvc.domain.model.BeerOrderLine;
import spring.start.here.juniemvc.domain.model.Customer;
import spring.start.here.juniemvc.web.model.BeerOrderDto;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BeerOrderMapperTest {

    @Autowired
    BeerOrderMapper mapper;

    @Test
    void toDto_mapsNestedRelations() {
        Customer customer = Customer.builder().id(42).build();
        Beer beer = Beer.builder().id(7).build();
        BeerOrder order = BeerOrder.builder()
                .id(1)
                .version(0)
                .orderStatus("NEW")
                .orderStatusCallbackUrl("cb")
                .customer(customer)
                .build();
        BeerOrderLine line = BeerOrderLine.builder()
                .id(100)
                .version(0)
                .beer(beer)
                .beerOrder(order)
                .orderQuantity(3)
                .quantityAllocated(0)
                .build();
        order.setBeerOrderLines(Set.of(line));

        BeerOrderDto dto = mapper.toDto(order);
        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(1);
        assertThat(dto.customerId()).isEqualTo(42);
        assertThat(dto.orderLines()).hasSize(1);
        assertThat(dto.orderLines().get(0).beerId()).isEqualTo(7);
        assertThat(dto.orderStatus()).isEqualTo("NEW");
    }
}
