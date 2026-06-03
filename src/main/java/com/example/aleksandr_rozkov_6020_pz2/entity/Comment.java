package com.example.aleksandr_rozkov_6020_pz2.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    private Long id;
    private Long userId;
    private Long manulId;
    private String content;
    private String createdAt;

}