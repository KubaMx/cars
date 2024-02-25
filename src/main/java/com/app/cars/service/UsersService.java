package com.app.cars.service;

import com.app.cars.service.dto.CreateUserDto;

public interface UsersService {
    Long registerUser(CreateUserDto createUserDto);
}
