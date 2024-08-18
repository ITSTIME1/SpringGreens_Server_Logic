package com.spring_greens.logic.global.dto;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jdk.jfr.Description;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MallDTO {

    private Long id;
    @Description("상가이름")
    private String name;
    
}
