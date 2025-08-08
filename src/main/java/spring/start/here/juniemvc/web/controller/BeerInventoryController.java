package spring.start.here.juniemvc.web.controller;

import org.springframework.web.bind.annotation.*;
import spring.start.here.juniemvc.service.BeerInventoryService;
import spring.start.here.juniemvc.web.model.BeerInventoryDto;

import java.util.List;

@RestController
@RequestMapping("/api/v1/beer-inventory")
class BeerInventoryController {

    private final BeerInventoryService beerInventoryService;

    BeerInventoryController(BeerInventoryService beerInventoryService) {
        this.beerInventoryService = beerInventoryService;
    }

    @GetMapping
    List<BeerInventoryDto> getAll() {
        return beerInventoryService.getAll();
    }

    @GetMapping("/beer/{beerId}")
    List<BeerInventoryDto> getByBeer(@PathVariable Integer beerId) {
        return beerInventoryService.getByBeerId(beerId);
    }
}
