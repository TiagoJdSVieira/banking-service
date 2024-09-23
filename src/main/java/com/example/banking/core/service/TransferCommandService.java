package com.example.banking.core.service;

import com.example.banking.model.CreateTransferRequest;

/**
 * Bank account command service
 */
public interface TransferCommandService {

    /**
     * Creates a new instant transfer based on given request <br>
     * Transfer is executed right away
     *
     * @param createTransferRequest bank account creation request
     */
    void createInstantTransferRequest(CreateTransferRequest createTransferRequest);
}
