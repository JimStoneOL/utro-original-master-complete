package com.example.utro.repository;

import com.example.utro.entity.Order;
import com.example.utro.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    Optional<Order> findByIdAndUser(UUID id, User user);
    Optional<List<Order>> findAllByIdAndUser(UUID id,User user);

    Optional<List<Order>> findAllByUser(User user);
}