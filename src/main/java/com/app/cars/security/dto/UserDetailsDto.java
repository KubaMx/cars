package com.app.cars.security.dto;

public record UserDetailsDto(String username, String password, boolean enabled, String role) {
}
