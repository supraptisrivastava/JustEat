package com.example.JustEat.repository;

import com.example.JustEat.entity.MenuItem;
import com.example.JustEat.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem,Long> {
    List<MenuItem> findByRestaurant(Restaurant restaurant);
    List<MenuItem> findByRestaurant_Id(Long restaurantId);
    Optional<MenuItem> findById(Long id);
    List<MenuItem> findByRestaurant_PublicIdAndIsAvailableTrue(UUID publicId);
    
    // Mostly Ordered feature - get popular items by order count
    List<MenuItem> findByRestaurant_PublicIdAndIsAvailableTrueOrderByOrderCountDesc(UUID publicId);
    
    // Get top N popular items across all restaurants
    List<MenuItem> findTop10ByIsAvailableTrueOrderByOrderCountDesc();
}
