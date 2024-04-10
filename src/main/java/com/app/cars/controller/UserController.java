package com.app.cars.controller;

import com.app.cars.service.UsersService;
import com.app.cars.service.dto.CreateUserDto;
import com.app.cars.service.dto.ResponseDto;
import com.app.cars.service.dto.UserActivationTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResponseDto<Long>> activateUser (@RequestBody UserActivationTokenDto userActivationTokenDto) {
        var id = usersService.activateUser(userActivationTokenDto);
        return id == null ?
                new ResponseEntity<>(new ResponseDto<>(null), HttpStatus.INTERNAL_SERVER_ERROR) :
                new ResponseEntity<>(new ResponseDto<>(id), HttpStatus.OK);
    }
}
