package com.example.JustEat.service;

import com.example.JustEat.entity.Order;
import java.util.List;
import java.util.UUID;
import com.example.JustEat.dto.response.OrderResponse;

public interface OrderService {
    Order placeOrder();
    List<OrderResponse> getOrderHistory();
    void reorder(UUID publicId);
}