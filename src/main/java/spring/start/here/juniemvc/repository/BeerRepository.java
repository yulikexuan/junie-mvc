package spring.start.here.juniemvc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.start.here.juniemvc.domain.model.Beer;

/**
 * Spring Data JPA Repository for Beer entity
 */
@Repository
public interface BeerRepository extends JpaRepository<Beer, Integer> {
    // No custom methods needed for basic CRUD operations
    // JpaRepository provides findAll, findById, save, delete, etc.
}