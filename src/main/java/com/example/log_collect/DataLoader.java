package com.example.log_collect;

import com.example.log_collect.models.User;
import com.example.log_collect.permission.Role;
import com.example.log_collect.security.PasswordEncoder;
import com.example.log_collect.user.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {
    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public DataLoader(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        User user = repository.findByUsername("09924664362");
        if (user == null) {
            User manager = new User();
            manager.setRole(Role.MANAGER);
            manager.setPassword(encoder.passwordEncoder().encode("123456789"));
            manager.setUsername("09924664362");
            repository.save(manager);
        }

    }
}

