package org.example.tfintechgradproject.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.example.tfintechgradproject.repository.JwtTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Component
public class JwtService {

    private final String secretKey;
    private final JwtTokenRepository jwtTokenRepository;
    @Value("${application.security.jwt.ttl}")
    private long jwtTtl;
    @Value("${application.security.jwt.ttlRememberMe}")
    private long jwtTtlRememberMe;

    public JwtService(JwtTokenRepository jwtTokenRepository) throws NoSuchAlgorithmException {
        this.jwtTokenRepository = jwtTokenRepository;
        secretKey = Encoders.BASE64.encode(KeyGenerator.getInstance("HmacSHA256").generateKey().getEncoded());
    }

    public String generateToken(String username, boolean isRememberMe) {
        var claims = new HashMap<String, Object>();
        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + (isRememberMe ? jwtTtlRememberMe : jwtTtl)))
                .and()
                .signWith(getSignInKey())
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        var validTokenFoundInDB = jwtTokenRepository.findByToken(token)
                .map(t -> !t.isExpired())
                .orElse(false);

        return validTokenFoundInDB && username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private SecretKey getSignInKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private  <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


}
