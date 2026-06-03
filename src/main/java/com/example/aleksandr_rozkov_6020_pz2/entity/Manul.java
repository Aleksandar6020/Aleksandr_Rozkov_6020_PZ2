package com.example.aleksandr_rozkov_6020_pz2.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Manul {

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