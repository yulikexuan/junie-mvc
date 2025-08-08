package spring.start.here.juniemvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.start.here.juniemvc.domain.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
