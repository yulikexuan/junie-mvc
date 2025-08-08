package spring.start.here.juniemvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.start.here.juniemvc.domain.model.BeerOrderLine;

@Repository
public interface BeerOrderLineRepository extends JpaRepository<BeerOrderLine, Integer> {
}
