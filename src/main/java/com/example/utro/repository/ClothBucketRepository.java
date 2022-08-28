package com.example.utro.repository;

import com.example.utro.entity.ClothBucket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClothBucketRepository extends JpaRepository<ClothBucket,Long> {

    Optional<List<ClothBucket>> findAllByUserId(Long userId);

    Optional<ClothBucket> findByIdAndUserId(Long id,Long userId);

    Optional<ClothBucket> findByClothIdAndUserId(UUID clothId, Long userId);

    Optional<ClothBucket> findByClothId(UUID clothId);

}
