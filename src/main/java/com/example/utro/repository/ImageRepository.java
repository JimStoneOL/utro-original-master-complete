package com.example.utro.repository;

import com.example.utro.entity.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<ImageModel,Long> {

    Optional<ImageModel> findByUserId(Long userId);

    Optional<ImageModel> findByProductId(UUID productId);

    Optional<ImageModel> findByClothId(UUID clothId);

    Optional<ImageModel> findByFurnitureId(UUID furnitureId);

    Optional<ImageModel> findByOther(String other);
}