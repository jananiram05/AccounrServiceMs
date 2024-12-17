package com.tekarch.accountServiceMs.services;

import com.tekarch.accountServiceMs.client.UserServiceClient;
import com.tekarch.accountServiceMs.dto.UserDto;
import com.tekarch.accountServiceMs.services.interfaces.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserServiceClient userServiceClient;

    public UserServiceImpl(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }


    @Override
    public UserDto getUserDetails(Long userId) {
        return userServiceClient.getUserById(userId);

    }

}
