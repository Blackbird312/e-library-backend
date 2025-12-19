package com.novelis.elibrary.dto.login;

import lombok.*;

@Builder
@Getter
public class AuthResponse {
    private String token;
    private String type = "Bearer";
    private long expiresInSeconds;
}
