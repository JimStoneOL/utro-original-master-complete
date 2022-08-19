package com.example.utro.repository;

import com.example.utro.entity.Cloth;
import com.example.utro.entity.ClothWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClothWarehouseRepository extends JpaRepository<ClothWarehouse,Long> {
    Optional<List<ClothWarehouse>> findAllByCloth(Cloth cloth);
}