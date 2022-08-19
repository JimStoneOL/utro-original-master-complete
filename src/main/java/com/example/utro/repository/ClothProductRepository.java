package com.example.utro.repository;

import com.example.utro.entity.Cloth;
import com.example.utro.entity.ClothProduct;
import com.example.utro.entity.Product;
import com.example.utro.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClothProductRepository extends JpaRepository<ClothProduct,Long> {
    Optional<List<ClothProduct>> findAllByUser(User user);
    Optional<ClothProduct> findByIdAndUser(Long id, User user);

    Optional<ClothProduct> findByCloth(Cloth cloth);


    Optional<List<ClothProduct>> findAllByProduct(Product product);

    Optional<List<ClothProduct>> findAllByProductAndUser(Product product,User user);

}