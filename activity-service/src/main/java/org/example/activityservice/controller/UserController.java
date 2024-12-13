package org.example.activityservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.activityservice.dto.request.ChangePasswordDto;
import org.example.activityservice.security.auth.UserPrincipal;
import org.example.activityservice.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PatchMapping("/password")
    public void changePassword(@Valid @RequestBody ChangePasswordDto request, @AuthenticationPrincipal UserPrincipal authenticatedUser) {
        userService.changePassword(request, authenticatedUser);
    }
}
