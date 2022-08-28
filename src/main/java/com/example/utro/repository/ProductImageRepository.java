package com.example.utro.repository;

import com.example.utro.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage,Long> {
    Optional<List<ProductImage>> findAllByProductId(UUID productId);
}
