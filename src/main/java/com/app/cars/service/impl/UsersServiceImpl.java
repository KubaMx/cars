package com.app.cars.service.impl;

import com.app.cars.persistence.repository.UserEntityRepository;
import com.app.cars.persistence.repository.VerificationTokenEntityRepository;
import com.app.cars.service.UsersService;
import com.app.cars.service.dto.CreateUserDto;
import com.app.cars.service.dto.UserActivationDto;
import com.app.cars.service.dto.UserActivationTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {
    private final PasswordEncoder passwordEncoder;
    private final UserEntityRepository userEntityRepository;
    private final VerificationTokenEntityRepository verificationTokenEntityRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

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

        if (userEntityRepository.findByEmail(createUserDto.email()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        var userToInsert = createUserDto
                .toUserEntity()
                .withPassword(passwordEncoder.encode(createUserDto.password()));

        var insertedUser = userEntityRepository.save(userToInsert);

        var id = insertedUser.getId();
        applicationEventPublisher.publishEvent(new UserActivationDto(id));
        return id;
    }

    @Override
    public Long activateUser(UserActivationTokenDto userActivationTokenDto) {
        var token = userActivationTokenDto.token();
        var verificationToken = verificationTokenEntityRepository
                .findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("User not found for this token"));

        var userToActivate = verificationToken.validate().orElse(null);

        verificationTokenEntityRepository.delete(verificationToken);

        if (userToActivate != null) {
            var activatedUser = userToActivate.activate();
            userEntityRepository.save(activatedUser);
            return activatedUser.getId();
        }
        return null;
    }
}
