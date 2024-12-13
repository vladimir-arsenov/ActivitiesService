package org.example.activityservice.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.activityservice.dto.request.ChangePasswordDto;
import org.example.activityservice.model.User;
import org.example.activityservice.repository.UserRepository;
import org.example.activityservice.security.auth.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    private UserRepository userRepositoryMock;

    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserService(userRepositoryMock, passwordEncoder);
    }

    @Test
    public void changePassword_validInput_shouldSaveUser() {
        var changePasswordDto = ChangePasswordDto.builder()
                .currentPassword("cur")
                .newPassword("new")
                .confirmationPassword("new")
                .build();
        var userPrincipalMock = mock(UserPrincipal.class);
        when(userPrincipalMock.getUsername()).thenReturn("email");
        when(userPrincipalMock.getPassword()).thenReturn(passwordEncoder.encode("cur"));
        when(userRepositoryMock.findByEmail("email")).thenReturn(Optional.of(new User()));

        userService.changePassword(changePasswordDto, userPrincipalMock);

        var captor = ArgumentCaptor.forClass(User.class);
        verify(userRepositoryMock).save(captor.capture());
        assertTrue(passwordEncoder.matches(changePasswordDto.getNewPassword(), captor.getValue().getPassword()));
    }

    @Test
    public void changePassword_confirmationPasswordDoesNotMatch_shouldThrowIllegalStateException() {
        var changePasswordDto = ChangePasswordDto.builder()
                .currentPassword("cur")
                .newPassword("new")
                .confirmationPassword("not new")
                .build();
        var userPrincipalMock = mock(UserPrincipal.class);
        when(userPrincipalMock.getUsername()).thenReturn("email");
        when(userPrincipalMock.getPassword()).thenReturn(passwordEncoder.encode("cur"));
        when(userRepositoryMock.findByEmail("email")).thenReturn(Optional.of(new User()));

        var exception = assertThrows(IllegalStateException.class, () -> userService.changePassword(changePasswordDto, userPrincipalMock));
        assertEquals("Passwords do not match", exception.getMessage());
    }

    @Test
    public void changePassword_authenticatedUsersPasswordDoesNotMatch_shouldThrowIllegalStateException() {
        var changePasswordDto = ChangePasswordDto.builder()
                .currentPassword("cur")
                .newPassword("new")
                .confirmationPassword("new")
                .build();
        var userPrincipalMock = mock(UserPrincipal.class);
        when(userPrincipalMock.getUsername()).thenReturn("email");
        when(userPrincipalMock.getPassword()).thenReturn(passwordEncoder.encode("not cur"));
        when(userRepositoryMock.findByEmail("email")).thenReturn(Optional.of(new User()));

        var exception = assertThrows(IllegalStateException.class, () -> userService.changePassword(changePasswordDto, userPrincipalMock));
        assertEquals("Wrong password", exception.getMessage());
    }

    @Test
    public void findByEmail_existingUser_shouldReturnUser() {
        var email = "user@example.com";
        var user = new User();
        user.setEmail(email);
        when(userRepositoryMock.findByEmail(email)).thenReturn(Optional.of(user));

        var result = userService.findByEmail(email);

        assertEquals(user, result);
    }

    @Test
    public void findByEmail_userNotFound_shouldThrowEntityNotFoundException() {
        var email = "nonexistent@example.com";
        when(userRepositoryMock.findByEmail(email)).thenReturn(Optional.empty());

        var exception = assertThrows(EntityNotFoundException.class,
                () -> userService.findByEmail(email));

        assertEquals("User with email nonexistent@example.com not found", exception.getMessage());
    }
}