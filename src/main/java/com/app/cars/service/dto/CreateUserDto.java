package com.app.cars.service.dto;

import com.app.cars.persistence.model.UserEntity;
import com.app.cars.persistence.model.type.Role;

public record CreateUserDto(
        String username,
        String email,
        Integer age,
        String password,
        String passwordConfirmation,
        Role role
) {
    public UserEntity toUserEntity() {
        return UserEntity
                .builder()
                .username(username)
                .email(email)
                .age(age)
                .password(password)
                .enabled(false)
                .role(role)
                .build();
    }
}
