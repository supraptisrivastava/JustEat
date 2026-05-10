package com.example.JustEat.repository;
import com.example.JustEat.enums.CuisineType;
import com.example.JustEat.entity.Restaurant;
import com.example.JustEat.enums.Location;
import com.example.JustEat.enums.RestaurantStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant,Long> {
    List<Restaurant> findByLocationAndStatus(Location location, RestaurantStatus status);
    List<Restaurant> findByStatus(RestaurantStatus status);
    Optional<Restaurant> findByPublicId(UUID publicId);
    List<Restaurant> findByOwnerPublicId(UUID ownerPublicId);
    List<Restaurant> findByCuisineTypesIn(List<CuisineType> cuisines);
    @Query("""
    SELECT r FROM Restaurant r
    WHERE r.status = com.example.JustEat.enums.RestaurantStatus.OPEN
        AND (:keyword IS NULL OR LOWER(r.name) LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%')))
        AND (:cuisine IS NULL OR :cuisine MEMBER OF r.cuisineTypes)
        AND (:location IS NULL OR r.location = :location)
""")
    List<Restaurant> searchRestaurants(
            @Param("keyword") String keyword,
            @Param("cuisine") CuisineType cuisine,
            @Param("location") Location location
    );
}
