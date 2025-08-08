package spring.start.here.juniemvc.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.start.here.juniemvc.domain.model.Customer;
import spring.start.here.juniemvc.repository.CustomerRepository;
import spring.start.here.juniemvc.web.mappers.CustomerMapper;
import spring.start.here.juniemvc.web.model.CustomerDto;
import spring.start.here.juniemvc.web.model.CustomerUpsertDto;

import java.util.List;
import java.util.Optional;

@Service
class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    @Transactional
    public CustomerDto create(CustomerUpsertDto upsertDto) {
        Customer entity = customerMapper.toEntity(upsertDto);
        Customer saved = customerRepository.save(entity);
        return customerMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerDto> getById(Integer id) {
        return customerRepository.findById(id).map(customerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDto> getAll() {
        return customerRepository.findAll().stream().map(customerMapper::toDto).toList();
    }

    @Override
    @Transactional
    public Optional<CustomerDto> update(Integer id, CustomerUpsertDto upsertDto) {
        return customerRepository.findById(id).map(existing -> {
            existing.setName(upsertDto.name());
            existing.setEmail(upsertDto.email());
            existing.setPhone(upsertDto.phone());
            return customerMapper.toDto(customerRepository.save(existing));
        });
    }

    @Override
    @Transactional
    public boolean delete(Integer id) {
        if (!customerRepository.existsById(id)) return false;
        customerRepository.deleteById(id);
        return true;
    }
}
