package spring.start.here.juniemvc.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.start.here.juniemvc.domain.model.Beer;
import spring.start.here.juniemvc.service.BeerService;

import java.util.List;

/**
 * REST Controller for Beer operations
 */
@RestController
@RequestMapping("/api/v1/beers")
public class BeerController {

    private final BeerService beerService;

    public BeerController(BeerService beerService) {
        this.beerService = beerService;
    }

    /**
     * Create a new beer
     * @param beer the beer to create
     * @return the created beer
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Beer createBeer(@RequestBody Beer beer) {
        return beerService.saveBeer(beer);
    }

    /**
     * Get a beer by ID
     * @param beerId the beer ID
     * @return the beer if found, or 404 if not found
     */
    @GetMapping("/{beerId}")
    public ResponseEntity<Beer> getBeerById(@PathVariable("beerId") Integer beerId) {
        return beerService.getBeerById(beerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all beers
     * @return list of all beers
     */
    @GetMapping
    public List<Beer> getAllBeers() {
        return beerService.getAllBeers();
    }

    /**
     * Update an existing beer
     * @param beerId the beer ID to update
     * @param beer the updated beer data
     * @return the updated beer if found, or 404 if not found
     */
    @PutMapping("/{beerId}")
    public ResponseEntity<Beer> updateBeer(@PathVariable("beerId") Integer beerId, @RequestBody Beer beer) {
        return beerService.updateBeer(beerId, beer)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete a beer by ID
     * @param beerId the beer ID to delete
     * @return 204 No Content if deleted, or 404 if not found
     */
    @DeleteMapping("/{beerId}")
    public ResponseEntity<Void> deleteBeer(@PathVariable("beerId") Integer beerId) {
        return beerService.deleteBeer(beerId)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}