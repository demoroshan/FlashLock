package com.flashlock.service;

import com.flashlock.model.Order;
import com.flashlock.model.Product;
import com.flashlock.repository.OrderRepository;
import com.flashlock.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

@Service
public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderService(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    @Retryable(
            retryFor = { ObjectOptimisticLockingFailureException.class },
            maxAttempts = 5,
            backoff = @Backoff(delay = 50, multiplier = 1.5)
    )
    @Transactional
    public Order placeOrder(Long userId, Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

        if (product.getStockQuantity() < quantity) {
            throw new IllegalStateException("Insufficient inventory available for purchase.");
        }

        // Deduct inventory
        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);

        // Record order
        Order order = new Order(userId, productId, quantity);
        return orderRepository.save(order);
    }
}