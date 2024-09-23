package com.example.banking.component.bankaccount;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BankAccountStatus {
    PENDING("PENDING"),
    ACTIVE("ACTIVE"),
    SUSPENDED("SUSPENDED"),
    DELETED("DELETED");

    private final String value;
}
