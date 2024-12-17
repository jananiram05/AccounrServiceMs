package com.tekarch.accountServiceMs.controller;


import com.tekarch.accountServiceMs.dto.AccountWithUserDto;
import com.tekarch.accountServiceMs.dto.TransactionLimitsDTO;
import com.tekarch.accountServiceMs.model.Account;
import com.tekarch.accountServiceMs.services.AccountServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/accounts")
public class AccountController {
    private final AccountServiceImpl accountServiceImpl;


    public AccountController(AccountServiceImpl accountServiceImpl) {
        this.accountServiceImpl = accountServiceImpl;
    }


    @GetMapping("/{accountId}/validate")
    public ResponseEntity<Boolean> validateAccount(@PathVariable Long accountId, @RequestParam Long userId) {
        logger.info("Request received to validate accountId={} for userId={}", accountId, userId);

        boolean isValid = accountServiceImpl.validateAccount(accountId, userId);

        if (isValid) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            logger.warn("Account validation failed for accountId={} and userId={}", accountId, userId);
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        logger.info("Request received to create an account: {}", account);
        return new ResponseEntity<>(accountServiceImpl.createAccount(account), HttpStatus.CREATED);

    }

    @GetMapping("/all")
    public ResponseEntity<List<Account>> getAllUserAccounts() {
        logger.info("Request received to fetch all user accounts");
        List<Account> accounts = accountServiceImpl.getAllUserAccounts();
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AccountWithUserDto>> getAccountsByUserId(@PathVariable Long userId) {
        logger.info("Request received to fetch accounts for userId={}", userId);
        List<AccountWithUserDto> accountsWithUserDto = accountServiceImpl.getUserAccounts(userId);  // Fetch accounts for the user

        if (accountsWithUserDto.isEmpty()) {
            logger.warn("No accounts found for userId={}", userId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(accountsWithUserDto, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Account> updateAccount(@RequestBody Account account) {
        logger.info("Request received to update an account: {}", account);
        return new ResponseEntity<>(accountServiceImpl.updateAccount(account), HttpStatus.OK);
    }

    @PutMapping("/account")
    public ResponseEntity<Account> updateAccountByQuery(@RequestParam Long userId,
                                                        @RequestParam Long accountId,
                                                        @RequestBody Account account) {
        logger.info("Request received to update account with userId={} and accountId={}: {}", userId, accountId, account);
        return new ResponseEntity<>(accountServiceImpl.updateAccount(account), HttpStatus.OK);
    }


    @GetMapping("/{userId}/account")
    public ResponseEntity<AccountWithUserDto> getUserAccountById(@PathVariable Long userId) {
        logger.info("Request received to fetch account for userId={}", userId);
        List<AccountWithUserDto> accounts = accountServiceImpl.getUserAccounts(userId);  // Fetch accounts for the user
        if (!accounts.isEmpty()) {
            return new ResponseEntity<>(accounts.get(0), HttpStatus.OK);  // Return the first account
        } else {
            logger.warn("Account not found for userId={}", userId);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
    }

    @GetMapping("/accounts/{accountNumber}")
    public ResponseEntity<Account> getAccountByAccountNumber(@PathVariable String accountNumber) {
        logger.info("Request received to fetch account for account_number={}", accountNumber);
        Account account = accountServiceImpl.getAccountByAccountNumber(accountNumber);  // Fetch account by account number
        if (account != null) {
            return new ResponseEntity<>(account, HttpStatus.OK);  // Return the account if found
        } else {
            logger.warn("Account not found for account_number={}", accountNumber);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);  // Return 404 if not found
        }
    }

    private static final Logger logger = LogManager.getLogger(AccountController.class);

    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long accountId) {
        logger.info("Request received to delete account with accountId={}", accountId);
        accountServiceImpl.deleteAccount(accountId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    // 2. Get Transaction Limits for Account
    @GetMapping("/{accountId}/transaction-limits")
    public ResponseEntity<TransactionLimitsDTO> getTransactionLimits(@PathVariable Long accountId) {
        logger.info("Request received to fetch transaction limits for accountId={}", accountId);
        TransactionLimitsDTO limits = accountServiceImpl.getTransactionLimits(accountId);  // Fetch transaction limits
        return new ResponseEntity<>(limits, HttpStatus.OK);
    }


}
