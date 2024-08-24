package com.spring_greens.logic.scheduling.redis.dto;


import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder(toBuilder = true)
public class ScheduledRedisProduct {
    private Long mall_id;
    private String mall_name;
    private List<Shop> shop_list;

    @Getter
    @Builder(toBuilder = true)
    public static class Shop {
        private Long shop_id;
        private String shop_name;
        private String shop_contact;
        private String shop_address_details;

        private List<RedisProduct> product;

        @Getter
        @Builder(toBuilder = true)
        public static class RedisProduct {
            private Long product_id;
            private String product_name;
            private String product_unit;
            private String product_image_url;
            private Integer product_price;
        }
    }
}

