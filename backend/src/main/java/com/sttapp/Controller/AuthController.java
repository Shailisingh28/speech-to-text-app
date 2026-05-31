package com.sttapp.Controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sttapp.DTO.AuthResponse;
import com.sttapp.DTO.LoginRequest;
import com.sttapp.DTO.RegisterRequest;
import com.sttapp.Model.Users;
import com.sttapp.Repository.UserRepo;
import com.sttapp.Security.JwtService;

// Lombok is not being processed for this build, so provide an explicit constructor.
@CrossOrigin("*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepo userRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepo userRepo, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepo.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exist");
        }
        Users users = new Users();
        users.setName(request.getName());
        users.setPassword(passwordEncoder.encode(request.getPassword()));
        users.setEmail(request.getEmail());
        userRepo.save(users);
        return ResponseEntity.ok("User registered");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Users> user = userRepo.findByEmail(request.getEmail());
        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        boolean match = passwordEncoder.matches(request.getPassword(), user.get().getPassword());
        if (!match) {

            return ResponseEntity.badRequest()
                    .body("Invalid password");
        }
        String token = jwtService.generateJWT(request.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
