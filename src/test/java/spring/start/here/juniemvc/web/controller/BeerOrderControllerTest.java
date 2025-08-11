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
import spring.start.here.juniemvc.service.BeerOrderService;
import spring.start.here.juniemvc.web.exception.GlobalExceptionHandler;
import spring.start.here.juniemvc.web.model.BeerOrderDto;
import spring.start.here.juniemvc.web.model.BeerOrderLineDto;
import spring.start.here.juniemvc.web.model.BeerOrderLineUpsertDto;
import spring.start.here.juniemvc.web.model.BeerOrderUpsertDto;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BeerOrderControllerTest {

    @Mock
    private BeerOrderService beerOrderService;

    @InjectMocks
    private BeerOrderController beerOrderController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private BeerOrderDto sampleOrderDto;
    private BeerOrderUpsertDto sampleUpsert;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(beerOrderController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();

        sampleOrderDto = new BeerOrderDto(
                1, 0, 10, null, "NEW", "http://callback", List.of(
                new BeerOrderLineDto(100, 0, 5, 2, 0)
        ));

        sampleUpsert = new BeerOrderUpsertDto(
                10,
                "REF-001",
                "http://callback",
                List.of(new BeerOrderLineUpsertDto(5, 2))
        );
    }

    @Test
    void testCreateOrder() throws Exception {
        given(beerOrderService.createOrder(org.mockito.ArgumentMatchers.any(BeerOrderUpsertDto.class))).willReturn(sampleOrderDto);

        mockMvc.perform(post("/api/v1/beer-orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleUpsert)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.customerId", is(10)))
                .andExpect(jsonPath("$.orderStatus", is("NEW")))
                .andExpect(jsonPath("$.orderLines", hasSize(1)));

        verify(beerOrderService).createOrder(org.mockito.ArgumentMatchers.any(BeerOrderUpsertDto.class));
    }

    @Test
    void testGetById_found() throws Exception {
        given(beerOrderService.getById(1)).willReturn(Optional.of(sampleOrderDto));

        mockMvc.perform(get("/api/v1/beer-orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.customerId", is(10)));
    }

    @Test
    void testGetById_notFound() throws Exception {
        given(beerOrderService.getById(1)).willReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/beer-orders/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAll_withoutPagination() throws Exception {
        given(beerOrderService.getAll(isNull(), isNull())).willReturn(List.of(sampleOrderDto));

        mockMvc.perform(get("/api/v1/beer-orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void testGetAll_withPagination() throws Exception {
        given(beerOrderService.getAll(1, 2)).willReturn(List.of(sampleOrderDto));

        mockMvc.perform(get("/api/v1/beer-orders")
                        .param("pageNumber", "1")
                        .param("pageSize", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testGetByCustomer() throws Exception {
        given(beerOrderService.getByCustomer(eq(10), isNull(), isNull())).willReturn(List.of(sampleOrderDto));

        mockMvc.perform(get("/api/v1/beer-orders/customer/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].customerId", is(10)));
    }

    @Test
    void testUpdateStatus_found() throws Exception {
        given(beerOrderService.updateStatus(1, "ALLOCATED")).willReturn(Optional.of(
                new BeerOrderDto(1, 0, 10, null, "ALLOCATED", "http://callback", List.of())
        ));

        mockMvc.perform(put("/api/v1/beer-orders/1/status").param("orderStatus", "ALLOCATED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderStatus", is("ALLOCATED")));

        verify(beerOrderService).updateStatus(1, "ALLOCATED");
    }

    @Test
    void testUpdateStatus_notFound() throws Exception {
        given(beerOrderService.updateStatus(1, "ALLOCATED")).willReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/beer-orders/1/status").param("orderStatus", "ALLOCATED"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDelete_found() throws Exception {
        given(beerOrderService.delete(1)).willReturn(true);

        mockMvc.perform(delete("/api/v1/beer-orders/1"))
                .andExpect(status().isNoContent());

        verify(beerOrderService).delete(1);
    }

    @Test
    void testDelete_notFound() throws Exception {
        given(beerOrderService.delete(1)).willReturn(false);

        mockMvc.perform(delete("/api/v1/beer-orders/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testValidationErrorOnCreate() throws Exception {
        // invalid payload: missing customerId and empty order lines
        BeerOrderUpsertDto invalid = new BeerOrderUpsertDto(
                null, "", null, List.of()
        );

        mockMvc.perform(post("/api/v1/beer-orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Validation Error")))
                .andExpect(jsonPath("$.errors").exists());
    }
}
