package spring.start.here.juniemvc.service;

import spring.start.here.juniemvc.web.model.BeerDto;
import spring.start.here.juniemvc.web.model.BeerListDto;
import spring.start.here.juniemvc.web.model.BeerUpsertDto;

import java.util.Optional;

/**
 * Service interface for Beer operations
 */
public interface BeerService {

    /**
     * Save a beer
     * @param beerUpsertDto the beer to save
     * @return the saved beer
     */
    BeerDto saveBeer(BeerUpsertDto beerUpsertDto);

    /**
     * Get a beer by ID
     * @param id the beer ID
     * @return the beer if found
     */
    Optional<BeerDto> getBeerById(Integer id);

    /**
     * Get all beers
     * @param pageNumber the page number (0-based)
     * @param pageSize the page size
     * @return list of all beers with pagination
     */
    BeerListDto getAllBeers(Integer pageNumber, Integer pageSize);

    /**
     * Update an existing beer
     * @param id the beer ID to update
     * @param beerUpsertDto the updated beer data
     * @return the updated beer if found, empty otherwise
     */
    Optional<BeerDto> updateBeer(Integer id, BeerUpsertDto beerUpsertDto);

    /**
     * Delete a beer by ID
     * @param id the beer ID to delete
     * @return true if beer was deleted, false if not found
     */
    boolean deleteBeer(Integer id);
}