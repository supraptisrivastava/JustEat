package com.example.JustEat.mapper;

import com.example.JustEat.dto.response.OrderItemResponse;
import com.example.JustEat.dto.response.OrderResponse;
import com.example.JustEat.entity.Order;

import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderResponse toResponse(Order order) {

        return OrderResponse.builder()
                .publicId(order.getPublicId())
                .restaurantName(order.getRestaurant().getName())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())

                .items(
                        order.getItems().stream()
                                .map(item -> OrderItemResponse.builder()
                                        .menuItemId(item.getMenuItem().getId())
                                        .name(item.getMenuItem().getName())
                                        .price(item.getPrice())
                                        .quantity(item.getQuantity())
                                        .build()
                                )
                                .collect(Collectors.toList())
                )
                .build();
    }
}