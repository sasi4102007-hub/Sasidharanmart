package com.sasidharanmart.shop.service;

import com.sasidharanmart.shop.model.User;
import com.sasidharanmart.shop.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * Spring Security only knows the logged-in principal's email (username).
 * This resolves that back to the full User entity (name, phone, address)
 * so controllers can use it directly.
 */
@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Logged-in user not found: " + email));
    }
}
