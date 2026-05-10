package com.example.JustEat.service;

import com.example.JustEat.dto.request.UpdatePreferenceRequest;
import com.example.JustEat.dto.response.UserPreferenceResponse;

public interface UserPreferenceService {
    UserPreferenceResponse getPreferences();
    UserPreferenceResponse savePreferences(UpdatePreferenceRequest request);
}

