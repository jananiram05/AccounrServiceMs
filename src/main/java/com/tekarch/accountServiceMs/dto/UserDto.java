package com.tekarch.accountServiceMs.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

    private Long userId;
    private String username;
    private String email;
}
