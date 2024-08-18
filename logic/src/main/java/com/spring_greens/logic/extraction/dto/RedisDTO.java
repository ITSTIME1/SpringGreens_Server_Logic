package com.spring_greens.logic.extraction.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.spring_greens.logic.global.dto.ShopDTO;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RedisDTO {
    private Long mall_id;
    private String mall_name;
    private List<ShopDTO> shop_list;    
}
