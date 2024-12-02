package org.example.tfintechgradproject.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.tfintechgradproject.repository.JwtTokenRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final JwtTokenRepository jwtTokenRepository;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            return;
        }
        String jwtToken = authHeader.substring(7);
        var storedToken = jwtTokenRepository.findByToken(jwtToken)
                .orElse(null);
        if (storedToken != null) {
            storedToken.setExpired(true);
            jwtTokenRepository.save(storedToken);
            SecurityContextHolder.clearContext();
        }
    }
}