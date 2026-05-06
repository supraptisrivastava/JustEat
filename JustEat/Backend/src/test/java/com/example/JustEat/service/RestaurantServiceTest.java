package com.example.JustEat.service;

import com.example.JustEat.dto.response.RestaurantResponse;
import com.example.JustEat.entity.Restaurant;
import com.example.JustEat.entity.User;
import com.example.JustEat.enums.CuisineType;
import com.example.JustEat.enums.Location;
import com.example.JustEat.enums.RestaurantStatus;
import com.example.JustEat.exception.BadRequestException;
import com.example.JustEat.exception.NotFoundException;
import com.example.JustEat.repository.OrderRepository;
import com.example.JustEat.repository.RestaurantRepository;
import com.example.JustEat.repository.UserPreferenceRepository;
import com.example.JustEat.repository.UserRepository;
import com.example.JustEat.service.Impl.RestaurantServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private UserPreferenceRepository userPreferenceRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    private Restaurant restaurant;
    private User owner;
    private UUID restaurantPublicId;

    @BeforeEach
    void setUp() {
        restaurantPublicId = UUID.randomUUID();

        owner = new User();
        owner.setId(1L);
        owner.setPublicId(UUID.randomUUID());

        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setPublicId(restaurantPublicId);
        restaurant.setName("Test Restaurant");
        restaurant.setDescription("Test description");
        restaurant.setLocation(Location.DELHI);
        restaurant.setCuisineTypes(List.of(CuisineType.INDIAN));
        restaurant.setStatus(RestaurantStatus.OPEN);
        restaurant.setOwner(owner);
    }

    @Test
    void getAllRestaurants_ShouldReturnAllOpenRestaurants() {
        // Arrange
        when(restaurantRepository.findByStatus(RestaurantStatus.OPEN))
                .thenReturn(List.of(restaurant));

        // Act
        List<RestaurantResponse> result = restaurantService.getAllRestaurants(null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Restaurant", result.get(0).getName());
    }

    @Test
    void getAllRestaurants_ShouldFilterByLocation() {
        // Arrange
        when(restaurantRepository.findByLocationAndStatus(Location.DELHI, RestaurantStatus.OPEN))
                .thenReturn(List.of(restaurant));

        // Act
        List<RestaurantResponse> result = restaurantService.getAllRestaurants(Location.DELHI);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Location.DELHI, result.get(0).getLocation());
    }

    @Test
    void getRestaurant_ShouldReturnRestaurant_WhenExists() {
        // Arrange
        when(restaurantRepository.findByPublicId(restaurantPublicId))
                .thenReturn(Optional.of(restaurant));

        // Act
        RestaurantResponse result = restaurantService.getRestaurant(restaurantPublicId);

        // Assert
        assertNotNull(result);
        assertEquals("Test Restaurant", result.getName());
    }

    @Test
    void getRestaurant_ShouldThrowNotFoundException_WhenNotExists() {
        // Arrange
        when(restaurantRepository.findByPublicId(restaurantPublicId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> {
            restaurantService.getRestaurant(restaurantPublicId);
        });
    }

    @Test
    void getRestaurant_ShouldThrowBadRequest_WhenNotOpen() {
        // Arrange
        restaurant.setStatus(RestaurantStatus.CLOSED);
        when(restaurantRepository.findByPublicId(restaurantPublicId))
                .thenReturn(Optional.of(restaurant));

        // Act & Assert
        assertThrows(BadRequestException.class, () -> {
            restaurantService.getRestaurant(restaurantPublicId);
        });
    }

    @Test
    void searchRestaurants_ShouldReturnResults() {
        // Arrange
        when(restaurantRepository.searchRestaurants("Test", CuisineType.INDIAN, null))
                .thenReturn(List.of(restaurant));

        // Act
        List<RestaurantResponse> result = restaurantService.searchRestaurants("Test", "INDIAN", null);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void searchRestaurants_ShouldThrowBadRequest_WhenInvalidCuisine() {
        // Act & Assert
        assertThrows(BadRequestException.class, () -> {
            restaurantService.searchRestaurants("Test", "INVALID_CUISINE", null);
        });
    }
}

