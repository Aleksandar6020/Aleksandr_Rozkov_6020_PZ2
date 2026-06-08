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
    private String name;
    private String photoUrl;
    private String shortDescription;
    private String longStory;
    private String locationType;
    private Long zooId;
    private String region;
    private Integer likesCount;
    private Integer favoritesCount;
    private String createdAt;

}