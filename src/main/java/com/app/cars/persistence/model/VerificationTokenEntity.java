package com.app.cars.persistence.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(name = "verification_tokens")
public class VerificationTokenEntity extends BaseEntity{
    private String token;
    private Long timestamp;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", unique = true)
    @Getter
    private UserEntity user;

    public Optional<UserEntity> validate() {
        var currentTime = System.nanoTime();
        return Optional.ofNullable(timestamp >= currentTime ? user : null);
    }
}
