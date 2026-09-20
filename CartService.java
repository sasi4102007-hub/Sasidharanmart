package com.sasidharanmart.shop.service;

import com.sasidharanmart.shop.model.CartItem;
import com.sasidharanmart.shop.model.Product;
import com.sasidharanmart.shop.model.User;
import com.sasidharanmart.shop.repository.CartItemRepository;
import com.sasidharanmart.shop.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    public CartService(CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    public List<CartItem> getCartItems(User user) {
        return cartItemRepository.findByUser(user);
    }

    /**
     * Adds a product to the user's cart. If the product is already in the
     * cart, increases the quantity instead of creating a duplicate row.
     */
    public void addToCart(User user, Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        CartItem item = cartItemRepository.findByUserAndProduct(user, product)
                .orElse(new CartItem(user, product, 0));

        item.setQuantity(item.getQuantity() + quantity);
        cartItemRepository.save(item);
    }

    public void updateQuantity(Long cartItemId, int quantity) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found: " + cartItemId));
        if (quantity <= 0) {
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
    }

    public void removeItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    public BigDecimal getCartTotal(User user) {
        return getCartItems(user).stream()
                .map(i -> i.getProduct().getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void clearCart(User user) {
        cartItemRepository.deleteByUser(user);
    }
}
