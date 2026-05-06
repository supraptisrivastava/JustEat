package com.example.JustEat.mapper;

import com.example.JustEat.dto.response.MenuItemResponse;
import com.example.JustEat.entity.MenuItem;

public class MenuItemMapper {
    
    private static final int MOSTLY_ORDERED_THRESHOLD = 5;
    
    public static MenuItemResponse toResponse(MenuItem item){
        boolean isMostlyOrdered = item.getOrderCount() != null && item.getOrderCount() >= MOSTLY_ORDERED_THRESHOLD;
        
        return MenuItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .price(item.getPrice())
                .imageUrl(item.getImageUrl())
                .cuisineType(item.getCuisineType())
                .dietaryRestriction(item.getDietaryRestriction())
                .available(item.isAvailable())
                .isSpecial(item.isSpecial())
                .orderCount(item.getOrderCount())
                .isMostlyOrdered(isMostlyOrdered)
                .build();
    }
}
