package com.spring_greens.logic.scheduling.global.enums;

import lombok.Getter;

public enum MallType {
    APM(2, "apm"),
    DONGPEONG(3, "dong"),
    CHUNGPEONG(4, "chung"),
    JEIL(5, "jeil");

    @Getter
    private final int id;
    @Getter
    private final String name;

    MallType(int id, String name) {
        this.id = id;
        this.name = name;
    }

}