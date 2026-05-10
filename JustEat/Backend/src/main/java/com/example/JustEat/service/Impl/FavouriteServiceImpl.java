package com.example.JustEat.service.Impl;

import com.example.JustEat.dto.response.MenuItemResponse;
import com.example.JustEat.dto.response.RestaurantResponse;
import com.example.JustEat.entity.Favourite;
import com.example.JustEat.entity.MenuItem;
import com.example.JustEat.entity.Restaurant;
import com.example.JustEat.entity.User;
import com.example.JustEat.exception.NotFoundException;
import com.example.JustEat.mapper.MenuItemMapper;
import com.example.JustEat.mapper.RestaurantMapper;
import com.example.JustEat.repository.FavouriteRepository;
import com.example.JustEat.repository.MenuItemRepository;
import com.example.JustEat.repository.RestaurantRepository;
import com.example.JustEat.repository.UserRepository;
import com.example.JustEat.service.FavouriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FavouriteServiceImpl implements FavouriteService {

    private final FavouriteRepository favouriteRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String userIdStr = SecurityContextHolder.getContext().getAuthentication().getName();
        UUID userId = UUID.fromString(userIdStr);
        return userRepository.findByPublicId(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    @Transactional
    public RestaurantResponse toggleRestaurantFavourite(UUID restaurantPublicId) {
        User user = getCurrentUser();
        Restaurant restaurant = restaurantRepository.findByPublicId(restaurantPublicId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));

        Optional<Favourite> existing = favouriteRepository.findByUserAndRestaurant(user, restaurant);
        if (existing.isPresent()) {
            favouriteRepository.delete(existing.get());
        } else {
            Favourite fav = new Favourite();
            fav.setUser(user);
            fav.setRestaurant(restaurant);
            favouriteRepository.save(fav);
        }
        return RestaurantMapper.toResponse(restaurant);
    }

    @Override
    @Transactional
    public MenuItemResponse toggleMenuItemFavourite(Long menuItemId) {
        User user = getCurrentUser();
        MenuItem menuItem = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new NotFoundException("Menu item not found"));

        Optional<Favourite> existing = favouriteRepository.findByUserAndMenuItem(user, menuItem);
        if (existing.isPresent()) {
            favouriteRepository.delete(existing.get());
        } else {
            Favourite fav = new Favourite();
            fav.setUser(user);
            fav.setMenuItem(menuItem);
            favouriteRepository.save(fav);
        }
        return MenuItemMapper.toResponse(menuItem);
    }

    @Override
    public List<RestaurantResponse> getFavouriteRestaurants() {
        User user = getCurrentUser();
        return favouriteRepository.findRestaurantFavouritesByUser(user)
                .stream()
                .map(f -> RestaurantMapper.toResponse(f.getRestaurant()))
                .toList();
    }

    @Override
    public List<MenuItemResponse> getFavouriteMenuItems() {
        User user = getCurrentUser();
        return favouriteRepository.findMenuItemFavouritesByUser(user)
                .stream()
                .map(f -> MenuItemMapper.toResponse(f.getMenuItem()))
                .toList();
    }
}

