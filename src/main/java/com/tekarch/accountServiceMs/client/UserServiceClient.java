package com.tekarch.accountServiceMs.client;

import com.tekarch.accountServiceMs.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:8081", fallback = UserServiceFallback.class)
public interface UserServiceClient {

    @GetMapping("/users/{userId}")
    UserDto getUserById(@PathVariable("userId") Long userId);

}

