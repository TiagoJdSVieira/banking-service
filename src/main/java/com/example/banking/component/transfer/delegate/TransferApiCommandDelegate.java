package com.example.banking.component.transfer.delegate;

import com.example.banking.api.TransferCommandApiDelegate;
import com.example.banking.core.service.TransferCommandService;
import com.example.banking.model.CreateTransferRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Delegate from the api that connects controller to component's services (Command & Query)
 */
@Component
@RequiredArgsConstructor
class TransferApiCommandDelegate implements TransferCommandApiDelegate {
    private final TransferCommandService transferCommandService;

    @Override
    public ResponseEntity<Void> createTransferRequest(CreateTransferRequest createTransferRequest) {
        transferCommandService.createInstantTransferRequest(createTransferRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
