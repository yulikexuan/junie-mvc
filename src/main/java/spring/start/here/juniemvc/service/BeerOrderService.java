package spring.start.here.juniemvc.service;

import spring.start.here.juniemvc.web.model.BeerOrderDto;
import spring.start.here.juniemvc.web.model.BeerOrderUpsertDto;

import java.util.List;
import java.util.Optional;

public interface BeerOrderService {
    BeerOrderDto createOrder(BeerOrderUpsertDto upsertDto);
    Optional<BeerOrderDto> getById(Integer id);
    List<BeerOrderDto> getAll(Integer pageNumber, Integer pageSize);
    List<BeerOrderDto> getByCustomer(Integer customerId, Integer pageNumber, Integer pageSize);
    Optional<BeerOrderDto> updateStatus(Integer id, String orderStatus);
    boolean delete(Integer id);
}
