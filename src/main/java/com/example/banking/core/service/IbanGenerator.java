package com.example.banking.core.service;

/**
 * Iban generator service
 */
public interface IbanGenerator {

    /**
     * Generate a new iban based on default values
     *
     * @return generated iban
     */
    String generateIban();

    /**
     * Generate a new iban based on given parameters
     *
     * @param countryCode code of the country
     * @param bankCode code of the bank
     * @return generated iban
     */
    String generateIban(String countryCode, String bankCode);
}
