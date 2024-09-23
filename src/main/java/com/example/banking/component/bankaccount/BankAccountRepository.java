package com.example.banking.component.bankaccount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccountDbo, Long> {
    Optional<BankAccountDbo> findByIban(String iban);

}
