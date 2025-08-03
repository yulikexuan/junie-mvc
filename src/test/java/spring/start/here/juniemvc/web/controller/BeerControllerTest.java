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
import spring.start.here.juniemvc.domain.model.Beer;
import spring.start.here.juniemvc.service.BeerService;

import java.math.BigDecimal;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(beerController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateBeer() throws Exception {
        // Given
        Beer beer = Beer.builder()
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

        given(beerService.saveBeer(any(Beer.class))).willReturn(savedBeer);

        // When/Then
        mockMvc.perform(post("/api/v1/beers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.beerName", is("Test Beer")))
                .andExpect(jsonPath("$.beerStyle", is("IPA")));
    }

    @Test
    void testGetBeerById() throws Exception {
        // Given
        Beer beer = Beer.builder()
                .id(1)
                .beerName("Test Beer")
                .beerStyle("IPA")
                .upc("123456")
                .price(new BigDecimal("12.99"))
                .quantityOnHand(100)
                .build();

        given(beerService.getBeerById(1)).willReturn(Optional.of(beer));

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

        given(beerService.getAllBeers()).willReturn(beers);

        // When/Then
        mockMvc.perform(get("/api/v1/beers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].beerName", is("Beer 1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].beerName", is("Beer 2")));
    }

    @Test
    void testUpdateBeer() throws Exception {
        // Given
        Beer beerToUpdate = Beer.builder()
                .beerName("Updated Beer")
                .beerStyle("Stout")
                .upc("654321")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(200)
                .build();

        Beer updatedBeer = Beer.builder()
                .id(1)
                .beerName("Updated Beer")
                .beerStyle("Stout")
                .upc("654321")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(200)
                .build();

        given(beerService.updateBeer(anyInt(), any(Beer.class))).willReturn(Optional.of(updatedBeer));

        // When/Then
        mockMvc.perform(put("/api/v1/beers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.beerName", is("Updated Beer")))
                .andExpect(jsonPath("$.beerStyle", is("Stout")))
                .andExpect(jsonPath("$.upc", is("654321")))
                .andExpect(jsonPath("$.price", is(14.99)))
                .andExpect(jsonPath("$.quantityOnHand", is(200)));

        verify(beerService).updateBeer(anyInt(), any(Beer.class));
    }

    @Test
    void testUpdateBeerNotFound() throws Exception {
        // Given
        Beer beerToUpdate = Beer.builder()
                .beerName("Updated Beer")
                .beerStyle("Stout")
                .upc("654321")
                .price(new BigDecimal("14.99"))
                .quantityOnHand(200)
                .build();

        given(beerService.updateBeer(anyInt(), any(Beer.class))).willReturn(Optional.empty());

        // When/Then
        mockMvc.perform(put("/api/v1/beers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerToUpdate)))
                .andExpect(status().isNotFound());

        verify(beerService).updateBeer(anyInt(), any(Beer.class));
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
}