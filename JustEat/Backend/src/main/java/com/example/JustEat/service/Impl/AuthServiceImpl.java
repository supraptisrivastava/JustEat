package com.example.JustEat.service.Impl;

import com.example.JustEat.service.EmailService;
import java.time.LocalDateTime;
import java.util.UUID;
import com.example.JustEat.dto.response.AuthResponse;
import com.example.JustEat.dto.request.LoginRequest;
import com.example.JustEat.dto.request.RegisterRequest;
import com.example.JustEat.entity.User;
import com.example.JustEat.entity.UserPreference;
import com.example.JustEat.enums.Role;
import com.example.JustEat.exception.BadRequestException;
import com.example.JustEat.repository.UserPreferenceRepository;
import com.example.JustEat.repository.UserRepository;
import com.example.JustEat.security.JwtUtil;
import com.example.JustEat.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserPreferenceRepository userPreferenceRepository;
    private final PasswordEncoder passwordEncoder;
    private  final JwtUtil jwtUtil;
    private final EmailService emailService;

    @Transactional
    public void register(RegisterRequest req){
        if(userRepository.existsByEmail(req.getEmail())){
            throw new BadRequestException("Email already exists");
        }
        User user = new User();
        user.setEmail(req.getEmail());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setFirstName(req.getFirstName());
        user.setLastName(req.getLastName());
        if (req.getRole() != Role.CUSTOMER && req.getRole() != Role.OWNER) {
            throw new BadRequestException("Invalid role selection");
        }
        user.setRole(req.getRole());
        user.setLocation(req.getLocation());
        user.setGender(req.getGender());
        user.setPhoneNumber(req.getPhoneNumber());
        User savedUser = userRepository.save(user);

        // Save user preferences (for CUSTOMER)
        if (req.getRole() == Role.CUSTOMER && 
            (req.getFavouriteCuisines() != null || req.getDietaryRestrictions() != null)) {
            UserPreference preference = new UserPreference();
            preference.setUser(savedUser);
            preference.setFavouriteCuisines(req.getFavouriteCuisines());
            preference.setDietaryRestrictions(req.getDietaryRestrictions());
            userPreferenceRepository.save(preference);
        }
    }
    
    public AuthResponse login(LoginRequest req){
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(()-> new BadRequestException("Invalid credentials"));

        if(!passwordEncoder.matches(req.getPassword(),user.getPasswordHash())){
            throw new RuntimeException("Invalid credentials");
        }
        String token= jwtUtil.generateToken(user.getPublicId(), user.getRole().name());
        return AuthResponse.builder()
                .token(token)
                .role(user.getRole().name())
                .userId(user.getPublicId())
                .location(user.getLocation() != null ? user.getLocation().name() : null)
                .build();
    }

    @Override
    public void forgotPassword(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("User not found"));

        String token = UUID.randomUUID().toString();

        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(15));

        userRepository.save(user);

        // 🔥 Send email
        emailService.sendPasswordResetEmail(user.getEmail(), token);
    }

    @Override
    public void resetPassword(String token, String newPassword) {

        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new BadRequestException("Invalid token"));

        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Token expired");
        }

        user.setPasswordHash(passwordEncoder.encode(newPassword));

        user.setResetToken(null);
        user.setResetTokenExpiry(null);

        userRepository.save(user);
    }


}
