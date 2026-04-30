package com.example.JustEat.controller;

import com.example.JustEat.entity.Order;
import com.example.JustEat.service.OrderService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/place")
    public ResponseEntity<Order> placeOrder() {
        return ResponseEntity.ok(orderService.placeOrder());
    }
}