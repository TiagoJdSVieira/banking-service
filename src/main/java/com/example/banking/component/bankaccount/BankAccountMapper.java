package com.example.banking.component.bankaccount;

import com.example.banking.config.ApplicationProperties;
import com.example.banking.core.enumeration.CurrencyType;
import com.example.banking.core.mapper.PageInfoMapper;
import com.example.banking.model.BankAccountInformation;
import com.example.banking.model.BankAccountInformationResponse;
import com.example.banking.model.CreateBankAccountRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Mapper class
 */
@Component
@RequiredArgsConstructor
class BankAccountMapper {

    private final ApplicationProperties applicationProperties;

    BankAccountInformationResponse toBankAccountInformationResponse(Page<BankAccountDbo> page, Integer pageNumber, Integer size) {
        if (page == null) {
            return null;
        }

        final List<BankAccountInformation> bankAccounts = toBankAccountsInformation(page.getContent());

        return BankAccountInformationResponse.builder()
                .accounts(bankAccounts)
                .pageInfo(PageInfoMapper.toPageInfo(page.getTotalPages(), page.getTotalElements(), pageNumber, size))
                .build();
    }

    private List<BankAccountInformation> toBankAccountsInformation(List<BankAccountDbo> bankAccounts) {
        if (CollectionUtils.isEmpty(bankAccounts)) return Collections.emptyList();

        return bankAccounts.stream().map(this::toBankAccountInformation).toList();
    }

    BankAccountInformation toBankAccountInformation(BankAccountDbo bankAccount) {
        if (bankAccount == null) return null;
        return BankAccountInformation.builder()
                .status(bankAccount.getStatus().getValue())
                .balance(bankAccount.getBalance())
                .currency(bankAccount.getCurrency().getValue())
                .holder(bankAccount.getHolder())
                .iban(bankAccount.getIban())
                .build();
    }

    BankAccountDbo toBankAccountDbo(CreateBankAccountRequest createBankAccountRequest) {
        if (createBankAccountRequest == null) return null;

        final BankAccountStatus status = Optional.ofNullable(createBankAccountRequest.getActivate())
                .map(this::defineBankAccountStatusOnCreation)
                .orElse(BankAccountStatus.PENDING);

        final CurrencyType currency = Optional.ofNullable(createBankAccountRequest.getCurrency())
                .map(CurrencyType::valueOf)
                .orElse(applicationProperties.currency());

        return BankAccountDbo.builder()
                .balance(createBankAccountRequest.getAmount())
                .currency(currency)
                .holder(createBankAccountRequest.getHolder())
                .status(status)
                .build();
    }

    private BankAccountStatus defineBankAccountStatusOnCreation(Boolean activate) {
        if (Boolean.TRUE.equals(activate)) return BankAccountStatus.ACTIVE;
        return BankAccountStatus.PENDING;
    }
}
