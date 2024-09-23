package com.example.banking.component.bankaccount;

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
        name = "bank_account",
        indexes = {
                @Index(name = "uc_bank_account_iban", columnList = "iban", unique = true),
                @Index(name = "idx_bank_account_holder", columnList = "holder"),
                @Index(name = "idx_bank_account_status", columnList = "status"),
                @Index(name = "idx_bank_account_currency", columnList = "currency"),
        }
)
public class BankAccountDbo extends BaseEntity {

    @NotBlank
    @Column(name = "iban")
    private String iban;

    @NotBlank
    @Column(name = "holder")
    private String holder;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BankAccountStatus status;

    @NotNull
    @Column(name = "balance")
    private BigDecimal balance = BigDecimal.ZERO;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private CurrencyType currency;
}
