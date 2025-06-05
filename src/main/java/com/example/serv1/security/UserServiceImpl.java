package com.example.serv1.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                // Since we're only validating tokens, not authenticating users,
                // we can create a simple UserDetails object with the username
                return new User(
                    username,
                    "", // Empty password since we're not using password authentication
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                );
            }
        };
    }
}