package com.app.cars.service;

import com.app.cars.service.dto.CreateUserDto;
import com.app.cars.service.dto.UserActivationTokenDto;

public interface UsersService {
    Long registerUser(CreateUserDto createUserDto);

    Long activateUser(UserActivationTokenDto userActivationTokenDto);
}
