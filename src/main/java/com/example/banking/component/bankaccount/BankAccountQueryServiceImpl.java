package com.example.banking.component.bankaccount;

import com.example.banking.core.exception.ResourceNotFoundException;
import com.example.banking.core.service.BankAccountQueryService;
import com.example.banking.model.BankAccountInformation;
import com.example.banking.model.BankAccountInformationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class BankAccountQueryServiceImpl implements BankAccountQueryService {
    private final BankAccountRepository bankAccountRepository;
    private final BankAccountMapper bankAccountMapper;

    @Override
    public BankAccountInformationResponse getBankAccounts(Integer page, Integer size) {
        final Page<BankAccountDbo> bankAccountsPage = bankAccountRepository.findAll(PageRequest.of(page, size));
        return bankAccountMapper.toBankAccountInformationResponse(bankAccountsPage, page, size);
    }

    @Override
    public BankAccountInformation showBankAccountInformation(String iban) {
        final BankAccountDbo bankAccount = bankAccountRepository.findByIban(iban)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Bank account not found for given iban %s", iban)));
        return bankAccountMapper.toBankAccountInformation(bankAccount);
    }
}
