package com.spring_greens.logic.scheduling.fcm.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FcmReservationMessage {

    private Long id;

    private Long userId;

    private String topicName;

    private String title;

    private String body;

    private String imagePath;

    private LocalDateTime reserveDateTime;

    private Boolean published;

}
