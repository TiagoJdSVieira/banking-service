package com.example.banking.component.transfer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransferStatus {
    PENDING("PENDING"),
    EXECUTED("EXECUTED"),
    SUSPENDED("SUSPENDED"),
    CANCELED("CANCELED");

    private final String value;
}
