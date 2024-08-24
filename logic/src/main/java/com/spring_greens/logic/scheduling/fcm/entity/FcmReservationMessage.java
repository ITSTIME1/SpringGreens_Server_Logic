package com.spring_greens.logic.scheduling.fcm.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FcmReservationMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "topic_name")
    private String topicName;

    private String title;

    private String body;

    @Column(name = "image_path")
    private String imagePath;

    @Column(name = "reserve_date_time")
    private LocalDateTime reserveDateTime;

    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean published;

}
