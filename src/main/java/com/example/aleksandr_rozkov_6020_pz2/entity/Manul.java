package com.example.aleksandr_rozkov_6020_pz2.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "manuls")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Manul {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String photoUrl;

    @Column(length = 1000)
    private String shortDescription;

    @Column(columnDefinition = "TEXT")
    private String longStory;

    private String locationType;
    private Long zooId;
    private String region;
    private Integer likesCount = 0;
    private Integer favoritesCount = 0;
    private String createdAt;
}
