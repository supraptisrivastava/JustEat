package com.example.JustEat.mapper;

import com.example.JustEat.dto.response.MenuItemResponse;
import com.example.JustEat.entity.MenuItem;
import com.example.JustEat.enums.CuisineType;
import com.example.JustEat.enums.DietaryRestriction;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MenuItemMapperTest {

    @Test
    void toResponse_ShouldMapAllFields() {
        // Arrange
        MenuItem item = new MenuItem();
        item.setId(1L);
        item.setName("Butter Chicken");
        item.setDescription("Creamy tomato-based curry");
        item.setPrice(15.99);
        item.setImageUrl("http://example.com/image.jpg");
        item.setCuisineType(CuisineType.INDIAN);
        item.setDietaryRestriction(DietaryRestriction.NON_VEG);
        item.setAvailable(true);
        item.setSpecial(true);
        item.setOrderCount(10);

        // Act
        MenuItemResponse response = MenuItemMapper.toResponse(item);

        // Assert
        assertEquals(1L, response.getId());
        assertEquals("Butter Chicken", response.getName());
        assertEquals("Creamy tomato-based curry", response.getDescription());
        assertEquals(15.99, response.getPrice());
        assertEquals("http://example.com/image.jpg", response.getImageUrl());
        assertEquals(CuisineType.INDIAN, response.getCuisineType());
        assertEquals(DietaryRestriction.NON_VEG, response.getDietaryRestriction());
        assertTrue(response.isAvailable());
        assertTrue(response.isSpecial());
        assertEquals(10, response.getOrderCount());
    }

    @Test
    void toResponse_ShouldSetMostlyOrdered_WhenOrderCountHighEnough() {
        // Arrange
        MenuItem item = new MenuItem();
        item.setId(1L);
        item.setName("Popular Item");
        item.setDescription("Very popular");
        item.setPrice(10.00);
        item.setImageUrl("http://example.com/img.jpg");
        item.setCuisineType(CuisineType.CHINESE);
        item.setDietaryRestriction(DietaryRestriction.VEG);
        item.setAvailable(true);
        item.setSpecial(false);
        item.setOrderCount(5); // Threshold is 5

        // Act
        MenuItemResponse response = MenuItemMapper.toResponse(item);

        // Assert
        assertTrue(response.isMostlyOrdered());
    }

    @Test
    void toResponse_ShouldNotSetMostlyOrdered_WhenOrderCountLow() {
        // Arrange
        MenuItem item = new MenuItem();
        item.setId(2L);
        item.setName("New Item");
        item.setDescription("Newly added");
        item.setPrice(8.00);
        item.setImageUrl("http://example.com/new.jpg");
        item.setCuisineType(CuisineType.ITALIAN);
        item.setDietaryRestriction(DietaryRestriction.VEGAN);
        item.setAvailable(true);
        item.setSpecial(false);
        item.setOrderCount(2);

        // Act
        MenuItemResponse response = MenuItemMapper.toResponse(item);

        // Assert
        assertFalse(response.isMostlyOrdered());
    }

    @Test
    void toResponse_ShouldHandleNullOrderCount() {
        // Arrange
        MenuItem item = new MenuItem();
        item.setId(3L);
        item.setName("Item with null count");
        item.setDescription("Description");
        item.setPrice(5.00);
        item.setImageUrl("http://example.com/null.jpg");
        item.setCuisineType(CuisineType.MEXICAN);
        item.setDietaryRestriction(DietaryRestriction.GLUTEN_FREE);
        item.setAvailable(true);
        item.setSpecial(false);
        item.setOrderCount(null);

        // Act
        MenuItemResponse response = MenuItemMapper.toResponse(item);

        // Assert
        assertFalse(response.isMostlyOrdered());
        assertNull(response.getOrderCount());
    }
}
