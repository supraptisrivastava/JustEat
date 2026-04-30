package com.example.JustEat.service.Impl;

import com.example.JustEat.entity.Cart;
import com.example.JustEat.entity.Order;
import com.example.JustEat.entity.OrderItem;
import com.example.JustEat.entity.User;
import com.example.JustEat.enums.OrderStatus;
import com.example.JustEat.exception.NotFoundException;
import com.example.JustEat.repository.CartRepository;
import com.example.JustEat.repository.OrderRepository;
import com.example.JustEat.repository.UserRepository;
import com.example.JustEat.service.CartService;
import com.example.JustEat.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartService cartService;

    private User getCurrentUser() {
        String userIdStr = SecurityContextHolder.getContext().getAuthentication().getName();
        UUID userId = UUID.fromString(userIdStr);
        return userRepository.findByPublicId(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    @Transactional
    public Order placeOrder() {
        User user = getCurrentUser();

        // Get user's cart
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new NotFoundException("Cart is empty"));

        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot place order with empty cart");
        }

        // Create order
        Order order = new Order();
        order.setUser(user);
        order.setRestaurant(cart.getRestaurant());
        order.setStatus(OrderStatus.PENDING);
//        order.setOrderTime(LocalDateTime.now());
        order.setTotalAmount(cart.getTotalAmount());

        // Convert cart items to order items
        order.setItems(cart.getItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setMenuItem(cartItem.getMenuItem());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getPrice());
                    return orderItem;
                })
                .collect(Collectors.toList()));

        // Save order
        Order savedOrder = orderRepository.save(order);

        // Clear cart
        cartService.clearCart();

        return savedOrder;
    }
}