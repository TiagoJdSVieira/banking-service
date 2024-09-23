package com.example.banking.component.iban;

import com.example.banking.config.ApplicationProperties;
import com.example.banking.core.exception.ServiceException;
import com.example.banking.core.service.IbanGenerator;
import lombok.RequiredArgsConstructor;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.iban4j.IbanFormatException;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@RequiredArgsConstructor
class DefaultIbanGenerator implements IbanGenerator {
    private final ApplicationProperties applicationProperties;

    @Override
    public String generateIban() {
        return generateIban(applicationProperties.iban().countryCode(), applicationProperties.iban().bankCode());
    }

    @Override
    public String generateIban(String countryCode, String bankCode) {
        try {
            return new Iban.Builder()
                    .countryCode(CountryCode.getByCode(countryCode))
                    .bankCode(bankCode)
                    .accountNumber(generateRandomAccountNumber())
                    .build()
                    .toString();
        } catch (IbanFormatException | IllegalArgumentException e) {
            throw new ServiceException("Error generating IBAN: " + e.getMessage());
        }
    }

    private String generateRandomAccountNumber() {
        final Random random = new Random();
        return IntStream.range(0, applicationProperties.iban().accountNumberLength())
                .map(i -> random.nextInt(10))
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
    }
}
