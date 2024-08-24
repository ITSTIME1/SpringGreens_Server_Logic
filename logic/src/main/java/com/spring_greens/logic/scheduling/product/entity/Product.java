package com.spring_greens.logic.scheduling.product.entity;


import jakarta.persistence.*;
import jdk.jfr.Description;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "shop_id")
    private Long shopId;

    @Description("상품 이름")
    private String name;

    @Description("상품 주기")
    private int age;

    @Description("상품 가격")
    private Integer price;

    @Description("상품 내용")
    private String content;

    @Column(name = "details_product_click_count")
    @Description("상품상세클릭수")
    private int detailsProductClickCount;


    @Column(name = "daily_ad_impressions")
    @Description("하루광고노출수")
    private int dailyAdImpressions;


    @Column(name = "extractable_status")
    @Description("마지막 추출 상태")
    private Boolean extractableStatus;

    @Description("단위")
    private String unit;

    @Column(name = "stock_status")
    @Description("상품 재고 상태")
    private Boolean stockStatus;

    @Description("평점수")
    private Byte rating;

    @Column(name = "registration_date_time")
    @Description("등록일")
    private LocalDateTime registrationDateTime;

    @Column(name = "last_change_date_time")
    @Description("마지막변경일")
    private LocalDateTime lastChangeDateTime;

    @Column(name = "total_viewers")
    @Description("누적뷰어수")
    private int totalViewers;


    @Column(name = "last_extract_date")
    @Description("마지막 추출 날짜")
    private LocalDate lastExtractDate;
}
