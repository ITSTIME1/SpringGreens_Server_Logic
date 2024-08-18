package com.spring_greens.logic.global.enums;

import lombok.Getter;

@Getter
public enum Mall {
    APM( "apm"),
    DONGPYEONGHWA("동평화시장"),
    CHEONGPYEONGHWA("청평화시장");
    private final String mallName;
    Mall(String mallName) {
        this.mallName = mallName;
    }
}
