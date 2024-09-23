package com.example.banking.component.bankaccount.delegate;

import com.example.banking.api.BankAccountCommandApiDelegate;
import com.example.banking.core.service.BankAccountCommandService;
import com.example.banking.model.CreateBankAccountRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Delegate from the api that connects controller to component's services (Command & Query)
 */
@Component
@RequiredArgsConstructor
class BankAccountApiCommandDelegate implements BankAccountCommandApiDelegate {
    private final BankAccountCommandService bankAccountCommandService;

    @Override
    public ResponseEntity<Void> createBankAccount(CreateBankAccountRequest createBankAccountRequest) {
        bankAccountCommandService.createBankAccount(createBankAccountRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
