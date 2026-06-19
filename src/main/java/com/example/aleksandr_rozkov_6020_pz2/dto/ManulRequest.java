package com.example.aleksandr_rozkov_6020_pz2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManulRequest {
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
