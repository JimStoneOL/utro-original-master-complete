package com.example.utro.repository;

import com.example.utro.entity.Order;
import com.example.utro.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findUserByEmail(String username);
	Boolean existsByEmail(String email);

}
