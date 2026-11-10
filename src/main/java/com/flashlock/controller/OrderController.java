package com.flashlock.controller;

import com.flashlock.model.Order;
import com.flashlock.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/purchase")
    public ResponseEntity<Order> purchase(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") int quantity) {
        Order order = orderService.placeOrder(userId, productId, quantity);
        return ResponseEntity.ok(order);
    }
}