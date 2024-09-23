package com.example.banking.config;

import com.example.banking.core.enumeration.CurrencyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record ApplicationProperties (
        @NotBlank CurrencyType currency,
        @NotNull Iban iban,
        @NotBlank String exchangeRateUrl
) {
    public record Iban (
        @NotBlank String countryCode,
        @NotBlank String bankCode,
        @NotNull int accountNumberLength
    ) {}
};




