package org.example.tfintechgradproject.security.auth;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.tfintechgradproject.security.dto.AuthenticationRequestDto;
import org.example.tfintechgradproject.security.dto.AuthenticationResponseDto;
import org.example.tfintechgradproject.security.dto.RegisterRequestDto;
import org.example.tfintechgradproject.security.jwt.JwtService;
import org.example.tfintechgradproject.security.jwt.JwtToken;
import org.example.tfintechgradproject.model.Role;
import org.example.tfintechgradproject.model.User;
import org.example.tfintechgradproject.repository.UserRepository;
import org.example.tfintechgradproject.security.jwt.JwtTokenRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository repository;
    private final JwtTokenRepository jwtTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponseDto register(RegisterRequestDto request) {
        var savedUser = repository.save(
                User.builder()
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .role(Role.USER)
                        .nickname(request.getNickname())
                        .rating(new BigDecimal("0.0"))
                        .build()
        );
        var jwtToken = jwtService.generateToken(savedUser.getEmail(), request.isRememberMe());
        saveUserToken(savedUser, jwtToken);
        return new AuthenticationResponseDto(jwtToken);
    }

    public AuthenticationResponseDto authenticate(AuthenticationRequestDto request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("User with email " + request.getEmail() + " not found"));
        var jwtToken = jwtService.generateToken(user.getEmail(), request.isRememberMe());
        saveUserToken(user, jwtToken);
        return new AuthenticationResponseDto(jwtToken);
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = JwtToken.builder()
                .user(user)
                .token(jwtToken)
                .expired(false)
                .build();
        jwtTokenRepository.save(token);
    }
}