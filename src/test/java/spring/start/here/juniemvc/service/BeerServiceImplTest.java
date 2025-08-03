package spring.start.here.juniemvc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import spring.start.here.juniemvc.domain.model.Beer;
import spring.start.here.juniemvc.repository.BeerRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class BeerServiceImplTest {

    @Mock
    private BeerRepository beerRepository;

    @InjectMocks
    private BeerServiceImpl beerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveBeer() {
        // Given
        Beer beerToSave = Beer.builder()
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        Beer savedBeer = Beer.builder()
                .id(1)
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        given(beerRepository.save(any(Beer.class))).willReturn(savedBeer);

        // When
        Beer result = beerService.saveBeer(beerToSave);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getBeerName()).isEqualTo("Test Beer");
        assertThat(result.getBeerStyle()).isEqualTo("IPA");
        assertThat(result.getUpc()).isEqualTo("123456");
        assertThat(result.getPrice()).isEqualTo(new BigDecimal("12.99"));
        assertThat(result.getQuantityOnHand()).isEqualTo(100);

        then(beerRepository).should(times(1)).save(any(Beer.class));
    }

    @Test
    void testGetBeerById() {
        // Given
        Beer beer = Beer.builder()
                .id(1)
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        given(beerRepository.findById(1)).willReturn(Optional.of(beer));

        // When
        Optional<Beer> result = beerService.getBeerById(1);

        // Then
        assertThat(result).isPresent();
        Beer fetchedBeer = result.get();
        assertThat(fetchedBeer.getId()).isEqualTo(1);
        assertThat(fetchedBeer.getBeerName()).isEqualTo("Test Beer");
        assertThat(fetchedBeer.getBeerStyle()).isEqualTo("IPA");
        assertThat(fetchedBeer.getUpc()).isEqualTo("123456");
        assertThat(fetchedBeer.getPrice()).isEqualTo(new BigDecimal("12.99"));
        assertThat(fetchedBeer.getQuantityOnHand()).isEqualTo(100);

        then(beerRepository).should(times(1)).findById(1);
    }

    @Test
    void testGetBeerByIdNotFound() {
        // Given
        given(beerRepository.findById(1)).willReturn(Optional.empty());

        // When
        Optional<Beer> result = beerService.getBeerById(1);

        // Then
        assertThat(result).isEmpty();
        then(beerRepository).should(times(1)).findById(1);
    }

    @Test
    void testGetAllBeers() {
        // Given
        List<Beer> beers = Arrays.asList(
                Beer.builder()
                        .id(1)
                        .beerName("Beer 1")
                        .beerStyle("IPA")
                        .upc("123456")
                        .price(new BigDecimal("12.99"))
                        .quantityOnHand(100)
                        .build(),
                Beer.builder()
                        .id(2)
                        .beerName("Beer 2")
                        .beerStyle("Stout")
                        .upc("654321")
                        .price(new BigDecimal("11.99"))
                        .quantityOnHand(200)
                        .build()
        );

        given(beerRepository.findAll()).willReturn(beers);

        // When
        List<Beer> result = beerService.getAllBeers();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getId()).isEqualTo(1);
        assertThat(result.get(0).getBeerName()).isEqualTo("Beer 1");
        assertThat(result.get(1).getId()).isEqualTo(2);
        assertThat(result.get(1).getBeerName()).isEqualTo("Beer 2");

        then(beerRepository).should(times(1)).findAll();
    }

    @Test
    void testGetAllBeersEmpty() {
        // Given
        given(beerRepository.findAll()).willReturn(List.of());

        // When
        List<Beer> result = beerService.getAllBeers();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        then(beerRepository).should(times(1)).findAll();
    }

    @Test
    void testUpdateBeerSuccess() {
        // Given
        Beer existingBeer = Beer.builder()
                .id(1)
                .beerName("Original Beer")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        Beer updatedBeerData = Beer.builder()
                .beerName("Updated Beer")
                .beerStyle("Stout")
                .upc("654321")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(200)
                .build();

        Beer savedBeer = Beer.builder()
                .id(1)
                .beerName("Updated Beer")
                .beerStyle("Stout")
                .upc("654321")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(200)
                .build();

        given(beerRepository.findById(1)).willReturn(Optional.of(existingBeer));
        given(beerRepository.save(any(Beer.class))).willReturn(savedBeer);

        // When
        Optional<Beer> result = beerService.updateBeer(1, updatedBeerData);

        // Then
        assertThat(result).isPresent();
        Beer resultBeer = result.get();
        assertThat(resultBeer.getId()).isEqualTo(1);
        assertThat(resultBeer.getBeerName()).isEqualTo("Updated Beer");
        assertThat(resultBeer.getBeerStyle()).isEqualTo("Stout");
        assertThat(resultBeer.getUpc()).isEqualTo("654321");
        assertThat(resultBeer.getPrice()).isEqualTo(new BigDecimal("14.99"));
        assertThat(resultBeer.getQuantityOnHand()).isEqualTo(200);

        verify(beerRepository).findById(1);
        verify(beerRepository).save(any(Beer.class));
    }

    @Test
    void testUpdateBeerNotFound() {
        // Given
        Beer updatedBeerData = Beer.builder()
                .beerName("Updated Beer")
                .beerStyle("Stout")
                .upc("654321")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(200)
                .build();

        given(beerRepository.findById(1)).willReturn(Optional.empty());

        // When
        Optional<Beer> result = beerService.updateBeer(1, updatedBeerData);

        // Then
        assertThat(result).isEmpty();
        verify(beerRepository).findById(1);
    }

    @Test
    void testDeleteBeerSuccess() {
        // Given
        Beer beerToDelete = Beer.builder()
                .id(1)
                .beerName("Beer to Delete")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        given(beerRepository.findById(1)).willReturn(Optional.of(beerToDelete));

        // When
        boolean result = beerService.deleteBeer(1);

        // Then
        assertThat(result).isTrue();
        verify(beerRepository).findById(1);
        verify(beerRepository).delete(beerToDelete);
    }

    @Test
    void testDeleteBeerNotFound() {
        // Given
        given(beerRepository.findById(1)).willReturn(Optional.empty());

        // When
        boolean result = beerService.deleteBeer(1);

        // Then
        assertThat(result).isFalse();
        verify(beerRepository).findById(1);
    }
}