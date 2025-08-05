package spring.start.here.juniemvc.web.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.start.here.juniemvc.service.BeerService;
import spring.start.here.juniemvc.web.model.BeerDto;
import spring.start.here.juniemvc.web.model.BeerListDto;
import spring.start.here.juniemvc.web.model.BeerUpsertDto;

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
     * @param beerUpsertDto the beer to create
     * @return the created beer
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BeerDto createBeer(@Valid @RequestBody BeerUpsertDto beerUpsertDto) {
        return beerService.saveBeer(beerUpsertDto);
    }

    /**
     * Get a beer by ID
     * @param beerId the beer ID
     * @return the beer if found, or 404 if not found
     */
    @GetMapping("/{beerId}")
    public ResponseEntity<BeerDto> getBeerById(@PathVariable("beerId") Integer beerId) {
        return beerService.getBeerById(beerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all beers with pagination
     * @param pageNumber the page number (0-based)
     * @param pageSize the page size
     * @return list of all beers with pagination information
     */
    @GetMapping
    public BeerListDto getAllBeers(
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize) {
        return beerService.getAllBeers(pageNumber, pageSize);
    }

    /**
     * Update an existing beer
     * @param beerId the beer ID to update
     * @param beerUpsertDto the updated beer data
     * @return the updated beer if found, or 404 if not found
     */
    @PutMapping("/{beerId}")
    public ResponseEntity<BeerDto> updateBeer(
            @PathVariable("beerId") Integer beerId,
            @Valid @RequestBody BeerUpsertDto beerUpsertDto) {
        return beerService.updateBeer(beerId, beerUpsertDto)
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