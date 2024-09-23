package com.example.banking.core.service;

import com.example.banking.model.BankAccountInformation;
import com.example.banking.model.BankAccountInformationResponse;

/**
 * Bank account query service
 */
public interface BankAccountQueryService {

    /**
     * Get all the bank accounts' information
     *
     * @param page number of the page
     * @param size number of items
     * @return found bank accounts' information
     */
    BankAccountInformationResponse getBankAccounts(Integer page, Integer size);

    /**
     * Get a bank account information based on the given iban
     *
     * @param iban bank account's iban
     * @return bank account information
     */
    BankAccountInformation showBankAccountInformation(String iban);

}
