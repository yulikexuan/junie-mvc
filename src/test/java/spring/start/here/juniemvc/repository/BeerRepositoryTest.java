package spring.start.here.juniemvc.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import spring.start.here.juniemvc.domain.model.Beer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    @Rollback
    void testSaveBeer() {
        // Given
        Beer beer = Beer.builder()
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        // When
        Beer savedBeer = beerRepository.save(beer);

        // Then
        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
    }

    @Test
    void testGetBeerById() {
        // Given
        Beer beer = Beer.builder()
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();
        Beer savedBeer = beerRepository.save(beer);

        // When
        Optional<Beer> fetchedBeerOptional = beerRepository.findById(savedBeer.getId());

        // Then
        assertThat(fetchedBeerOptional).isPresent();
        Beer fetchedBeer = fetchedBeerOptional.get();
        assertThat(fetchedBeer.getBeerName()).isEqualTo("Test Beer");
        assertThat(fetchedBeer.getBeerStyle()).isEqualTo("IPA");
    }

    @Test
    void testGetAllBeers() {
        // Given
        Beer beer1 = Beer.builder()
                .beerName("Beer 1")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        Beer beer2 = Beer.builder()
                .beerName("Beer 2")
                .beerStyle("Stout")
                .upc("654321")
                .price(new BigDecimal("11.99"))
                .quantityOnHand(200)
                .build();

        beerRepository.save(beer1);
        beerRepository.save(beer2);

        // When
        List<Beer> beerList = beerRepository.findAll();

        // Then
        assertThat(beerList).isNotNull();
        assertThat(beerList.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    void testUpdateBeer() {
        // Given
        Beer beer = Beer.builder()
                .beerName("Original Beer")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();
        Beer savedBeer = beerRepository.save(beer);

        // When
        savedBeer.setBeerName("Updated Beer");
        savedBeer.setQuantityOnHand(200);
        Beer updatedBeer = beerRepository.save(savedBeer);

        // Then
        assertThat(updatedBeer.getBeerName()).isEqualTo("Updated Beer");
        assertThat(updatedBeer.getQuantityOnHand()).isEqualTo(200);
    }

    @Test
    void testDeleteBeer() {
        // Given
        Beer beer = Beer.builder()
                .beerName("Delete Me")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();
        Beer savedBeer = beerRepository.save(beer);

        // When
        beerRepository.deleteById(savedBeer.getId());
        Optional<Beer> deletedBeer = beerRepository.findById(savedBeer.getId());

        // Then
        assertThat(deletedBeer).isEmpty();
    }
}