package com.sasidharanmart.shop.repository;

import com.sasidharanmart.shop.model.Order;
import com.sasidharanmart.shop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserOrderByOrderDateDesc(User user);
}
