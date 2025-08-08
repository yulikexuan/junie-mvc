package spring.start.here.juniemvc.web.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.start.here.juniemvc.service.CustomerService;
import spring.start.here.juniemvc.web.model.CustomerDto;
import spring.start.here.juniemvc.web.model.CustomerUpsertDto;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
class CustomerController {

    private final CustomerService customerService;

    CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CustomerDto create(@Valid @RequestBody CustomerUpsertDto upsertDto) {
        return customerService.create(upsertDto);
    }

    @GetMapping("/{id}")
    ResponseEntity<CustomerDto> getById(@PathVariable Integer id) {
        return customerService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    List<CustomerDto> getAll() {
        return customerService.getAll();
    }

    @PutMapping("/{id}")
    ResponseEntity<CustomerDto> update(@PathVariable Integer id, @Valid @RequestBody CustomerUpsertDto upsertDto) {
        return customerService.update(id, upsertDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Integer id) {
        return customerService.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
