package com.example.aleksandr_rozkov_6020_pz2.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuggestionRequest {
    private Long manulId;
    private String type;
    private String content;
    private String status;
}
