package spring.start.here.juniemvc.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import spring.start.here.juniemvc.domain.model.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BeerOrderRepositoryTest {

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveAndFindAllByCustomer() {
        Customer customer = customerRepository.save(Customer.builder()
                .name("John Doe").email("john@example.com").phone("123")
                .build());
        Beer beer = beerRepository.save(Beer.builder()
                .beerName("Test").beerStyle("IPA").upc("u").price(java.math.BigDecimal.ONE).quantityOnHand(10)
                .build());

        BeerOrder order1 = BeerOrder.builder().customer(customer).orderStatus("NEW").build();
        BeerOrder order2 = BeerOrder.builder().customer(customer).orderStatus("NEW").build();
        beerOrderRepository.save(order1);
        beerOrderRepository.save(order2);

        List<BeerOrder> list = beerOrderRepository.findAllByCustomer(customer);
        assertThat(list).hasSize(2);
    }
}
