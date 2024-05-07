package com.app.cars.persistence.model;

import com.app.cars.persistence.model.type.Role;
import com.app.cars.security.dto.UserDetailsDto;
import com.app.cars.service.dto.UserEmailDto;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {
    private String username;
    private String email;
    private Integer age;
    @Getter
    private String password;
    private boolean enabled;
    @Enumerated(EnumType.STRING)
    private Role role;

    public UserEntity withPassword(String newPassword) {
        return UserEntity
                .builder()
                .id(id)
                .username(username)
                .email(email)
                .age(age)
                .password(newPassword)
                .enabled(enabled)
                .role(role)
                .build();
    }

    public UserEntity activate() {
        return UserEntity
                .builder()
                .id(id)
                .username(username)
                .email(email)
                .age(age)
                .password(password)
                .enabled(true)
                .role(role)
                .build();
    }

    public UserEmailDto toUserEmailDto() {
        return new UserEmailDto(email);
    }

    public UserDetailsDto toUserDetailsDto() {
        return new UserDetailsDto(username, password, enabled, role.name());
    }
}
