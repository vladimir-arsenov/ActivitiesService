package org.example.tfintechgradproject.controller;

import lombok.RequiredArgsConstructor;
import org.example.tfintechgradproject.service.UserService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
}
