package com.tekarch.accountServiceMs.client;

import com.tekarch.accountServiceMs.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserServiceFallback implements UserServiceClient {

    @Override
    public UserDto getUserById(Long userId) {
        // Return a default UserDto or throw an exception as needed
        // You can also return a custom message if User Service is unavailable
        UserDto fallbackUser = new UserDto();
        fallbackUser.setUserId(userId);
        fallbackUser.setUsername("Janani ");
        fallbackUser.setEmail("Janani@gmail.com");

        return fallbackUser;
    }
}
