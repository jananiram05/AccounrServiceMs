package com.tekarch.accountServiceMs.services;

import com.tekarch.accountServiceMs.dto.UserDto;
import com.tekarch.accountServiceMs.exceptions.InsufficientFundsException;
import com.tekarch.accountServiceMs.exceptions.ResourceNotFoundException;
import com.tekarch.accountServiceMs.exceptions.UserNotFoundException;
import com.tekarch.accountServiceMs.model.Account;
import com.tekarch.accountServiceMs.repositories.AccountRepository;
import com.tekarch.accountServiceMs.services.interfaces.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service


public class AccountServiceImpl implements AccountService {

   @Autowired
   private RestTemplate restTemplate;
    private final String USER_MS_URL = "http://localhost:8081/users"; // Assuming your User MS is on port 8081

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account createAccount(Account account) {
        ResponseEntity<UserDto> userResponse = restTemplate.exchange(
                USER_MS_URL + "/" + account.getUserId(),
                HttpMethod.GET,
                null,
                UserDto.class
        );
        if (userResponse.getStatusCode() == HttpStatus.OK) {
            // User exists, proceed with account creation
            return accountRepository.save(account);
        } else {
            throw new UserNotFoundException("User with ID " + account.getUserId() + " not found.");
        }

    }
    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account updateAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account getAccountById(Long accountId) {
        Optional<Account> account = accountRepository.findById(accountId);
        return account.orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @Override
    public List<Account> getAccountsByUserId(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    public Account getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    @Override
    public void deleteAccountById(Long accountId) {
        accountRepository.deleteById(accountId);
    }

    @Override
    public void withdraw(Long accountId, BigDecimal amount) {
        // Find the account by ID
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found for ID " + accountId));

        // Check if the account has sufficient balance
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Not enough funds in the account for withdrawal.");
        }

        // Deduct the amount from the account balance
        account.setBalance(account.getBalance().subtract(amount));

        // Save the updated account
        accountRepository.save(account);
    }

    // Method to deposit funds into an account
    @Override
    public void deposit(Long accountId, BigDecimal amount) {
        // Find the account by ID
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found for ID " + accountId));

        // Add the amount to the account balance
        account.setBalance(account.getBalance().add(amount));

        // Save the updated account
        accountRepository.save(account);
    }

    public BigDecimal getTransactionLimit(Long accountId) {
        // For demo purposes, return a static limit
        // You can implement logic to fetch this from a database
        return BigDecimal.valueOf(10000); // Example: limit is 10,000
    }

}





