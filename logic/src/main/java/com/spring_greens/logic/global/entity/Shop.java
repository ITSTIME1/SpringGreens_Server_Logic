package com.spring_greens.logic.global.entity;

import jakarta.persistence.*;
import jdk.jfr.Description;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;


// @TODO 리팩토링
@Getter
@Builder
@Entity
public class Shop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "mall_id")
    private Long mallId;
    private String contact;
    private String name;
    private String intro;
    @Column(name = "profile_type")
    @Description("프로필 설정 여부")
    private Boolean profileType;
    @Column(name = "road_address")
    @Description("도로명주소")
    private String roadAddress;

    @Column(name = "address_details")
    @Description("상세주소")
    private String addressDetails;

    @Column(name = "start_time")
    @Description("영업시작시간")
    private LocalTime startTime;

    @Column(name = "end_time")
    @Description("영업종료시간")
    private LocalTime endTime;

    @Column(name = "registration_date")
    @Description("가게등록일")
    private LocalDateTime registrationDateTime;

}
