package com.sasidharanmart.shop.controller;

import com.sasidharanmart.shop.model.User;
import com.sasidharanmart.shop.service.CartService;
import com.sasidharanmart.shop.service.CurrentUserService;
import com.sasidharanmart.shop.service.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Logged-in-only routes: view the cart, change quantities, remove items,
 * and check out (which hands off to OrderService).
 */
@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final OrderService orderService;
    private final CurrentUserService currentUserService;

    public CartController(CartService cartService, OrderService orderService,
                           CurrentUserService currentUserService) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.currentUserService = currentUserService;
    }

    @GetMapping
    public String viewCart(Authentication authentication, Model model) {
        User user = currentUserService.getCurrentUser(authentication);
        model.addAttribute("cartItems", cartService.getCartItems(user));
        model.addAttribute("total", cartService.getCartTotal(user));
        model.addAttribute("user", user);
        return "cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
                             @RequestParam(defaultValue = "1") int quantity,
                             Authentication authentication) {
        User user = currentUserService.getCurrentUser(authentication);
        cartService.addToCart(user, productId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateQuantity(@RequestParam Long cartItemId,
                                  @RequestParam int quantity) {
        cartService.updateQuantity(cartItemId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String removeItem(@RequestParam Long cartItemId) {
        cartService.removeItem(cartItemId);
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String checkout(@RequestParam String deliveryAddress,
                            Authentication authentication,
                            Model model) {
        User user = currentUserService.getCurrentUser(authentication);
        try {
            orderService.checkout(user, deliveryAddress);
            return "redirect:/orders?success=true";
        } catch (IllegalStateException ex) {
            model.addAttribute("cartItems", cartService.getCartItems(user));
            model.addAttribute("total", cartService.getCartTotal(user));
            model.addAttribute("checkoutError", ex.getMessage());
            return "cart";
        }
    }
}
