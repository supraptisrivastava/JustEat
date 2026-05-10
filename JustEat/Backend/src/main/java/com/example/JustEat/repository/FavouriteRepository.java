package com.example.JustEat.repository;

import com.example.JustEat.entity.Favourite;
import com.example.JustEat.entity.MenuItem;
import com.example.JustEat.entity.Restaurant;
import com.example.JustEat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, Long> {

    Optional<Favourite> findByUserAndRestaurant(User user, Restaurant restaurant);

    Optional<Favourite> findByUserAndMenuItem(User user, MenuItem menuItem);

    @Query("SELECT f FROM Favourite f JOIN FETCH f.restaurant WHERE f.user = :user AND f.restaurant IS NOT NULL")
    List<Favourite> findRestaurantFavouritesByUser(@Param("user") User user);

    @Query("SELECT f FROM Favourite f JOIN FETCH f.menuItem WHERE f.user = :user AND f.menuItem IS NOT NULL")
    List<Favourite> findMenuItemFavouritesByUser(@Param("user") User user);

    @Query("SELECT f.restaurant FROM Favourite f WHERE f.user = :user AND f.restaurant IS NOT NULL")
    List<Restaurant> findFavouriteRestaurantsByUser(@Param("user") User user);

    boolean existsByUserAndRestaurant(User user, Restaurant restaurant);

    boolean existsByUserAndMenuItem(User user, MenuItem menuItem);
}

