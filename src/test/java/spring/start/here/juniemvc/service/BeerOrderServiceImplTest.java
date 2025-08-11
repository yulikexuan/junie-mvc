package spring.start.here.juniemvc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import spring.start.here.juniemvc.domain.model.Beer;
import spring.start.here.juniemvc.domain.model.BeerOrder;
import spring.start.here.juniemvc.domain.model.BeerOrderLine;
import spring.start.here.juniemvc.domain.model.Customer;
import spring.start.here.juniemvc.repository.BeerOrderRepository;
import spring.start.here.juniemvc.repository.BeerRepository;
import spring.start.here.juniemvc.repository.CustomerRepository;
import spring.start.here.juniemvc.web.mappers.BeerOrderLineMapper;
import spring.start.here.juniemvc.web.mappers.BeerOrderMapper;
import spring.start.here.juniemvc.web.model.BeerOrderDto;
import spring.start.here.juniemvc.web.model.BeerOrderLineUpsertDto;
import spring.start.here.juniemvc.web.model.BeerOrderUpsertDto;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BeerOrderServiceImplTest {

    @Mock
    BeerOrderRepository beerOrderRepository;

    @Mock
    CustomerRepository customerRepository;

    @Mock
    BeerRepository beerRepository;

    @Mock
    BeerOrderMapper beerOrderMapper;

    @Mock
    BeerOrderLineMapper beerOrderLineMapper;

    @InjectMocks
    BeerOrderServiceImpl beerOrderService;

    Customer customer;
    Beer beer;
    BeerOrder order;
    BeerOrderDto orderDto;
    BeerOrderUpsertDto upsertDto;

    @BeforeEach
    void init() {
        customer = Customer.builder().id(10).build();
        beer = Beer.builder().id(5).build();
        order = BeerOrder.builder().id(1).customer(customer).orderStatus("NEW").build();
        orderDto = new BeerOrderDto(1, 0, 10, null, "NEW", "cb", List.of());
        upsertDto = new BeerOrderUpsertDto(10, "REF", "cb", List.of(new BeerOrderLineUpsertDto(5, 2)));
    }

    @Test
    void createOrder_success() {
        given(customerRepository.findById(10)).willReturn(Optional.of(customer));
        given(beerRepository.findById(5)).willReturn(Optional.of(beer));
        given(beerOrderRepository.save(any(BeerOrder.class))).willReturn(order);
        given(beerOrderMapper.toDto(order)).willReturn(orderDto);

        BeerOrderDto result = beerOrderService.createOrder(upsertDto);
        assertThat(result).isEqualTo(orderDto);
        verify(beerOrderRepository).save(any(BeerOrder.class));
    }

    @Test
    void createOrder_missingCustomer_throws() {
        given(customerRepository.findById(10)).willReturn(Optional.empty());
        assertThatThrownBy(() -> beerOrderService.createOrder(upsertDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Customer not found");
    }

    @Test
    void createOrder_missingBeer_throws() {
        given(customerRepository.findById(10)).willReturn(Optional.of(customer));
        given(beerRepository.findById(5)).willReturn(Optional.empty());
        assertThatThrownBy(() -> beerOrderService.createOrder(upsertDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Beer not found");
    }

    @Test
    void getById_found() {
        given(beerOrderRepository.findById(1)).willReturn(Optional.of(order));
        given(beerOrderMapper.toDto(order)).willReturn(orderDto);
        Optional<BeerOrderDto> result = beerOrderService.getById(1);
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(orderDto);
    }

    @Test
    void getById_notFound() {
        given(beerOrderRepository.findById(1)).willReturn(Optional.empty());
        Optional<BeerOrderDto> result = beerOrderService.getById(1);
        assertThat(result).isEmpty();
    }

    @Test
    void getAll_withPagination() {
        Page<BeerOrder> page = new PageImpl<>(List.of(order));
        given(beerOrderRepository.findAll(any(PageRequest.class))).willReturn(page);
        given(beerOrderMapper.toDto(order)).willReturn(orderDto);
        List<BeerOrderDto> result = beerOrderService.getAll(0, 10);
        assertThat(result).hasSize(1);
    }

    @Test
    void getAll_withoutPagination() {
        given(beerOrderRepository.findAll()).willReturn(List.of(order));
        given(beerOrderMapper.toDto(order)).willReturn(orderDto);
        List<BeerOrderDto> result = beerOrderService.getAll(null, null);
        assertThat(result).hasSize(1);
    }

    @Test
    void getByCustomer_found() {
        given(customerRepository.findById(10)).willReturn(Optional.of(customer));
        given(beerOrderRepository.findAllByCustomer(customer)).willReturn(List.of(order));
        given(beerOrderMapper.toDto(order)).willReturn(orderDto);
        List<BeerOrderDto> result = beerOrderService.getByCustomer(10, null, null);
        assertThat(result).hasSize(1);
    }

    @Test
    void getByCustomer_notFound() {
        given(customerRepository.findById(10)).willReturn(Optional.empty());
        List<BeerOrderDto> result = beerOrderService.getByCustomer(10, null, null);
        assertThat(result).isEmpty();
    }

    @Test
    void updateStatus_found() {
        given(beerOrderRepository.findById(1)).willReturn(Optional.of(order));
        given(beerOrderRepository.save(order)).willReturn(order);
        given(beerOrderMapper.toDto(order)).willReturn(new BeerOrderDto(1, 0, 10, null, "ALLOCATED", "cb", List.of()));
        Optional<BeerOrderDto> result = beerOrderService.updateStatus(1, "ALLOCATED");
        assertThat(result).isPresent();
        assertThat(result.get().orderStatus()).isEqualTo("ALLOCATED");
    }

    @Test
    void updateStatus_notFound() {
        given(beerOrderRepository.findById(1)).willReturn(Optional.empty());
        Optional<BeerOrderDto> result = beerOrderService.updateStatus(1, "ALLOCATED");
        assertThat(result).isEmpty();
    }

    @Test
    void delete_found() {
        given(beerOrderRepository.existsById(1)).willReturn(true);
        boolean result = beerOrderService.delete(1);
        assertThat(result).isTrue();
        verify(beerOrderRepository).deleteById(1);
    }

    @Test
    void delete_notFound() {
        given(beerOrderRepository.existsById(1)).willReturn(false);
        boolean result = beerOrderService.delete(1);
        assertThat(result).isFalse();
    }
}
