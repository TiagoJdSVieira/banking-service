package com.example.banking.core.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CurrencyType {
    USD("USD"),
    EUR("EUR"),
    CHF("CHF"),
    GBP("GBP");

    private final String value;
}
