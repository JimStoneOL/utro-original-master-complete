package com.example.utro.repository;

import com.example.utro.entity.Order;
import com.example.utro.entity.OrderedProduct;
import com.example.utro.entity.Product;
import com.example.utro.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderedProductRepository extends JpaRepository<OrderedProduct,Long> {
    Optional<OrderedProduct> findByIdAndUser(Long id, User user);

    Optional<List<OrderedProduct>> findAllByUser(User user);

    Optional<List<OrderedProduct>> findAllByOrder(Order order);

    Optional<List<OrderedProduct>> findAllByOrderAndUser(Order order, User user);
    //Optional<OrderedProduct> findByOrderAndProducts(Order order, List<Product> products);
}