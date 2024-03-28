package com.app.cars.events;

import com.app.cars.persistence.model.VerificationTokenEntity;
import com.app.cars.persistence.repository.UserEntityRepository;
import com.app.cars.persistence.repository.VerificationTokenEntityRepository;
import com.app.cars.service.EmailService;
import com.app.cars.service.dto.UserActivationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserActivationListener {

    @Value("${km-config.activation-mail-expiration-time}")
    private Long activationMailExpirationTime;

    private final UserEntityRepository userEntityRepository;
    private final VerificationTokenEntityRepository verificationTokenEntityRepository;
    private final EmailService emailService;

    @Async
    @EventListener
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendActivationMail(UserActivationDto userActivationDto) {
        var userToActivate = userEntityRepository
                .findById(userActivationDto.userId())
                .orElseThrow();

        var token = UUID.randomUUID().toString().replaceAll("\\W", "");

        var timestamp = LocalDateTime
                .now()
                .toInstant(ZoneOffset.UTC)
                .getNano() + activationMailExpirationTime;

        var verificationToken = VerificationTokenEntity
                .builder()
                .user(userToActivate)
                .token(token)
                .timestamp(timestamp)
                .build();

        verificationTokenEntityRepository.save(verificationToken);

        emailService.send(
                userToActivate.toUserEmailDto().email(),
                "Activation link",
                "Use activation code: " + token
        );
    }
}
