package com.example.aleksandr_rozkov_6020_pz2.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String email;
    private String password;
    private String role;

}