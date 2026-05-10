package com.example.JustEat.controller;

import com.example.JustEat.dto.response.MenuItemResponse;
import com.example.JustEat.dto.response.RestaurantResponse;
import com.example.JustEat.service.FavouriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/favourites")
@RequiredArgsConstructor
public class FavouriteController {

    private final FavouriteService favouriteService;

    @PostMapping("/restaurants/{publicId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<RestaurantResponse> toggleRestaurantFavourite(@PathVariable UUID publicId) {
        return ResponseEntity.ok(favouriteService.toggleRestaurantFavourite(publicId));
    }

    @PostMapping("/menu-items/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<MenuItemResponse> toggleMenuItemFavourite(@PathVariable Long id) {
        return ResponseEntity.ok(favouriteService.toggleMenuItemFavourite(id));
    }

    @GetMapping("/restaurants")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<RestaurantResponse>> getFavouriteRestaurants() {
        return ResponseEntity.ok(favouriteService.getFavouriteRestaurants());
    }

    @GetMapping("/menu-items")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<MenuItemResponse>> getFavouriteMenuItems() {
        return ResponseEntity.ok(favouriteService.getFavouriteMenuItems());
    }
}

