package com.tekarch.accountServiceMs.repositories;


import com.tekarch.accountServiceMs.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {


    List<Account> findByUserId(Long userId);
    List<Account> findByUserIdAndAccountType(Long userId, String accountType);
    Account findByAccountNumber(String accountNumber);
   // List<Account> findAll();
}

