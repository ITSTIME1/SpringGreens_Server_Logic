package com.spring_greens.logic.extraction.controller;

import com.spring_greens.logic.global.controller.AbstractBaseController;
import com.spring_greens.logic.global.factory.converter.ifs.ConverterFactory;
import com.spring_greens.logic.global.factory.service.ifs.ServiceFactory;

public class ExtractionConroller extends AbstractBaseController{

    public ExtractionConroller(ConverterFactory converterFactory, ServiceFactory serviceFactory) {
        super(converterFactory, serviceFactory);
    }
    
}
