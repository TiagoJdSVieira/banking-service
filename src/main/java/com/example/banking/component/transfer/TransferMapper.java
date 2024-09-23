package com.example.banking.component.transfer;

import com.example.banking.core.enumeration.CurrencyType;
import com.example.banking.model.CreateTransferRequest;
import org.springframework.stereotype.Component;

/**
 * Mapper class
 */
@Component
class TransferMapper {

    TransferDbo toInstantTransferDbo(CreateTransferRequest createTransferRequest) {
        if (createTransferRequest == null) return null;

        final CurrencyType currencyType = CurrencyType.valueOf(createTransferRequest.getCurrency());

        return TransferDbo.builder()
                .creditorIban(createTransferRequest.getCreditor())
                .debtorIban(createTransferRequest.getDebtor())
                .amount(createTransferRequest.getAmount())
                .currency(currencyType)
                .status(TransferStatus.EXECUTED)
                .build();
    }
}
