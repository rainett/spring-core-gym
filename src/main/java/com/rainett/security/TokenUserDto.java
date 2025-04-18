package com.rainett.security;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Data
public class TokenUserDto {
    private String username;
    private String token;
    private final List<SimpleGrantedAuthority> authorities = new ArrayList<>();

    public void addAuthority(String authority) {
        authorities.add(new SimpleGrantedAuthority(authority));
    }
}
