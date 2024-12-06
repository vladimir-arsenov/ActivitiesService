package org.example.tfintechgradproject.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.tfintechgradproject.dto.request.ChangePasswordDto;
import org.example.tfintechgradproject.model.User;
import org.example.tfintechgradproject.repository.UserRepository;
import org.example.tfintechgradproject.security.auth.UserPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void changePassword(ChangePasswordDto request, UserPrincipal authenticatedUser) {
        var user = findByEmail(authenticatedUser.getUsername());
        if (!passwordEncoder.matches(request.getCurrentPassword(), authenticatedUser.getPassword())) {
            throw new IllegalStateException("Wrong password");
        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Passwords do not match");
        }

        var encodedPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }


    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email %s not found".formatted(email)));
    }
}
