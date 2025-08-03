package spring.start.here.juniemvc.service;

import org.springframework.stereotype.Service;
import spring.start.here.juniemvc.domain.model.Beer;
import spring.start.here.juniemvc.repository.BeerRepository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of BeerService interface
 */
@Service
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;

    public BeerServiceImpl(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    @Override
    public Beer saveBeer(Beer beer) {
        return beerRepository.save(beer);
    }

    @Override
    public Optional<Beer> getBeerById(Integer id) {
        return beerRepository.findById(id);
    }

    @Override
    public List<Beer> getAllBeers() {
        return beerRepository.findAll();
    }

    @Override
    public Optional<Beer> updateBeer(Integer id, Beer beer) {
        return beerRepository.findById(id)
                .map(existingBeer -> {
                    // Update the existing beer with new values
                    existingBeer.setBeerName(beer.getBeerName());
                    existingBeer.setBeerStyle(beer.getBeerStyle());
                    existingBeer.setUpc(beer.getUpc());
                    existingBeer.setPrice(beer.getPrice());
                    existingBeer.setQuantityOnHand(beer.getQuantityOnHand());

                    // Save the updated beer
                    return beerRepository.save(existingBeer);
                });
    }

    @Override
    public boolean deleteBeer(Integer id) {
        return beerRepository.findById(id)
                .map(beer -> {
                    beerRepository.delete(beer);
                    return true;
                })
                .orElse(false);
    }
}