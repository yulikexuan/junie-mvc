package spring.start.here.juniemvc.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import spring.start.here.juniemvc.domain.model.Beer;
import spring.start.here.juniemvc.repository.BeerRepository;
import spring.start.here.juniemvc.web.mappers.BeerMapper;
import spring.start.here.juniemvc.web.model.BeerDto;
import spring.start.here.juniemvc.web.model.BeerListDto;
import spring.start.here.juniemvc.web.model.BeerUpsertDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BeerServiceImplTest {

    @Mock
    BeerRepository beerRepository;

    @Mock
    BeerMapper beerMapper;

    @InjectMocks
    BeerServiceImpl beerService;

    Beer testBeer;
    BeerDto testBeerDto;
    BeerUpsertDto testBeerUpsertDto;

    @BeforeEach
    void setUp() {
        testBeer = Beer.builder()
                .id(1)
                .version(1)
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456789012")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        testBeerDto = BeerDto.builder()
                .id(1)
                .version(1)
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456789012")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();

        testBeerUpsertDto = BeerUpsertDto.builder()
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456789012")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();
    }

    @Test
    void testSaveBeer() {
        // Given
        given(beerMapper.beerUpsertDtoToBeer(any(BeerUpsertDto.class))).willReturn(testBeer);
        given(beerRepository.save(any(Beer.class))).willReturn(testBeer);
        given(beerMapper.beerToBeerDto(any(Beer.class))).willReturn(testBeerDto);

        // When
        BeerDto savedBeerDto = beerService.saveBeer(testBeerUpsertDto);

        // Then
        assertThat(savedBeerDto).isNotNull();
        assertThat(savedBeerDto.getId()).isEqualTo(testBeerDto.getId());
        verify(beerRepository).save(any(Beer.class));
    }

    @Test
    void testGetBeerById() {
        // Given
        given(beerRepository.findById(1)).willReturn(Optional.of(testBeer));
        given(beerMapper.beerToBeerDto(testBeer)).willReturn(testBeerDto);

        // When
        Optional<BeerDto> foundBeerDto = beerService.getBeerById(1);

        // Then
        assertThat(foundBeerDto).isPresent();
        assertThat(foundBeerDto.get().getId()).isEqualTo(testBeerDto.getId());
    }

    @Test
    void testGetBeerByIdNotFound() {
        // Given
        given(beerRepository.findById(1)).willReturn(Optional.empty());

        // When
        Optional<BeerDto> foundBeerDto = beerService.getBeerById(1);

        // Then
        assertThat(foundBeerDto).isEmpty();
    }

    @Test
    void testGetAllBeers() {
        // Given
        Page<Beer> beerPage = new PageImpl<>(List.of(testBeer));
        given(beerRepository.findAll(any(PageRequest.class))).willReturn(beerPage);
        given(beerMapper.beerToBeerDto(testBeer)).willReturn(testBeerDto);

        // When
        BeerListDto beerListDto = beerService.getAllBeers(0, 10);

        // Then
        assertThat(beerListDto).isNotNull();
        assertThat(beerListDto.getBeers()).hasSize(1);
        assertThat(beerListDto.getTotalPages()).isEqualTo(1);
        assertThat(beerListDto.getCurrentPage()).isEqualTo(0);
        assertThat(beerListDto.getTotalElements()).isEqualTo(1);
    }

    @Test
    void testGetAllBeersWithDefaultPagination() {
        // Given
        Page<Beer> beerPage = new PageImpl<>(List.of(testBeer));
        given(beerRepository.findAll(any(PageRequest.class))).willReturn(beerPage);
        given(beerMapper.beerToBeerDto(testBeer)).willReturn(testBeerDto);

        // When
        BeerListDto beerListDto = beerService.getAllBeers(null, null);

        // Then
        assertThat(beerListDto).isNotNull();
        assertThat(beerListDto.getBeers()).hasSize(1);
        verify(beerRepository).findAll(any(PageRequest.class));
    }

    @Test
    void testUpdateBeer() {
        // Given
        given(beerRepository.findById(1)).willReturn(Optional.of(testBeer));
        given(beerRepository.save(any(Beer.class))).willReturn(testBeer);
        given(beerMapper.beerToBeerDto(testBeer)).willReturn(testBeerDto);

        // When
        Optional<BeerDto> updatedBeerDto = beerService.updateBeer(1, testBeerUpsertDto);

        // Then
        assertThat(updatedBeerDto).isPresent();
        assertThat(updatedBeerDto.get().getId()).isEqualTo(testBeerDto.getId());
        verify(beerRepository).save(testBeer);
    }

    @Test
    void testUpdateBeerNotFound() {
        // Given
        given(beerRepository.findById(1)).willReturn(Optional.empty());

        // When
        Optional<BeerDto> updatedBeerDto = beerService.updateBeer(1, testBeerUpsertDto);

        // Then
        assertThat(updatedBeerDto).isEmpty();
    }

    @Test
    void testDeleteBeer() {
        // Given
        given(beerRepository.findById(1)).willReturn(Optional.of(testBeer));

        // When
        boolean result = beerService.deleteBeer(1);

        // Then
        assertThat(result).isTrue();
        verify(beerRepository).delete(testBeer);
    }

    @Test
    void testDeleteBeerNotFound() {
        // Given
        given(beerRepository.findById(1)).willReturn(Optional.empty());

        // When
        boolean result = beerService.deleteBeer(1);

        // Then
        assertThat(result).isFalse();
    }
}