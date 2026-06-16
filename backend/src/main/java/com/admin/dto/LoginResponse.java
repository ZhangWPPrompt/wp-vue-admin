package com.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Collection;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String username;
    private String nickname;
    private String avatar;
    private Collection<String> roles;
    private Collection<String> permissions;
}
