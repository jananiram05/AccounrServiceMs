package com.tekarch.accountServiceMs.services;

import com.tekarch.accountServiceMs.client.UserServiceClient;
import com.tekarch.accountServiceMs.dto.AccountWithUserDto;
import com.tekarch.accountServiceMs.dto.TransactionLimitsDTO;
import com.tekarch.accountServiceMs.dto.UserDto;
import com.tekarch.accountServiceMs.exceptions.ResourceNotFoundException;
import com.tekarch.accountServiceMs.model.Account;
import com.tekarch.accountServiceMs.repositories.AccountRepository;
import com.tekarch.accountServiceMs.services.interfaces.AccountService;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service


public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final UserServiceClient userServiceClient;
    private final Logger logger = LogManager.getLogger(AccountServiceImpl.class);

    public AccountServiceImpl(AccountRepository accountRepository, UserServiceClient userServiceClient) {
        this.accountRepository = accountRepository;
        this.userServiceClient = userServiceClient;
    }

    // Method to validate if an account belongs to a user
    public boolean validateAccount(Long accountId, Long userId) {
        // Fetch the account from the repository using the accountId
        Account account = accountRepository.findById(accountId).orElse(null);

        if (account == null) {
            // If account is not found, return false
            return false;
        }

        // Validate that the account belongs to the given userId (you can add more logic here)
        return account.getUserId().equals(userId);
    }

    @Override
    public List<AccountWithUserDto> getUserAccounts(Long userId) {
        // Fetch accounts associated with the userId
        List<Account> accounts = accountRepository.findByUserId(userId);

        // Call UserMS to get user details (e.g., username)
        UserDto user = userServiceClient.getUserById(userId);

        // Create a list to hold the response DTOs
        List<AccountWithUserDto> accountWithUserDtos = new ArrayList<>();

        // Add user name to each account and map to the custom DTO
        for (Account account : accounts) {
            AccountWithUserDto accountWithUserDto = new AccountWithUserDto();
            accountWithUserDto.setAccountId(account.getAccountId());
            accountWithUserDto.setUserId(account.getUserId());
            accountWithUserDto.setAccountNumber(account.getAccountNumber());
            accountWithUserDto.setAccountType(account.getAccountType());
            accountWithUserDto.setBalance(account.getBalance());
            accountWithUserDto.setCurrency(account.getCurrency());
            accountWithUserDto.setCreatedAt(account.getCreatedAt());
            accountWithUserDto.setUpdatedAt(account.getUpdatedAt());
            accountWithUserDto.setUserName(user.getUsername());  // Set the username from UserDto

            accountWithUserDtos.add(accountWithUserDto);
        }

        return accountWithUserDtos;
    }

    @Override
    @Transactional
    public Account createAccount(Account account) {

        UserDto user;
        try {
            user = userServiceClient.getUserById(account.getUserId());
            System.out.println("***user found");

        } catch (HttpClientErrorException e) {
            throw new RuntimeException("User not found in User Service or User Service is unavailable", e);
        }


        logger.info("User found: {}", user.getUsername());

        // Save the account
        return accountRepository.save(account);
    }

    public List<Account> getAllUserAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts;
    }


    @Override
    public Account updateAccount(Account account) {
        Optional<Account> optionalAccount = accountRepository.findByAccountId(account.getAccountId());
        if (!optionalAccount.isPresent()) {
            throw new RuntimeException("Account is not present");
        }
        Account existingAccount = optionalAccount.get();
        existingAccount.setAccountType(account.getAccountType());
        existingAccount.setBalance(account.getBalance());
        existingAccount.setCurrency(account.getCurrency());
        return accountRepository.save(existingAccount);
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }



    @Override
    public Account getAccountById(Long accountId) {
        Optional<Account> optionalAccount = accountRepository.findByAccountId(accountId);
        if (!optionalAccount.isPresent()) {
            throw new RuntimeException("Account is not present");
        }
        return optionalAccount.get();
    }

    @Override
    public void deleteAccount(Long accountId) {
        accountRepository.deleteById(accountId);

    }

    public Account getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    // Get the balance of a specific account

    @Override
    public BigDecimal getBalance(Long accountId) {
        Account account = getAccountById(accountId);
        return account.getBalance();
    }

    public TransactionLimitsDTO getTransactionLimits(Long accountId) {
        double dailyLimitRemaining = 5000.0;
        double weeklyLimitRemaining = 20000.0;
        double monthlyLimitRemaining = 50000.0;

        // If the transaction limits are null or invalid, throw an exception
        if (dailyLimitRemaining == 0.0 && weeklyLimitRemaining == 0.0 && monthlyLimitRemaining == 0.0) {
            throw new ResourceNotFoundException("Transaction limits not found or invalid for accountId: " + accountId);
        }

        // Return the populated TransactionLimitsDTO
        return new TransactionLimitsDTO(dailyLimitRemaining, weeklyLimitRemaining, monthlyLimitRemaining);
    }
}





