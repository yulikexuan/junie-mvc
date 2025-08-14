package spring.start.here.juniemvc.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import spring.start.here.juniemvc.domain.model.Customer;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void save_and_findById_and_delete() {
        Customer toSave = Customer.builder()
                .name("John Doe")
                .email("john@example.com")
                .phone("1234567890")
                .build();

        Customer saved = customerRepository.save(toSave);
        assertThat(saved.getId()).isNotNull();
        // With @Version, JPA typically initializes version to 0 on insert
        assertThat(saved.getVersion()).isNotNull();
        assertThat(saved.getVersion()).isGreaterThanOrEqualTo(0);

        Optional<Customer> foundOpt = customerRepository.findById(saved.getId());
        assertThat(foundOpt).isPresent();
        Customer found = foundOpt.get();
        assertThat(found.getName()).isEqualTo("John Doe");
        assertThat(found.getEmail()).isEqualTo("john@example.com");
        assertThat(found.getPhone()).isEqualTo("1234567890");

        List<Customer> all = customerRepository.findAll();
        assertThat(all).extracting(Customer::getId).contains(saved.getId());

        customerRepository.deleteById(saved.getId());
        assertThat(customerRepository.findById(saved.getId())).isEmpty();
    }
}
