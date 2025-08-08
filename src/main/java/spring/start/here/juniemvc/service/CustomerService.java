package spring.start.here.juniemvc.service;

import spring.start.here.juniemvc.web.model.CustomerDto;
import spring.start.here.juniemvc.web.model.CustomerUpsertDto;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    CustomerDto create(CustomerUpsertDto upsertDto);
    Optional<CustomerDto> getById(Integer id);
    List<CustomerDto> getAll();
    Optional<CustomerDto> update(Integer id, CustomerUpsertDto upsertDto);
    boolean delete(Integer id);
}
