package com.example.banking.component.bankaccount.delegate;

import com.example.banking.api.BankAccountQueryApiDelegate;
import com.example.banking.core.service.BankAccountQueryService;
import com.example.banking.model.BankAccountInformation;
import com.example.banking.model.BankAccountInformationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Delegate from the api that connects controller to component's services (Command & Query)
 */
@Component
@RequiredArgsConstructor
class BankAccountApiQueryDelegate implements BankAccountQueryApiDelegate {

    private final BankAccountQueryService bankAccountQueryService;

    @Override
    public ResponseEntity<BankAccountInformationResponse> getBankAccounts(Integer page, Integer size) {
        return ResponseEntity.ok(bankAccountQueryService.getBankAccounts(page, size));
    }

    @Override
    public ResponseEntity<BankAccountInformation> showBankAccountInformation(String iban) {
        return ResponseEntity.ok(bankAccountQueryService.showBankAccountInformation(iban));
    }
}
