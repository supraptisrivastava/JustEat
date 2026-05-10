package com.example.JustEat.dto.response;

import com.example.JustEat.enums.CuisineType;
import com.example.JustEat.enums.DietaryRestriction;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UserPreferenceResponse {
    private List<CuisineType> favouriteCuisines;
    private List<DietaryRestriction> dietaryRestrictions;
}

