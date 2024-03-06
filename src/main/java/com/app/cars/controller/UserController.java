package com.app.cars.controller;

import com.app.cars.service.UsersService;
import com.app.cars.service.dto.CreateUserDto;
import com.app.cars.service.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UsersService usersService;

    @PostMapping
    public ResponseDto<Long> registerUser (@RequestBody CreateUserDto createUserDto) {
        return new ResponseDto<>(usersService.registerUser(createUserDto));
    }
}
