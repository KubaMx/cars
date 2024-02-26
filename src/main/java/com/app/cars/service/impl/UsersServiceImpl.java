package com.app.cars.service.impl;

import com.app.cars.persistence.repository.UserEntityRepository;
import com.app.cars.service.UsersService;
import com.app.cars.service.dto.CreateUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {
    private final PasswordEncoder passwordEncoder;
    private final UserEntityRepository userEntityRepository;

    @Override
    public Long registerUser(CreateUserDto createUserDto) {
        // TODO do zaimplementowania warstwa walidacji
        // na razie zostawiam trzy ważne warunki poniżej

        if (!createUserDto.password().equals(createUserDto.passwordConfirmation())) {
            throw new IllegalArgumentException("Passwords are not correct");
        }

        if (userEntityRepository.findByUsername(createUserDto.username()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        if(userEntityRepository.findByEmail(createUserDto.email()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        var userToInsert = createUserDto
                .toUserEntity()
                .withPassword(passwordEncoder.encode(createUserDto.password()));

        var insertedUser = userEntityRepository.save(userToInsert);

        return insertedUser.getId();
    }
}
