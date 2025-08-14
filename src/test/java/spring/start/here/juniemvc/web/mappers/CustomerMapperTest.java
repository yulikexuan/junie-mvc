package spring.start.here.juniemvc.web.mappers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import spring.start.here.juniemvc.domain.model.Customer;
import spring.start.here.juniemvc.web.model.CustomerDto;
import spring.start.here.juniemvc.web.model.CustomerUpsertDto;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CustomerMapperTest {

    @Autowired
    CustomerMapper mapper;

    @Test
    void toDto_and_toEntity_work() {
        Customer entity = Customer.builder()
                .id(1)
                .version(0)
                .name("Alice")
                .email("alice@example.com")
                .phone("555-0000")
                .build();

        CustomerDto dto = mapper.toDto(entity);
        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(1);
        assertThat(dto.name()).isEqualTo("Alice");

        CustomerDto dto2 = new CustomerDto(2, 1, "Bob", "bob@example.com", "123");
        Customer fromDto = mapper.toEntity(dto2);
        assertThat(fromDto.getId()).isEqualTo(2);
        assertThat(fromDto.getVersion()).isEqualTo(1);
        assertThat(fromDto.getName()).isEqualTo("Bob");

        CustomerUpsertDto upsert = new CustomerUpsertDto("Carl", "carl@example.com", "999");
        Customer fromUpsert = mapper.toEntity(upsert);
        assertThat(fromUpsert.getId()).isNull();
        assertThat(fromUpsert.getName()).isEqualTo("Carl");
        assertThat(fromUpsert.getEmail()).isEqualTo("carl@example.com");
        assertThat(fromUpsert.getPhone()).isEqualTo("999");
    }
}
