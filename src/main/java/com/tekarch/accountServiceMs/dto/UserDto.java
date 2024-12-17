package com.tekarch.accountServiceMs.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserDto {

    private Long userId;
    private String username;
    private String email;
    private String passwordHash;
    private String phoneNumber;
    private boolean twoFactorEnabled = false;
    private String kycStatus = "pending";
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
