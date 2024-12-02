package org.example.tfintechgradproject.service;

import lombok.RequiredArgsConstructor;
import org.example.tfintechgradproject.dto.ChangePasswordDto;
import org.example.tfintechgradproject.repository.UserRepository;
import org.example.tfintechgradproject.security.UserPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void changePassword(ChangePasswordDto request, UserPrincipal authenticatedUser) {
        var user = userRepository.findByEmail(authenticatedUser.getUsername())
                .get();
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
}
