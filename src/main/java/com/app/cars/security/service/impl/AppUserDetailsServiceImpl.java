package com.app.cars.security.service.impl;

import com.app.cars.persistence.repository.UserEntityRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class AppUserDetailsServiceImpl implements UserDetailsService {
    private final UserEntityRepository userEntityRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userEntityRepository
                .findByUsername(username)
                .map( userFromDb -> {
                    var userDetails = userFromDb.toUserDetailsDto();
                    return new User(
                            userDetails.username(),
                            userDetails.password(),
                            userDetails.enabled(),
                            true,
                            true,
                            true,
                            List.of(new SimpleGrantedAuthority(userDetails.role()))
                    );}
                ).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
