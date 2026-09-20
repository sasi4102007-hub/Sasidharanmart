package com.sasidharanmart.shop.controller;

import com.sasidharanmart.shop.model.User;
import com.sasidharanmart.shop.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Handles the registration page (name, phone, address, email, password)
 * and hands login itself off to Spring Security (see SecurityConfig).
 */
@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ---- LOGIN PAGE ----
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // renders templates/login.html
    }

    // ---- REGISTRATION PAGE ----
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register"; // renders templates/register.html
    }

    @PostMapping("/register")
    public String registerSubmit(@Valid @ModelAttribute("user") User user,
                                  BindingResult result,
                                  Model model) {

        if (result.hasErrors()) {
            return "register";
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            model.addAttribute("emailError", "An account with this email already exists.");
            return "register";
        }

        // Hash the password before saving — never store plain text
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_BUYER");
        userRepository.save(user);

        return "redirect:/login?registered=true";
    }
}
