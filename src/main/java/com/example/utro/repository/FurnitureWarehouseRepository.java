package com.example.utro.repository;

import com.example.utro.entity.Furniture;
import com.example.utro.entity.FurnitureWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FurnitureWarehouseRepository extends JpaRepository<FurnitureWarehouse,Long> {
    Optional<List<FurnitureWarehouse>> findAllByFurniture(Furniture furniture);
}