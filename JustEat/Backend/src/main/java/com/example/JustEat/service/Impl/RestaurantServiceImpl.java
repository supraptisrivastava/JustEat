package com.example.JustEat.service.Impl;
import com.example.JustEat.enums.CuisineType;
import com.example.JustEat.dto.request.CreateRestaurantRequest;
import com.example.JustEat.dto.response.RestaurantResponse;
import com.example.JustEat.entity.Restaurant;
import com.example.JustEat.entity.User;
import com.example.JustEat.enums.Location;
import com.example.JustEat.exception.BadRequestException;
import com.example.JustEat.exception.NotFoundException;
import com.example.JustEat.mapper.RestaurantMapper;
import com.example.JustEat.repository.RestaurantRepository;
import com.example.JustEat.repository.UserRepository;
import com.example.JustEat.service.CloudinaryService;
import com.example.JustEat.repository.UserPreferenceRepository;
import com.example.JustEat.repository.OrderRepository;
import com.example.JustEat.repository.FavouriteRepository;
import com.example.JustEat.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import com.example.JustEat.entity.Order;
import com.example.JustEat.entity.UserPreference;
import java.util.List;
import java.util.Map;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final CloudinaryService cloudinaryService;
    private final UserPreferenceRepository userPreferenceRepository;
    private final OrderRepository orderRepository;
    private final FavouriteRepository favouriteRepository;
    private User getCurrentUser() {
        String userIdStr = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        UUID userId = UUID.fromString(userIdStr);

        return userRepository.findByPublicId(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
    @Override
    public RestaurantResponse createRestaurant(CreateRestaurantRequest request, MultipartFile image) {
        //get current user from jwt
        String userIdStr = SecurityContextHolder.getContext().getAuthentication().getName();
        UUID userId = UUID.fromString(userIdStr);
        User owner = userRepository.findByPublicId(userId).orElseThrow(() -> new NotFoundException("User not found"));

        // Upload image to Cloudinary
        Map<String, String> uploadResult = cloudinaryService.uploadImage(image, "justeat/restaurants");
        String imageUrl = uploadResult.get("url");
        String publicId = uploadResult.get("publicId");

        //create restaurant
        Restaurant restaurant = new Restaurant();
        restaurant.setName(request.getName());
        restaurant.setDescription(request.getDescription());
        restaurant.setLocation(request.getLocation());
        restaurant.setCuisineTypes(request.getCuisineTypes());
        restaurant.setImageUrl(imageUrl);
        restaurant.setImagePublicId(publicId);
        restaurant.setOwner(owner);
        
        Restaurant saved = restaurantRepository.save(restaurant);
        return RestaurantMapper.toResponse(saved);
    }

    @Override
    public List<RestaurantResponse> getAllRestaurants(Location location) {
        List<Restaurant> restaurants;
        if(location!=null){
            restaurants = restaurantRepository.findByLocationAndStatus(
                    location,
                    com.example.JustEat.enums.RestaurantStatus.OPEN
            );
        }else{
            restaurants = restaurantRepository.findByStatus(
                    com.example.JustEat.enums.RestaurantStatus.OPEN
            );
        }
        return restaurants.stream().map(restaurant -> RestaurantMapper.toResponse(restaurant))
                .toList();
    }

    public RestaurantResponse getRestaurant(UUID publicId){
        Restaurant restaurant = restaurantRepository.findByPublicId(publicId)
                .orElseThrow(()-> new NotFoundException("Restaurant not found"));
        validateRestaurantOpen(restaurant);
        return RestaurantMapper.toResponse(restaurant);
    }

    private void validateRestaurantOpen(Restaurant restaurant) {
        if (restaurant.getStatus() != com.example.JustEat.enums.RestaurantStatus.OPEN) {
            throw new BadRequestException("Restaurant is not available");
        }
    }

    @Override
    public List<RestaurantResponse> getMyRestaurants() {
        String userIdStr = SecurityContextHolder.getContext().getAuthentication().getName();
        UUID ownerId = UUID.fromString(userIdStr);
        return restaurantRepository.findByOwnerPublicId(ownerId)
                .stream()
                .map(RestaurantMapper::toResponse)
                .toList();
    }
    @Override
    public List<RestaurantResponse> getRecommendations() {
        User user = getCurrentUser();
        UserPreference preference = userPreferenceRepository.findByUser(user)
                .orElse(null);

        List<Restaurant> recommended = new ArrayList<>();

        // Priority 1: Favourite restaurants
        List<Restaurant> favRestaurants = favouriteRepository.findFavouriteRestaurantsByUser(user);
        recommended.addAll(favRestaurants);

        // Priority 2: Restaurants based on favourite cuisines
        if (preference != null && preference.getFavouriteCuisines() != null && !preference.getFavouriteCuisines().isEmpty()) {
            recommended.addAll(
                    restaurantRepository.findByCuisineTypesIn(preference.getFavouriteCuisines())
            );
        }

        // Priority 3: Restaurants from order history
        List<Order> orders = orderRepository.findByUserOrderByCreatedAtDesc(user);
        for (Order order : orders) {
            recommended.add(order.getRestaurant());
        }

        List<Restaurant> unique = recommended.stream().distinct().toList();
        return unique.stream()
                .limit(10)
                .map(RestaurantMapper::toResponse)
                .toList();
    }
    @Override
    public List<RestaurantResponse> searchRestaurants(String keyword, String cuisine, Location location) {

        CuisineType cuisineEnum = null;

        if (cuisine != null && !cuisine.isEmpty()) {
            try {
                cuisineEnum = CuisineType.valueOf(cuisine.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid cuisine type");
            }
        }

        String keywordParam = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        List<Restaurant> restaurants = restaurantRepository
                .searchRestaurants(keywordParam, cuisineEnum, location);

        return restaurants.stream()
                .map(RestaurantMapper::toResponse)
                .toList();
    };
};
