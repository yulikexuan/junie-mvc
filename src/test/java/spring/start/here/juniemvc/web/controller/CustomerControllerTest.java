package spring.start.here.juniemvc.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import spring.start.here.juniemvc.service.CustomerService;
import spring.start.here.juniemvc.web.exception.CustomerNotFoundException;
import spring.start.here.juniemvc.web.exception.GlobalExceptionHandler;
import spring.start.here.juniemvc.web.model.CustomerDto;
import spring.start.here.juniemvc.web.model.CustomerUpsertDto;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CustomerController.class)
@Import(GlobalExceptionHandler.class)
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CustomerService customerService;

    @Test
    void create_returns201() throws Exception {
        CustomerUpsertDto upsert = new CustomerUpsertDto("John", "john@example.com", "123");
        CustomerDto dto = new CustomerDto(1, 0, "John", "john@example.com", "123");
        given(customerService.create(any(CustomerUpsertDto.class))).willReturn(dto);

        mockMvc.perform(post("/api/v1/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(upsert)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John")));
    }

    @Test
    void create_validationError_returns400_withProblemDetails() throws Exception {
        // invalid: missing name and phone
        String json = "{\"email\":\"not-an-email\"}";
        mockMvc.perform(post("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title", is("Validation Error")))
                .andExpect(jsonPath("$.errors").exists());
    }

    @Test
    void getById_found_returns200() throws Exception {
        CustomerDto dto = new CustomerDto(1, 0, "John", "john@example.com", "123");
        given(customerService.getById(1)).willReturn(Optional.of(dto));

        mockMvc.perform(get("/api/v1/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John")));
    }

    @Test
    void getById_notFound_returns404() throws Exception {
        given(customerService.getById(99)).willReturn(Optional.empty());
        mockMvc.perform(get("/api/v1/customers/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAll_returns200_withList() throws Exception {
        List<CustomerDto> list = List.of(new CustomerDto(1, 0, "John", "john@example.com", "123"));
        given(customerService.getAll()).willReturn(list);

        mockMvc.perform(get("/api/v1/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    void update_found_returns200() throws Exception {
        CustomerUpsertDto upsert = new CustomerUpsertDto("Jane", "jane@example.com", "999");
        CustomerDto dto = new CustomerDto(1, 0, "Jane", "jane@example.com", "999");
        given(customerService.update(eq(1), any(CustomerUpsertDto.class))).willReturn(Optional.of(dto));

        mockMvc.perform(put("/api/v1/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(upsert)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Jane")));
    }

    @Test
    void update_notFound_throwsAndReturns404() throws Exception {
        CustomerUpsertDto upsert = new CustomerUpsertDto("Jane", "jane@example.com", "999");
        // Simulate service throwing our exception, handled by GlobalExceptionHandler
        doThrow(new CustomerNotFoundException(42)).when(customerService).update(eq(42), any(CustomerUpsertDto.class));

        mockMvc.perform(put("/api/v1/customers/42")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(upsert)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title", is("Not Found")))
                .andExpect(jsonPath("$.id", is(42)));
    }

    @Test
    void delete_found_returns204() throws Exception {
        doReturn(true).when(customerService).delete(1);
        mockMvc.perform(delete("/api/v1/customers/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_notFound_returns404() throws Exception {
        doReturn(false).when(customerService).delete(99);
        mockMvc.perform(delete("/api/v1/customers/99"))
                .andExpect(status().isNotFound());
    }
}
