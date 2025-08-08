package spring.start.here.juniemvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.start.here.juniemvc.domain.model.Beer;
import spring.start.here.juniemvc.domain.model.BeerInventory;

import java.util.List;

@Repository
public interface BeerInventoryRepository extends JpaRepository<BeerInventory, Integer> {
    List<BeerInventory> findAllByBeer(Beer beer);
}
