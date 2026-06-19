package com.example.aleksandr_rozkov_6020_pz2.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "suggestions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Suggestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long manulId;
    private String type;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String status;
    private String createdAt;
}
