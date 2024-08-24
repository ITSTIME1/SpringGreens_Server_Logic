package com.spring_greens.logic.scheduling.fcm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TopicDetails {
    private String title;
    private String body;
    private String imagePath;
}
