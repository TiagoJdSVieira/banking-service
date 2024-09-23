package com.example.banking.core.service;

import com.example.banking.model.CreateBankAccountRequest;

import java.math.BigDecimal;

/**
 * Bank account command service
 */
public interface BankAccountCommandService {

    /**
     * Creates a new bank account based on given request
     *
     * @param createBankAccountRequest bank account creation request
     */
    void createBankAccount(CreateBankAccountRequest createBankAccountRequest);

    /**
     * Add given amount to given account
     *
     * @param iban account to perform action
     * @param amount amount to add
     */
    void increaseBankAccountBalance(String iban, BigDecimal amount);

    /**
     * Remove given amount from given account <br>
     * Not allowing account's balance to be negative
     *
     * @param iban account to perform action
     * @param amount amount to remove
     */
    void decreaseBankAccountBalance(String iban, BigDecimal amount);
}
