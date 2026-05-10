package com.example.JustEat.controller;

import com.example.JustEat.dto.request.UpdatePreferenceRequest;
import com.example.JustEat.dto.response.UserPreferenceResponse;
import com.example.JustEat.service.UserPreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/preferences")
@RequiredArgsConstructor
public class UserPreferenceController {

    private final UserPreferenceService userPreferenceService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserPreferenceResponse> getPreferences() {
        return ResponseEntity.ok(userPreferenceService.getPreferences());
    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserPreferenceResponse> savePreferences(@RequestBody UpdatePreferenceRequest request) {
        return ResponseEntity.ok(userPreferenceService.savePreferences(request));
    }
}

