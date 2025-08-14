package spring.start.here.juniemvc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spring.start.here.juniemvc.domain.model.Customer;
import spring.start.here.juniemvc.repository.CustomerRepository;
import spring.start.here.juniemvc.web.exception.CustomerNotFoundException;
import spring.start.here.juniemvc.web.mappers.CustomerMapper;
import spring.start.here.juniemvc.web.model.CustomerDto;
import spring.start.here.juniemvc.web.model.CustomerUpsertDto;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    CustomerRepository customerRepository;

    @Mock
    CustomerMapper customerMapper;

    @InjectMocks
    CustomerServiceImpl customerService;

    Customer entity;
    CustomerDto dto;
    CustomerUpsertDto upsert;

    @BeforeEach
    void setUp() {
        entity = Customer.builder().id(1).version(0).name("John").email("j@e.com").phone("123").build();
        dto = new CustomerDto(1, 0, "John", "j@e.com", "123");
        upsert = new CustomerUpsertDto("Jane", "jane@e.com", "999");
    }

    @Test
    void create_success() {
        given(customerMapper.toEntity(upsert)).willReturn(Customer.builder().name("Jane").email("jane@e.com").phone("999").build());
        given(customerRepository.save(any(Customer.class))).willReturn(entity);
        given(customerMapper.toDto(entity)).willReturn(dto);

        CustomerDto result = customerService.create(upsert);
        assertThat(result).isEqualTo(dto);
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void getById_found() {
        given(customerRepository.findById(1)).willReturn(Optional.of(entity));
        given(customerMapper.toDto(entity)).willReturn(dto);
        Optional<CustomerDto> result = customerService.getById(1);
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(dto);
    }

    @Test
    void getById_notFound() {
        given(customerRepository.findById(1)).willReturn(Optional.empty());
        Optional<CustomerDto> result = customerService.getById(1);
        assertThat(result).isEmpty();
    }

    @Test
    void getAll_returnsList() {
        given(customerRepository.findAll()).willReturn(List.of(entity));
        given(customerMapper.toDto(entity)).willReturn(dto);
        List<CustomerDto> result = customerService.getAll();
        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isEqualTo(dto);
    }

    @Test
    void update_found_updatesAndReturnsDto() {
        given(customerRepository.findById(1)).willReturn(Optional.of(entity));
        // repository.save returns same entity for simplicity
        given(customerRepository.save(entity)).willReturn(entity);
        given(customerMapper.toDto(entity)).willReturn(new CustomerDto(1, 0, "Jane", "jane@e.com", "999"));

        Optional<CustomerDto> result = customerService.update(1, upsert);
        assertThat(result).isPresent();
        assertThat(result.get().name()).isEqualTo("Jane");
        verify(customerRepository).save(entity);
    }

    @Test
    void update_notFound_throwsCustomerNotFound() {
        given(customerRepository.findById(1)).willReturn(Optional.empty());
        assertThatThrownBy(() -> customerService.update(1, upsert))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessageContaining("Customer not found");
    }

    @Test
    void delete_found_returnsTrue() {
        given(customerRepository.existsById(1)).willReturn(true);
        boolean deleted = customerService.delete(1);
        assertThat(deleted).isTrue();
        verify(customerRepository).deleteById(1);
    }

    @Test
    void delete_notFound_returnsFalse() {
        given(customerRepository.existsById(1)).willReturn(false);
        boolean deleted = customerService.delete(1);
        assertThat(deleted).isFalse();
    }
}
