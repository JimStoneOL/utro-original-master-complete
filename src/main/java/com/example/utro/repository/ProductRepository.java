package com.example.utro.repository;



import com.example.utro.entity.Product;
import com.example.utro.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
     Optional<Product> findByArticleAndUser(UUID article, User user);

     Optional<List<Product>> findAllByUser(User user);

}