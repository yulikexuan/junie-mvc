package spring.start.here.juniemvc.web.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.start.here.juniemvc.service.BeerOrderService;
import spring.start.here.juniemvc.web.model.BeerOrderDto;
import spring.start.here.juniemvc.web.model.BeerOrderUpsertDto;

import java.util.List;

@RestController
@RequestMapping("/api/v1/beer-orders")
class BeerOrderController {

    private final BeerOrderService beerOrderService;

    BeerOrderController(BeerOrderService beerOrderService) {
        this.beerOrderService = beerOrderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    BeerOrderDto createBeerOrder(@Valid @RequestBody BeerOrderUpsertDto upsert) {
        return beerOrderService.createOrder(upsert);
    }

    @GetMapping("/{orderId}")
    ResponseEntity<BeerOrderDto> getBeerOrderById(@PathVariable("orderId") Integer orderId) {
        return beerOrderService.getById(orderId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    List<BeerOrderDto> getAllBeerOrders(@RequestParam(required = false) Integer pageNumber,
                                         @RequestParam(required = false) Integer pageSize) {
        return beerOrderService.getAll(pageNumber, pageSize);
    }

    @GetMapping("/customer/{customerId}")
    List<BeerOrderDto> getBeerOrdersByCustomer(@PathVariable("customerId") Integer customerId,
                                               @RequestParam(required = false) Integer pageNumber,
                                               @RequestParam(required = false) Integer pageSize) {
        return beerOrderService.getByCustomer(customerId, pageNumber, pageSize);
    }

    @PutMapping("/{orderId}/status")
    ResponseEntity<BeerOrderDto> updateBeerOrderStatus(@PathVariable("orderId") Integer orderId,
                                                       @RequestParam String orderStatus) {
        return beerOrderService.updateStatus(orderId, orderStatus)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{orderId}")
    ResponseEntity<Void> deleteBeerOrder(@PathVariable("orderId") Integer orderId) {
        return beerOrderService.delete(orderId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
