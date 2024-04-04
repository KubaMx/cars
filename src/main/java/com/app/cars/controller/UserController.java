package com.app.cars.controller;

import com.app.cars.service.UsersService;
import com.app.cars.service.dto.CreateUserDto;
import com.app.cars.service.dto.ResponseDto;
import com.app.cars.service.dto.UserActivationTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UsersService usersService;

    @PostMapping("/register")
    public ResponseDto<Long> registerUser (@RequestBody CreateUserDto createUserDto) {
        return new ResponseDto<>(usersService.registerUser(createUserDto));
    }

    @PostMapping("/activate")
    public ResponseDto<Long> activateUser (@RequestBody UserActivationTokenDto userActivationTokenDto) {
        return new ResponseDto<>(usersService.activateUser(userActivationTokenDto));
    }
}
