package com.spring_greens.logic.extraction.schduler;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.spring_greens.logic.global.factory.service.ifs.ServiceFactory;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExtractionScheduler {
    private final ServiceFactory serviceFactory;

    @Scheduled
    public void extractionProductScheduler(){

        serviceFactory.getExtractionService().ExtractionProductService();
    }

}
