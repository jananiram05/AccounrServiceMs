package com.tekarch.accountServiceMs.controller;


import com.tekarch.accountServiceMs.dto.AccountWithUserDto;
import com.tekarch.accountServiceMs.dto.TransactionLimitsDTO;
import com.tekarch.accountServiceMs.dto.WithdrawalDepositRequest;
import com.tekarch.accountServiceMs.exceptions.InsufficientFundsException;
import com.tekarch.accountServiceMs.exceptions.ResourceNotFoundException;
import com.tekarch.accountServiceMs.model.Account;
import com.tekarch.accountServiceMs.services.AccountServiceImpl;
import com.tekarch.accountServiceMs.services.interfaces.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/accounts")
public class AccountController {
    private final AccountServiceImpl accountServiceImpl;

    @Autowired
    private AccountService accountService;

    private static final Logger logger = LogManager.getLogger(AccountController.class);


    public AccountController(AccountServiceImpl accountServiceImpl) {
        this.accountServiceImpl = accountServiceImpl;
    }



    // Create Account
    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        Account newAccount = accountService.createAccount(account);
        return new ResponseEntity<>(newAccount, HttpStatus.CREATED);
    }

    // Update Account
    @PutMapping
    public ResponseEntity<Account> updateAccount(@RequestBody Account account) {
        Account updatedAccount = accountService.updateAccount(account);
        return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    // Update Account using userId and accountId
    @PutMapping(params = {"userId", "accountId"})
    public ResponseEntity<Account> updateAccountByQuery(@RequestParam Long userId, @RequestParam Long accountId, @RequestBody Account account) {
        account.setUserId(userId);
        account.setAccountId(accountId);
        Account updatedAccount = accountService.updateAccount(account);
        return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
    }

    // Retrieve all accounts
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
     /*   List<Account> accounts = accountService.getAccountsByUserId(null);
        return new ResponseEntity<>(accounts, HttpStatus.OK);*/
        List<Account> accounts = accountServiceImpl.getAllAccounts();
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    // Retrieve account by userId
    @GetMapping("/{userId}/account")
    public ResponseEntity<List<Account>> getUserAccounts(@PathVariable Long userId) {
        List<Account> accounts = accountService.getAccountsByUserId(userId);
        return new ResponseEntity<>(accounts, HttpStatus.OK);
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

    // Retrieve account by accountId
    @GetMapping("/account/{accountId}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long accountId) {
        Account account = accountService.getAccountById(accountId);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

   /* @GetMapping("/accounts/account/{accountId}")
    public ResponseEntity<Boolean> checkAccountExistence(@PathVariable Long accountId) {
        boolean exists = accountService.existsById(accountId);
        return ResponseEntity.ok(exists);
    }*/


    // Delete account by accountId
    @DeleteMapping("/account/{accountId}")
    public ResponseEntity<Void> deleteAccountById(@PathVariable Long accountId) {
        accountService.deleteAccountById(accountId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Withdraw from sender's account
    @PostMapping("/{accountId}/withdraw")
    public ResponseEntity<String> withdraw(@PathVariable Long accountId, @RequestBody WithdrawalDepositRequest request) {
        try {
            accountService.withdraw(accountId, request.getAmount());
            return ResponseEntity.ok("Withdrawal successful.");
        } catch (InsufficientFundsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient funds for withdrawal.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
    // New endpoint to get transaction limits
    @GetMapping("/{accountId}/limits")
    public ResponseEntity<BigDecimal> getTransactionLimits(@PathVariable Long accountId) {
        BigDecimal limit = accountService.getTransactionLimit(accountId);
        return ResponseEntity.ok(limit);
    }

    // Deposit into receiver's account
    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<String> deposit(@PathVariable Long accountId, @RequestBody WithdrawalDepositRequest request) {
        try {
            accountService.deposit(accountId, request.getAmount());
            return ResponseEntity.ok("Deposit successful.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }

    }

/*
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
    }*/


}
