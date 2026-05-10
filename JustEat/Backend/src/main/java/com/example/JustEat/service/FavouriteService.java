package com.example.JustEat.service;

import com.example.JustEat.dto.response.MenuItemResponse;
import com.example.JustEat.dto.response.RestaurantResponse;

import java.util.List;
import java.util.UUID;

public interface FavouriteService {
    RestaurantResponse toggleRestaurantFavourite(UUID restaurantPublicId);
    MenuItemResponse toggleMenuItemFavourite(Long menuItemId);
    List<RestaurantResponse> getFavouriteRestaurants();
    List<MenuItemResponse> getFavouriteMenuItems();
}

