package com.example.utro.entity;

import com.example.utro.entity.enums.EmailStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    private String whomUserEmail;
    private String fromUserEmail;
    @JsonIgnore
    private EmailStatus status;
    @Column(nullable = false)
    private String heading;
    @Column(nullable = false,columnDefinition = "text")
    private String message;
    @Column(updatable = false)
    @JsonFormat(pattern = "dd-mm-yyyy ss:mm:HH")
    private LocalDateTime createdDate;
    @PrePersist
    protected void onCreate()
    {
        this.createdDate = LocalDateTime.now();
    }
}
