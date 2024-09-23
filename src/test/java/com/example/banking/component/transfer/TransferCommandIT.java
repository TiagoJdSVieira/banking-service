package com.example.banking.component.transfer;

import com.example.banking.component.bankaccount.BankAccountDbo;
import com.example.banking.component.bankaccount.BankAccountRepository;
import com.example.banking.component.bankaccount.BankAccountStatus;
import com.example.banking.core.enumeration.CurrencyType;
import com.example.banking.model.CreateTransferRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/reset-database.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class TransferCommandIT {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @BeforeEach
    void setUp() {
        BankAccountDbo account1 = BankAccountDbo.builder()
                .iban("LU1234567890123456789")
                .holder("John Doe")
                .balance(new BigDecimal("1000.00"))
                .status(BankAccountStatus.ACTIVE)
                .currency(CurrencyType.EUR)
                .build();

        BankAccountDbo account2 = BankAccountDbo.builder()
                .iban("LU9876543210987654321")
                .holder("Jane Doe")
                .balance(new BigDecimal("2000.00"))
                .status(BankAccountStatus.ACTIVE)
                .currency(CurrencyType.EUR)
                .build();

        bankAccountRepository.saveAll(List.of(account1, account2));
    }

    @Test
    void createTransferRequest_Returns201() throws Exception {
        CreateTransferRequest validRequest = new CreateTransferRequest("LU1234567890123456789",
                "LU9876543210987654321",
                BigDecimal.valueOf(100.00));

        mockMvc.perform(post("/v1/transfers/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void createTransferRequest_Returns404_WhenAccountNotFound() throws Exception {
        CreateTransferRequest invalidRequest = new CreateTransferRequest("LU0000000000000000000",
                "LU9876543210987654321",
                BigDecimal.valueOf(100.00));

        mockMvc.perform(post("/v1/transfers/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTransferRequest_Returns422_WhenInsufficientFunds() throws Exception {
        CreateTransferRequest insufficientFundsRequest = new CreateTransferRequest("LU1234567890123456789",
                "LU9876543210987654321",
                BigDecimal.valueOf(10000.00));

        mockMvc.perform(post("/v1/transfers/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(insufficientFundsRequest)))
                .andExpect(status().isUnprocessableEntity());
    }
}