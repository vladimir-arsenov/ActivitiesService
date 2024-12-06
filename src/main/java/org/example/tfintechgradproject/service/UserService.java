package org.example.tfintechgradproject.service;

import lombok.RequiredArgsConstructor;
import org.example.tfintechgradproject.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

}
