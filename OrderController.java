package com.sasidharanmart.shop.controller;

import com.sasidharanmart.shop.model.User;
import com.sasidharanmart.shop.service.CurrentUserService;
import com.sasidharanmart.shop.service.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final CurrentUserService currentUserService;

    public OrderController(OrderService orderService, CurrentUserService currentUserService) {
        this.orderService = orderService;
        this.currentUserService = currentUserService;
    }

    @GetMapping("/orders")
    public String orderHistory(Authentication authentication,
                                @RequestParam(required = false) Boolean success,
                                Model model) {
        User user = currentUserService.getCurrentUser(authentication);
        model.addAttribute("orders", orderService.getOrderHistory(user));
        model.addAttribute("success", success);
        return "orders";
    }
}
