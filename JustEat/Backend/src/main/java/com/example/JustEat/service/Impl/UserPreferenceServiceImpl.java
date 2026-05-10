package com.example.JustEat.service.Impl;

import com.example.JustEat.dto.request.UpdatePreferenceRequest;
import com.example.JustEat.dto.response.UserPreferenceResponse;
import com.example.JustEat.entity.User;
import com.example.JustEat.entity.UserPreference;
import com.example.JustEat.exception.NotFoundException;
import com.example.JustEat.repository.UserPreferenceRepository;
import com.example.JustEat.repository.UserRepository;
import com.example.JustEat.service.UserPreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserPreferenceServiceImpl implements UserPreferenceService {

    private final UserPreferenceRepository userPreferenceRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String userIdStr = SecurityContextHolder.getContext().getAuthentication().getName();
        UUID userId = UUID.fromString(userIdStr);
        return userRepository.findByPublicId(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public UserPreferenceResponse getPreferences() {
        User user = getCurrentUser();
        UserPreference pref = userPreferenceRepository.findByUser(user).orElse(null);
        if (pref == null) {
            return UserPreferenceResponse.builder().build();
        }
        return UserPreferenceResponse.builder()
                .favouriteCuisines(pref.getFavouriteCuisines())
                .dietaryRestrictions(pref.getDietaryRestrictions())
                .build();
    }

    @Override
    @Transactional
    public UserPreferenceResponse savePreferences(UpdatePreferenceRequest request) {
        User user = getCurrentUser();
        UserPreference pref = userPreferenceRepository.findByUser(user)
                .orElseGet(() -> {
                    UserPreference p = new UserPreference();
                    p.setUser(user);
                    return p;
                });
        pref.setFavouriteCuisines(request.getFavouriteCuisines());
        pref.setDietaryRestrictions(request.getDietaryRestrictions());
        UserPreference saved = userPreferenceRepository.save(pref);
        return UserPreferenceResponse.builder()
                .favouriteCuisines(saved.getFavouriteCuisines())
                .dietaryRestrictions(saved.getDietaryRestrictions())
                .build();
    }
}

