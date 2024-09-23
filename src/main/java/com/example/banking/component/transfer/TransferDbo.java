package com.example.banking.component.transfer;

import com.example.banking.core.enumeration.CurrencyType;
import com.example.banking.core.infrastructure.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        name = "transfer",
        indexes = {
                @Index(name = "idx_transfer_creditor_iban", columnList = "creditor_iban"),
                @Index(name = "idx_transfer_debtor_iban", columnList = "debtor_iban"),
                @Index(name = "idx_transfer_status", columnList = "status"),
                @Index(name = "idx_transfer_currency", columnList = "currency"),
        }
)
class TransferDbo extends BaseEntity {

    @NotBlank
    @Column(name = "creditor_iban")
    private String creditorIban;

    @NotBlank
    @Column(name = "debtor_iban")
    private String debtorIban;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TransferStatus status;

    @NotNull
    @Column(name = "amount")
    private BigDecimal amount = BigDecimal.ZERO;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private CurrencyType currency;
}
