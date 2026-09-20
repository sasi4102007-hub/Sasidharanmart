package com.sasidharanmart.shop.repository;

import com.sasidharanmart.shop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository / DAO layer for User.
 * Spring Data JPA generates the implementation at runtime —
 * no SQL needs to be written by hand.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
