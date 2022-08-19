package com.example.utro.repository;


import com.example.utro.entity.Furniture;
import com.example.utro.entity.FurnitureProduct;
import com.example.utro.entity.Product;
import com.example.utro.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FurnitureProductRepository extends JpaRepository<FurnitureProduct,Long> {
    Optional<List<FurnitureProduct>> findAllByUser(User user);

    Optional<FurnitureProduct> findByIdAndUser(Long id, User user);

    Optional<List<FurnitureProduct>> findAllByProduct(Product product);

    Optional<List<FurnitureProduct>> findAllByProductAndUser(Product product, User user);
}