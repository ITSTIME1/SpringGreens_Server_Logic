package com.spring_greens.logic.scheduling.product.dto.projection;


public interface ExtractedProducts {
    long getId();
    Integer getDailyAdImpressions();

    Integer getDetailsProductClickCount();

    Integer getTotalViewers();
}
