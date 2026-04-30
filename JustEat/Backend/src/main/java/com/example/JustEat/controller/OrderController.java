package com.example.JustEat.controller;

import com.example.JustEat.entity.Order;
import com.example.JustEat.service.OrderService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;
import com.example.JustEat.dto.response.OrderResponse;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder() {
        return ResponseEntity.ok(orderService.placeOrder());
    }
    @GetMapping("/history")
    public ResponseEntity<List<OrderResponse>> getHistory() {
        return ResponseEntity.ok(orderService.getOrderHistory());
    }
    @PostMapping("/reorder/{publicId}")
    public ResponseEntity<String> reorder(@PathVariable UUID publicId) {
        orderService.reorder(publicId);
        return ResponseEntity.ok("Reordered successfully");
    }
}