package com.example.log_collect.security;

import com.example.log_collect.models.User;
import com.example.log_collect.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class SecurityService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(SecurityService.class);
    private final UserRepository repository;

    public SecurityService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByUsername(username);
        if (user == null) {
            log.error("no such user found");
            throw new UsernameNotFoundException("no such user present in db");
        } else {
            log.info("user {} found in db", username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getRole().getGrantedAuthority());
    }
}
