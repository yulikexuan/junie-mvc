package spring.start.here.juniemvc.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import spring.start.here.juniemvc.service.BeerService;
import spring.start.here.juniemvc.web.exception.GlobalExceptionHandler;
import spring.start.here.juniemvc.web.model.BeerDto;
import spring.start.here.juniemvc.web.model.BeerListDto;
import spring.start.here.juniemvc.web.model.BeerUpsertDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BeerControllerTest {

    @Mock
    private BeerService beerService;

    @InjectMocks
    private BeerController beerController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private BeerDto validBeerDto;
    private BeerUpsertDto validBeerUpsertDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(beerController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();

        validBeerDto = BeerDto.builder()
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

        validBeerUpsertDto = BeerUpsertDto.builder()
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456789012")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();
    }

    @Test
    void testCreateBeer() throws Exception {
        // Given
        given(beerService.saveBeer(any(BeerUpsertDto.class))).willReturn(validBeerDto);

        // When/Then
        mockMvc.perform(post("/api/v1/beers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validBeerUpsertDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.beerName", is("Test Beer")))
                .andExpect(jsonPath("$.beerStyle", is("IPA")));
    }

    @Test
    void testGetBeerById() throws Exception {
        // Given
        given(beerService.getBeerById(1)).willReturn(Optional.of(validBeerDto));

        // When/Then
        mockMvc.perform(get("/api/v1/beers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.beerName", is("Test Beer")))
                .andExpect(jsonPath("$.beerStyle", is("IPA")));
    }

    @Test
    void testGetBeerByIdNotFound() throws Exception {
        // Given
        given(beerService.getBeerById(1)).willReturn(Optional.empty());

        // When/Then
        mockMvc.perform(get("/api/v1/beers/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllBeers() throws Exception {
        // Given
        BeerListDto beerListDto = BeerListDto.builder()
                .beers(List.of(validBeerDto))
                .currentPage(0)
                .totalPages(1)
                .totalElements(1)
                .build();

        given(beerService.getAllBeers(any(), any())).willReturn(beerListDto);

        // When/Then
        mockMvc.perform(get("/api/v1/beers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.beers", hasSize(1)))
                .andExpect(jsonPath("$.beers[0].id", is(1)))
                .andExpect(jsonPath("$.beers[0].beerName", is("Test Beer")))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.currentPage", is(0)))
                .andExpect(jsonPath("$.totalElements", is(1)));
    }

    @Test
    void testUpdateBeer() throws Exception {
        // Given
        given(beerService.updateBeer(anyInt(), any(BeerUpsertDto.class))).willReturn(Optional.of(validBeerDto));

        // When/Then
        mockMvc.perform(put("/api/v1/beers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validBeerUpsertDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.beerName", is("Test Beer")))
                .andExpect(jsonPath("$.beerStyle", is("IPA")));

        verify(beerService).updateBeer(anyInt(), any(BeerUpsertDto.class));
    }

    @Test
    void testUpdateBeerNotFound() throws Exception {
        // Given
        given(beerService.updateBeer(anyInt(), any(BeerUpsertDto.class))).willReturn(Optional.empty());

        // When/Then
        mockMvc.perform(put("/api/v1/beers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validBeerUpsertDto)))
                .andExpect(status().isNotFound());

        verify(beerService).updateBeer(anyInt(), any(BeerUpsertDto.class));
    }

    @Test
    void testDeleteBeer() throws Exception {
        // Given
        given(beerService.deleteBeer(1)).willReturn(true);

        // When/Then
        mockMvc.perform(delete("/api/v1/beers/1"))
                .andExpect(status().isNoContent());

        verify(beerService).deleteBeer(1);
    }

    @Test
    void testDeleteBeerNotFound() throws Exception {
        // Given
        given(beerService.deleteBeer(1)).willReturn(false);

        // When/Then
        mockMvc.perform(delete("/api/v1/beers/1"))
                .andExpect(status().isNotFound());

        verify(beerService).deleteBeer(1);
    }

    @Test
    void testValidationFailure() throws Exception {
        // Given
        BeerUpsertDto invalidBeerDto = BeerUpsertDto.builder()
                .beerName("") // Invalid: blank name
                .beerStyle("IPA")
                .upc("123456789012")
                .price(new BigDecimal("-1.0")) // Invalid: negative price
                .quantityOnHand(-5) // Invalid: negative quantity
                .build();

        // When/Then
        mockMvc.perform(post("/api/v1/beers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidBeerDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").exists())
                .andExpect(jsonPath("$.title", is("Validation Error")))
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    void testPagination() throws Exception {
        // Given
        BeerListDto beerListDto = BeerListDto.builder()
                .beers(List.of(validBeerDto))
                .currentPage(1)
                .totalPages(5)
                .totalElements(10)
                .build();

        given(beerService.getAllBeers(1, 2)).willReturn(beerListDto);

        // When/Then
        mockMvc.perform(get("/api/v1/beers")
                .param("pageNumber", "1")
                .param("pageSize", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage", is(1)))
                .andExpect(jsonPath("$.totalPages", is(5)))
                .andExpect(jsonPath("$.totalElements", is(10)));
    }
}