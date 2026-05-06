package com.example.JustEat.service;

import com.example.JustEat.dto.request.CreateMenuItemRequest;
import com.example.JustEat.dto.response.MenuItemResponse;
import com.example.JustEat.entity.MenuItem;
import com.example.JustEat.entity.Restaurant;
import com.example.JustEat.entity.User;
import com.example.JustEat.enums.CuisineType;
import com.example.JustEat.enums.DietaryRestriction;
import com.example.JustEat.exception.ForbiddenException;
import com.example.JustEat.exception.NotFoundException;
import com.example.JustEat.repository.MenuItemRepository;
import com.example.JustEat.repository.RestaurantRepository;
import com.example.JustEat.service.Impl.MenuItemServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuItemServiceTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private MenuItemServiceImpl menuItemService;

    private Restaurant restaurant;
    private User owner;
    private MenuItem menuItem;
    private UUID restaurantPublicId;
    private UUID ownerPublicId;

    @BeforeEach
    void setUp() {
        ownerPublicId = UUID.randomUUID();
        restaurantPublicId = UUID.randomUUID();

        owner = new User();
        owner.setId(1L);
        owner.setPublicId(ownerPublicId);
        owner.setEmail("owner@test.com");

        restaurant = new Restaurant();
        restaurant.setId(1L);
        restaurant.setPublicId(restaurantPublicId);
        restaurant.setName("Test Restaurant");
        restaurant.setOwner(owner);

        menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItem.setName("Test Item");
        menuItem.setPrice(9.99);
        menuItem.setDescription("Test description");
        menuItem.setImageUrl("http://test.com/image.jpg");
        menuItem.setCuisineType(CuisineType.INDIAN);
        menuItem.setDietaryRestriction(DietaryRestriction.VEG);
        menuItem.setRestaurant(restaurant);
        menuItem.setAvailable(true);
        menuItem.setSpecial(false);
        menuItem.setOrderCount(0);
    }

    @Test
    void getMenu_ShouldReturnMenuItems() {
        // Arrange
        when(restaurantRepository.findByPublicId(restaurantPublicId))
                .thenReturn(Optional.of(restaurant));
        when(menuItemRepository.findByRestaurant_PublicIdAndIsAvailableTrue(restaurantPublicId))
                .thenReturn(List.of(menuItem));

        // Act
        List<MenuItemResponse> result = menuItemService.getMenu(restaurantPublicId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Item", result.get(0).getName());
    }

    @Test
    void getMenu_ShouldThrowNotFoundException_WhenRestaurantNotFound() {
        // Arrange
        when(restaurantRepository.findByPublicId(restaurantPublicId))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> {
            menuItemService.getMenu(restaurantPublicId);
        });
    }

    @Test
    void getPopularItems_ShouldReturnTopItems() {
        // Arrange
        menuItem.setOrderCount(10);
        when(menuItemRepository.findByRestaurant_PublicIdAndIsAvailableTrueOrderByOrderCountDesc(restaurantPublicId))
                .thenReturn(List.of(menuItem));

        // Act
        List<MenuItemResponse> result = menuItemService.getPopularItems(restaurantPublicId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).isMostlyOrdered());
    }

    @Test
    void incrementOrderCount_ShouldIncreaseCount() {
        // Arrange
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(menuItem);

        // Act
        menuItemService.incrementOrderCount(1L, 3);

        // Assert
        assertEquals(3, menuItem.getOrderCount());
        verify(menuItemRepository).save(menuItem);
    }

    @Test
    void incrementOrderCount_ShouldThrowNotFoundException() {
        // Arrange
        when(menuItemRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> {
            menuItemService.incrementOrderCount(999L, 1);
        });
    }

    @Test
    void deleteMenuItem_ShouldDelete_WhenAuthorized() {
        // Arrange
        when(restaurantRepository.findByPublicId(restaurantPublicId))
                .thenReturn(Optional.of(restaurant));
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));

        // Act
        menuItemService.deleteMenuItem(restaurantPublicId, 1L, ownerPublicId);

        // Assert
        verify(menuItemRepository).delete(menuItem);
    }

    @Test
    void deleteMenuItem_ShouldThrowForbidden_WhenNotAuthorized() {
        // Arrange
        UUID differentUserId = UUID.randomUUID();
        when(restaurantRepository.findByPublicId(restaurantPublicId))
                .thenReturn(Optional.of(restaurant));
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(menuItem));

        // Act & Assert
        assertThrows(ForbiddenException.class, () -> {
            menuItemService.deleteMenuItem(restaurantPublicId, 1L, differentUserId);
        });
    }
}
