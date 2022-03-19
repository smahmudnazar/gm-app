package com.example.gmrestapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DTOClientAutoShop {
    private String name;
    private String gmName;
    private List<String> carNames;
    private String home;
    private String street;
    private String city;
}
