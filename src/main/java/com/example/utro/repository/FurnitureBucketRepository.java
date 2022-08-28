package com.example.utro.repository;

import com.example.utro.entity.FurnitureBucket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FurnitureBucketRepository extends JpaRepository<FurnitureBucket,Long> {

    Optional<List<FurnitureBucket>> findAllByUserId(Long userId);

    Optional<FurnitureBucket> findByIdAndUserId(Long id,Long userId);

    Optional<FurnitureBucket> findByFurnitureId(UUID furnitureId);

    Optional<FurnitureBucket> findByFurnitureIdAndUserId(UUID furnitureId, Long userId);
}
