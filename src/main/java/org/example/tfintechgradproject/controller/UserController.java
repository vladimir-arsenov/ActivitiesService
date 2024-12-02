package org.example.tfintechgradproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.tfintechgradproject.dto.ChangePasswordDto;
import org.example.tfintechgradproject.security.UserPrincipal;
import org.example.tfintechgradproject.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PatchMapping("/password")
    public void changePassword(@RequestBody ChangePasswordDto request, @AuthenticationPrincipal UserPrincipal authenticatedUser) {
        userService.changePassword(request, authenticatedUser);
    }
}
