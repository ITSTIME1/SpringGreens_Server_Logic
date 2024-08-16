package com.spring_greens.logic.global.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShopDTO {
    private Long id;
    private Long mallId;
    private String name;
    private String contact;
    private String addressDetails;
}
