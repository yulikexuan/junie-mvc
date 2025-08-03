package spring.start.here.juniemvc.service;

import spring.start.here.juniemvc.domain.model.Beer;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Beer operations
 */
public interface BeerService {

    /**
     * Save a beer
     * @param beer the beer to save
     * @return the saved beer
     */
    Beer saveBeer(Beer beer);

    /**
     * Get a beer by ID
     * @param id the beer ID
     * @return the beer if found
     */
    Optional<Beer> getBeerById(Integer id);

    /**
     * Get all beers
     * @return list of all beers
     */
    List<Beer> getAllBeers();

    /**
     * Update an existing beer
     * @param id the beer ID to update
     * @param beer the updated beer data
     * @return the updated beer if found, empty otherwise
     */
    Optional<Beer> updateBeer(Integer id, Beer beer);

    /**
     * Delete a beer by ID
     * @param id the beer ID to delete
     * @return true if beer was deleted, false if not found
     */
    boolean deleteBeer(Integer id);
}