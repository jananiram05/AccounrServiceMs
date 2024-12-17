package com.tekarch.accountServiceMs.services.interfaces;


import com.tekarch.accountServiceMs.dto.UserDto;

public interface UserService {
    UserDto getUserDetails(Long userId);
}
