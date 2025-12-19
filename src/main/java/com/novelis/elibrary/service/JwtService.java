package com.novelis.elibrary.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;

    // 1 hour
    private static final long EXPIRES_IN_SECONDS = 3600;

    public JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public long expiresInSeconds() {
        return EXPIRES_IN_SECONDS;
    }

    public String generateToken(Authentication auth) {
        Instant now = Instant.now();

        String roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" ")); // ex: "ROLE_USER ROLE_ADMIN"

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("e-library")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(EXPIRES_IN_SECONDS))
                .subject(auth.getName()) // username
                .claim("roles", roles)
                .build();

        return jwtEncoder.encode(
                JwtEncoderParameters.from(
                        JwsHeader.with(MacAlgorithm.HS256).build(),
                        claims
                )
        ).getTokenValue();
    }
}
