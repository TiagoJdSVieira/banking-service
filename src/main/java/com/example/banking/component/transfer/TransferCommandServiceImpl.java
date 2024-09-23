package com.example.banking.component.transfer;

import com.example.banking.component.bankaccount.BankAccountStatus;
import com.example.banking.core.exception.ServiceException;
import com.example.banking.core.service.BankAccountCommandService;
import com.example.banking.core.service.BankAccountQueryService;
import com.example.banking.core.service.TransferCommandService;
import com.example.banking.model.BankAccountInformation;
import com.example.banking.model.CreateTransferRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
class TransferCommandServiceImpl implements TransferCommandService {
    private final TransferMapper transferMapper;
    private final BankAccountQueryService bankAccountQueryService;
    private final BankAccountCommandService bankAccountCommandService;
    private final TransferRepository transferRepository;
    private final ExchangeRateService exchangeRateService;

    @Override
    public void createInstantTransferRequest(CreateTransferRequest createTransferRequest) {
        log.info("Creating new transfer...");
        if (createTransferRequest == null) throw new IllegalArgumentException("In order to create a transfer, the request must not be null");
        if (BigDecimal.ZERO.compareTo(createTransferRequest.getAmount()) >= 0) throw new IllegalArgumentException("In order to create a transfer, the requested amount shall be greater than 0");

        performTransferOperations(createTransferRequest);

        final TransferDbo transferDbo = transferMapper.toInstantTransferDbo(createTransferRequest);
        transferRepository.save(transferDbo);

        log.info("New transfer with creditor iban {} and debtor iban {} created.", createTransferRequest.getCreditor(), createTransferRequest.getDebtor());
    }

    private void performTransferOperations(CreateTransferRequest request) {
        performDebtorOperations(request);
        performCreditorOperations(request);
    }

    private void performCreditorOperations(CreateTransferRequest request) {
        final BankAccountInformation creditor = availableAccount(request.getCreditor());
        final double exchangeRate = exchangeRateService.getExchangeRate(request.getCurrency(), creditor.getCurrency());
        final BigDecimal amountToAdd = request.getAmount().multiply(BigDecimal.valueOf(exchangeRate)).setScale(2, RoundingMode.HALF_UP);
        bankAccountCommandService.increaseBankAccountBalance(creditor.getIban(), amountToAdd);
    }

    private void performDebtorOperations(CreateTransferRequest request) {
        final BankAccountInformation debtor = availableAccount(request.getDebtor());
        final double exchangeRate = exchangeRateService.getExchangeRate(request.getCurrency(), debtor.getCurrency());
        final BigDecimal amountToRemove = request.getAmount().multiply(BigDecimal.valueOf(exchangeRate)).setScale(2, RoundingMode.HALF_UP);
        bankAccountCommandService.decreaseBankAccountBalance(debtor.getIban(), amountToRemove);
    }

    private BankAccountInformation availableAccount(String iban) {
        final BankAccountInformation account = bankAccountQueryService.showBankAccountInformation(iban);

        if (!BankAccountStatus.ACTIVE.getValue().equals(account.getStatus())) {
            throw new ServiceException(String.format("Account with iban %s is not active, can't create a transfer", iban));
        }

        return account;
    }
}
