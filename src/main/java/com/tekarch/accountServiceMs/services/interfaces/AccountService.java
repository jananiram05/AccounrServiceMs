package com.tekarch.accountServiceMs.services.interfaces;

import com.tekarch.accountServiceMs.dto.AccountWithUserDto;
import com.tekarch.accountServiceMs.dto.TransactionLimitsDTO;
import com.tekarch.accountServiceMs.dto.UserDto;
import com.tekarch.accountServiceMs.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

   /* // Methods for managing accounts
    Account createAccount(Account account);
    Account updateAccount(Account account);
    List<Account> getAllAccounts();
    //List<Account> getUserAccounts(Long userId);
    Account getAccountById(Long accountId);
    void deleteAccount(Long accountId);
    BigDecimal getBalance(Long accountId);
   // List<Account> getAccountsByType(Long userId, String accountType);
   List<AccountWithUserDto> getUserAccounts(Long userId);
    boolean validateAccount(Long accountId, Long userId);

    TransactionLimitsDTO getTransactionLimits(Long accountId);*/

    Account createAccount(Account account);
    Account updateAccount(Account account);
    Account getAccountById(Long accountId);
    List<Account> getAccountsByUserId(Long userId);
    void deleteAccountById(Long accountId);
    List<Account> getAllAccounts();


    void withdraw(Long accountId, BigDecimal amount);

    // Method to deposit funds into an account
    void deposit(Long accountId, BigDecimal amount);

    BigDecimal getTransactionLimit(Long accountId);


}
