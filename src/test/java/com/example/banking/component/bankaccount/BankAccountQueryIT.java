package com.example.banking.component.bankaccount;

import com.example.banking.component.bankaccount.BankAccountDbo;
import com.example.banking.component.bankaccount.BankAccountRepository;
import com.example.banking.component.bankaccount.BankAccountStatus;
import com.example.banking.core.enumeration.CurrencyType;
import com.example.banking.model.BankAccountInformation;
import com.example.banking.model.BankAccountInformationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/reset-database.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class BankAccountQueryIT {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    private BankAccountDbo account1;
    private BankAccountDbo account2;

    @BeforeEach
    void setUp() {
        account1 = BankAccountDbo.builder()
                .iban("LU1234567890123456789")
                .holder("John Doe")
                .balance(new BigDecimal("1000.00"))
                .status(BankAccountStatus.ACTIVE)
                .currency(CurrencyType.EUR)
                .build();

        account2 = BankAccountDbo.builder()
                .iban("LU9876543210987654321")
                .holder("Jane Doe")
                .balance(new BigDecimal("2000.00"))
                .status(BankAccountStatus.ACTIVE)
                .currency(CurrencyType.EUR)
                .build();

        bankAccountRepository.saveAll(List.of(account1, account2));
    }

    @Test
    void getBankAccounts_Returns200() throws Exception {
        MvcResult result = mockMvc.perform(get("/v1/bank-accounts/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        BankAccountInformationResponse response = objectMapper.readValue(jsonResponse, BankAccountInformationResponse.class);

        assertNotNull(response);
        assertEquals(2, response.getAccounts().size());

        BankAccountInformation accountInformation1 = response.getAccounts().get(0);
        assertEquals(account1.getIban(), accountInformation1.getIban());
        assertEquals(account1.getHolder(), accountInformation1.getHolder());

        BankAccountInformation accountInformation2 = response.getAccounts().get(1);
        assertEquals(account2.getIban(), accountInformation2.getIban());
        assertEquals(account2.getHolder(), accountInformation2.getHolder());
    }

    @Test
    void showBankAccountInformation_Returns200() throws Exception {
        MvcResult result = mockMvc.perform(get("/v1/bank-accounts/" + account1.getIban())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        BankAccountInformation accountInfo = objectMapper.readValue(jsonResponse, BankAccountInformation.class);
        assertEquals(account1.getIban(), accountInfo.getIban());
        assertEquals(account1.getHolder(), accountInfo.getHolder());
    }

    @Test
    void showBankAccountInformation_Returns404_WhenAccountNotFound() throws Exception {
        mockMvc.perform(get("/v1/bank-accounts/LU9999999999999999999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}