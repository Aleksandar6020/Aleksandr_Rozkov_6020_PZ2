package com.example.aleksandr_rozkov_6020_pz2.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long manulId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String createdAt;
}
