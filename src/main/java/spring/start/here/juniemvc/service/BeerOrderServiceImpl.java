package spring.start.here.juniemvc.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
class BeerOrderServiceImpl implements BeerOrderService {

    private final BeerOrderRepository beerOrderRepository;
    private final CustomerRepository customerRepository;
    private final BeerRepository beerRepository;
    private final BeerOrderMapper beerOrderMapper;
    private final BeerOrderLineMapper beerOrderLineMapper;

    BeerOrderServiceImpl(BeerOrderRepository beerOrderRepository,
                         CustomerRepository customerRepository,
                         BeerRepository beerRepository,
                         BeerOrderMapper beerOrderMapper,
                         BeerOrderLineMapper beerOrderLineMapper) {
        this.beerOrderRepository = beerOrderRepository;
        this.customerRepository = customerRepository;
        this.beerRepository = beerRepository;
        this.beerOrderMapper = beerOrderMapper;
        this.beerOrderLineMapper = beerOrderLineMapper;
    }

    @Override
    @Transactional
    public BeerOrderDto createOrder(BeerOrderUpsertDto upsertDto) {
        Customer customer = customerRepository.findById(upsertDto.customerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found: " + upsertDto.customerId()));

        BeerOrder order = BeerOrder.builder()
                .customer(customer)
                .orderStatus("NEW")
                .orderStatusCallbackUrl(upsertDto.orderStatusCallbackUrl())
                .build();

        List<BeerOrderLine> lines = new ArrayList<>();
        for (BeerOrderLineUpsertDto lineDto : upsertDto.orderLines()) {
            Beer beer = beerRepository.findById(lineDto.beerId())
                    .orElseThrow(() -> new IllegalArgumentException("Beer not found: " + lineDto.beerId()));
            BeerOrderLine line = BeerOrderLine.builder()
                    .beer(beer)
                    .beerOrder(order)
                    .orderQuantity(lineDto.orderQuantity())
                    .quantityAllocated(0)
                    .build();
            lines.add(line);
        }
        order.getBeerOrderLines().addAll(lines);

        BeerOrder saved = beerOrderRepository.save(order);
        return beerOrderMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BeerOrderDto> getById(Integer id) {
        return beerOrderRepository.findById(id).map(beerOrderMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BeerOrderDto> getAll(Integer pageNumber, Integer pageSize) {
        if (pageNumber != null && pageSize != null) {
            return beerOrderRepository.findAll(PageRequest.of(pageNumber, pageSize))
                    .map(beerOrderMapper::toDto)
                    .toList();
        }
        return beerOrderRepository.findAll().stream().map(beerOrderMapper::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BeerOrderDto> getByCustomer(Integer customerId, Integer pageNumber, Integer pageSize) {
        return customerRepository.findById(customerId)
                .map(customer -> beerOrderRepository.findAllByCustomer(customer).stream()
                        .map(beerOrderMapper::toDto).toList())
                .orElseGet(List::of);
    }

    @Override
    @Transactional
    public Optional<BeerOrderDto> updateStatus(Integer id, String orderStatus) {
        return beerOrderRepository.findById(id).map(order -> {
            order.setOrderStatus(orderStatus);
            return beerOrderMapper.toDto(beerOrderRepository.save(order));
        });
    }

    @Override
    @Transactional
    public boolean delete(Integer id) {
        if (!beerOrderRepository.existsById(id)) return false;
        beerOrderRepository.deleteById(id);
        return true;
    }
}
