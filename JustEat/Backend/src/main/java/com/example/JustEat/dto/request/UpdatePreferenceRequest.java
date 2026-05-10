package com.example.JustEat.dto.request;

import com.example.JustEat.enums.CuisineType;
import com.example.JustEat.enums.DietaryRestriction;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdatePreferenceRequest {
    private List<CuisineType> favouriteCuisines;
    private List<DietaryRestriction> dietaryRestrictions;
}

