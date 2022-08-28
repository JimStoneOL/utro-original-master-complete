package com.example.utro.repository;

import com.example.utro.entity.Email;
import com.example.utro.entity.enums.EmailStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmailRepository extends JpaRepository<Email,Long> {

    Optional<List<Email>> findAllByWhomUserEmailOrStatusOrderByCreatedDateDesc(String whomUserEmail, EmailStatus status);

    Optional<Email> findByIdAndWhomUserEmail(Long id,String whomUserEmail);
}
