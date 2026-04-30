package com.example.JustEat.dto.response;

import com.example.JustEat.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class OrderResponse {

    private UUID publicId;
    private String restaurantName;
    private Double totalAmount;
    private OrderStatus status;
    private List<OrderItemResponse> items;
}