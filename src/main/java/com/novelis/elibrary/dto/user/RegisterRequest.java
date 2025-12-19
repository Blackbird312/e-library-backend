package com.novelis.elibrary.dto.user;

public record RegisterRequest(
        String fullName,
        String email,
        String password
) {}

