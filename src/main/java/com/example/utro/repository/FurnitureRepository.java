package com.example.utro.repository;

import com.example.utro.entity.Furniture;
import com.example.utro.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FurnitureRepository extends JpaRepository<Furniture, UUID> {
//    Optional<List<Furniture>> findAllByUser(User user);
//
//    Optional<Furniture> findByIdAndUser(UUID article, User user);
}