package com.tekarch.accountServiceMs.model;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;  // Primary key for Account entity

    @Column(nullable = false)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String accountNumber;  // Unique Account number

    @Column(nullable = false)
    private String accountType;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;  // Account balance with default value

    @Column(nullable = false)
    private String currency = "USD";  // Currency type with default 'USD'

    private LocalDateTime createdAt = LocalDateTime.now();  // Timestamp for when the account was created

    private LocalDateTime updatedAt = LocalDateTime.now();  // Timestamp for when the account was last updated


}

