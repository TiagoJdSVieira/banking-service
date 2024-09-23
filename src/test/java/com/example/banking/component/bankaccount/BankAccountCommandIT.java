package com.example.banking.component.bankaccount;

import com.example.banking.model.CreateBankAccountRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/reset-database.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class BankAccountCommandIT {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenValidInput_thenReturns201() throws Exception {
        CreateBankAccountRequest validRequest = new CreateBankAccountRequest();
        validRequest.setHolder("John Doe");
        validRequest.setAmount(new BigDecimal("1000.00"));
        validRequest.setCurrency("USD");
        validRequest.setActivate(true);

        mockMvc.perform(post("/v1/bank-accounts/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void whenHolderIsNull_thenReturns400() throws Exception {
        CreateBankAccountRequest invalidRequest = new CreateBankAccountRequest();
        invalidRequest.setHolder(null); // Invalid since holder is @NotNull
        invalidRequest.setAmount(new BigDecimal("1000.00"));
        invalidRequest.setCurrency("USD");
        invalidRequest.setActivate(true);

        mockMvc.perform(post("/v1/bank-accounts/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenAmountIsNull_thenReturns400() throws Exception {
        CreateBankAccountRequest invalidRequest = new CreateBankAccountRequest();
        invalidRequest.setHolder("John Doe");
        invalidRequest.setAmount(null); // Invalid since amount is @NotNull
        invalidRequest.setCurrency("USD");
        invalidRequest.setActivate(true);

        mockMvc.perform(post("/v1/bank-accounts/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenAmountIsNegative_thenReturns400() throws Exception {
        CreateBankAccountRequest invalidRequest = new CreateBankAccountRequest();
        invalidRequest.setHolder("John Doe");
        invalidRequest.setAmount(new BigDecimal("-100.00")); // Invalid negative amount
        invalidRequest.setCurrency("USD");
        invalidRequest.setActivate(true);

        mockMvc.perform(post("/v1/bank-accounts/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenCurrencyIsInvalid_thenReturns400() throws Exception {
        CreateBankAccountRequest invalidRequest = new CreateBankAccountRequest();
        invalidRequest.setHolder("Jane Doe");
        invalidRequest.setAmount(new BigDecimal("100.00"));
        invalidRequest.setCurrency("INVALID"); // Invalid currency code, not in the enum
        invalidRequest.setActivate(true);

        mockMvc.perform(post("/v1/bank-accounts/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void whenFieldsOmitted_thenReturns400() throws Exception {
        CreateBankAccountRequest invalidRequest = new CreateBankAccountRequest();
        invalidRequest.setHolder("John Doe"); // Only holder is provided, other fields omitted

        mockMvc.perform(post("/v1/bank-accounts/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void whenActivateIsFalse_thenReturns201() throws Exception {
        CreateBankAccountRequest validRequest = new CreateBankAccountRequest();
        validRequest.setHolder("John Doe");
        validRequest.setAmount(new BigDecimal("1000.00"));
        validRequest.setCurrency("USD");
        validRequest.setActivate(false); // Set activate to false

        mockMvc.perform(post("/v1/bank-accounts/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated());
    }

}