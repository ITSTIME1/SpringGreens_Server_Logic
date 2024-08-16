package com.spring_greens.logic.global.dto;

import java.time.LocalDate;

import org.springframework.context.annotation.Description;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDTO {
    private Long id;
    private Long shop_id;
    private String name;
    private String unit;
    private Integer price;
    private Integer totalViewers;
    /*
    private String productImageUrl;
    private String majorCategory;
    private String subCategory;
     */
    private Integer age;
    private Integer detailsProductClickCount;
    private Integer dailyAdImpressions;    
    private Boolean extractableStatus;
    private LocalDate lastExtractDate;

    public void changeAge(Integer age){
        this.age = age;
    }
}
