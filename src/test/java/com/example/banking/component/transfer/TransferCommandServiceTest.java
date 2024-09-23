package com.example.banking.component.transfer;

import com.example.banking.core.exception.ServiceException;
import com.example.banking.core.service.BankAccountCommandService;
import com.example.banking.core.service.BankAccountQueryService;
import com.example.banking.model.BankAccountInformation;
import com.example.banking.model.CreateTransferRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TransferCommandServiceTest {

    @InjectMocks
    private TransferCommandServiceImpl transferCommandService;

    @Mock
    private TransferMapper transferMapper;

    @Mock
    private BankAccountQueryService bankAccountQueryService;

    @Mock
    private BankAccountCommandService bankAccountCommandService;

    @Mock
    private TransferRepository transferRepository;

    @Mock
    private ExchangeRateService exchangeRateService;

    private BankAccountInformation debtorAccount;
    private BankAccountInformation creditorAccount;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        debtorAccount = BankAccountInformation.builder()
                .iban("LU1234567890123456789")
                .holder("John Doe")
                .balance(BigDecimal.valueOf(1000))
                .status("ACTIVE")
                .currency("EUR")
                .build();
        creditorAccount = BankAccountInformation.builder()
                .iban("LU9876543210987654321")
                .holder("Jane Doe")
                .balance(BigDecimal.valueOf(2000))
                .status("ACTIVE")
                .currency("EUR")
                .build();
    }

    @Test
    void createInstantTransferRequest_Success() {
        BigDecimal amount = BigDecimal.valueOf(100.00).setScale(2, RoundingMode.HALF_UP);
        CreateTransferRequest request = new CreateTransferRequest(debtorAccount.getIban(), creditorAccount.getIban(), amount);

        when(bankAccountQueryService.showBankAccountInformation(debtorAccount.getIban())).thenReturn(debtorAccount);
        when(bankAccountQueryService.showBankAccountInformation(creditorAccount.getIban())).thenReturn(creditorAccount);
        when(exchangeRateService.getExchangeRate("EUR", "EUR")).thenReturn(1.0);
        when(transferMapper.toInstantTransferDbo(request)).thenReturn(new TransferDbo());

        transferCommandService.createInstantTransferRequest(request);

        verify(bankAccountCommandService).decreaseBankAccountBalance(debtorAccount.getIban(), amount);
        verify(bankAccountCommandService).increaseBankAccountBalance(creditorAccount.getIban(), amount);
        verify(transferRepository).save(any());
    }

    @Test
    void createInstantTransferRequest_NullRequest_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> transferCommandService.createInstantTransferRequest(null));
    }

    @Test
    void createInstantTransferRequest_NegativeAmount_ThrowsException() {
        CreateTransferRequest request = new CreateTransferRequest(debtorAccount.getIban(), creditorAccount.getIban(), BigDecimal.valueOf(-100.00));
        assertThrows(IllegalArgumentException.class, () -> transferCommandService.createInstantTransferRequest(request));
    }

    @Test
    void createInstantTransferRequest_AccountNotActive_ThrowsServiceException() {
        debtorAccount.setStatus("INACTIVE");
        CreateTransferRequest request = new CreateTransferRequest(debtorAccount.getIban(), creditorAccount.getIban(), BigDecimal.valueOf(100.00));

        when(bankAccountQueryService.showBankAccountInformation(debtorAccount.getIban())).thenReturn(debtorAccount);

        assertThrows(ServiceException.class, () -> transferCommandService.createInstantTransferRequest(request));
    }
}