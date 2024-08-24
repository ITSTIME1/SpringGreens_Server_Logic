package com.spring_greens.logic.scheduling.redis.dto.projection;

public interface RedisProductProjection {
    // Shop 관련 필드
    Long getShopId();
    String getShopName();
    String getShopContact();
    String getShopAddressDetails();

    // Product 관련 필드
    Long getProductId();
    String getProductName();
    String getProductUnit();
    Integer getProductPrice();
}
