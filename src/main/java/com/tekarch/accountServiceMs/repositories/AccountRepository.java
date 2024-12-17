package com.tekarch.accountServiceMs.repositories;


import com.tekarch.accountServiceMs.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    // Method to find all accounts by user ID
    List<Account> findByUserId(Long userId);

    // Method to find an account by account ID
    Optional<Account> findByAccountId(Long accountId);

    // Method to find all accounts by user ID and account type
    List<Account> findByUserIdAndAccountType(Long userId, String accountType);

    Account findByAccountNumber(String accountNumber);
    List<Account> findAll();

}

