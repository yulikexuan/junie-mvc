package spring.start.here.juniemvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.start.here.juniemvc.domain.model.BeerOrder;
import spring.start.here.juniemvc.domain.model.Customer;

import java.util.List;

@Repository
public interface BeerOrderRepository extends JpaRepository<BeerOrder, Integer> {
    List<BeerOrder> findAllByCustomer(Customer customer);
}
