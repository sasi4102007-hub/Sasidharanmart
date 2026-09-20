package com.sasidharanmart.shop.service;

import com.sasidharanmart.shop.model.*;
import com.sasidharanmart.shop.repository.CartItemRepository;
import com.sasidharanmart.shop.repository.OrderRepository;
import com.sasidharanmart.shop.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(CartItemRepository cartItemRepository,
                         OrderRepository orderRepository,
                         ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    /**
     * Converts the user's current cart into a placed Order:
     * snapshots product name/price into OrderItems, decrements stock,
     * and empties the cart. Runs as a single transaction.
     */
    @Transactional
    public Order checkout(User user, String deliveryAddress) {
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cannot check out an empty cart.");
        }

        BigDecimal total = BigDecimal.ZERO;
        Order order = new Order(user, BigDecimal.ZERO, deliveryAddress);

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            if (product.getStockQuantity() < cartItem.getQuantity()) {
                throw new IllegalStateException("Not enough stock for: " + product.getName());
            }

            OrderItem orderItem = new OrderItem(
                    product, product.getName(), product.getPrice(), cartItem.getQuantity());
            order.addItem(orderItem);

            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));

            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);
        }

        order.setTotalAmount(total);
        Order savedOrder = orderRepository.save(order);

        cartItemRepository.deleteByUser(user);

        return savedOrder;
    }

    public List<Order> getOrderHistory(User user) {
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }
}
