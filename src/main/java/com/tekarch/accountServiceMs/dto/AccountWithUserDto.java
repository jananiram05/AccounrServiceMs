package com.tekarch.accountServiceMs.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter

public class AccountWithUserDto {
    private Long accountId;
    private Long userId;
    private String accountNumber;
    private String accountType;
    private BigDecimal balance;
    private String currency;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String userName;


}

