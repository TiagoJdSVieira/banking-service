package com.example.banking.component.bankaccount;

import com.example.banking.core.exception.ResourceNotFoundException;
import com.example.banking.core.exception.ServiceException;
import com.example.banking.core.service.BankAccountCommandService;
import com.example.banking.core.service.IbanGenerator;
import com.example.banking.model.CreateBankAccountRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
class BankAccountCommandServiceImpl implements BankAccountCommandService {
    private final BankAccountRepository bankAccountRepository;
    private final BankAccountMapper bankAccountMapper;
    private final IbanGenerator ibanGenerator;

    @Override
    public void createBankAccount(CreateBankAccountRequest createBankAccountRequest) {
        log.info("Creating new bank account...");
        if (createBankAccountRequest == null) throw new IllegalArgumentException("In order to create a new bank account, the request must not be null");

        final BankAccountDbo bankAccountDbo = bankAccountMapper.toBankAccountDbo(createBankAccountRequest);

        final String iban = ibanGenerator.generateIban();
        bankAccountDbo.setIban(iban);

        bankAccountRepository.save(bankAccountDbo);
        log.info("New bank account with iban {} created.", iban);
    }

    @Override
    public void increaseBankAccountBalance(String iban, BigDecimal amount) {
        log.info("Updating iban {} account's balance...", iban);
        final BankAccountDbo bankAccountDbo = bankAccountRepository.findByIban(iban)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Bank account not found for given iban %s", iban)));

        bankAccountDbo.setBalance(bankAccountDbo.getBalance().add(amount));
        bankAccountRepository.save(bankAccountDbo);
        log.info("Iban {} account's balance updated.", iban);
    }

    @Override
    public void decreaseBankAccountBalance(String iban, BigDecimal amount) {
        log.info("Updating iban {} account's balance...", iban);
        final BankAccountDbo bankAccountDbo = bankAccountRepository.findByIban(iban)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Bank account not found for given iban %s", iban)));

        final BigDecimal newBalance = bankAccountDbo.getBalance().subtract(amount);

        if (BigDecimal.ZERO.compareTo(newBalance) > 0) {
            throw new ServiceException("Insufficient funds for account's iban " + iban);
        }

        bankAccountDbo.setBalance(bankAccountDbo.getBalance().subtract(amount));
        bankAccountRepository.save(bankAccountDbo);
        log.info("Iban {} account's balance updated.", iban);
    }

}
