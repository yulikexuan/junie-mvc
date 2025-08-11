package spring.start.here.juniemvc.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import spring.start.here.juniemvc.domain.model.*;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

@DataJpaTest
class BeerOrderLineRepositoryTest {

    @Autowired
    BeerOrderLineRepository beerOrderLineRepository;

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void testSaveOrderLine() {
        Customer customer = customerRepository.save(Customer.builder()
                .name("Jane").email("jane@example.com").phone("555")
                .build());
        Beer beer = beerRepository.save(Beer.builder()
                .beerName("B").beerStyle("IPA").upc("123").price(BigDecimal.TEN).quantityOnHand(5)
                .build());
        BeerOrder order = beerOrderRepository.save(BeerOrder.builder()
                .customer(customer).orderStatus("NEW").build());

        BeerOrderLine line = BeerOrderLine.builder()
                .beer(beer)
                .beerOrder(order)
                .orderQuantity(2)
                .quantityAllocated(0)
                .build();

        BeerOrderLine saved = beerOrderLineRepository.save(line);
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getBeer().getId()).isEqualTo(beer.getId());
        assertThat(saved.getBeerOrder().getId()).isEqualTo(order.getId());
    }
}
