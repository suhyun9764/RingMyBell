package com.ringmabell.whichme_backend.entitiy.user;

import lombok.Getter;

@Getter
public enum Disease {
    HEART_DISEASE("심장병"),
    ANEMIA("빈혈"),
    OTOLITHIASIS("이석증"),
    EYE_DISEASE("눈 관련 질환"),
    HIGH_BLOOD_PRESSURE("고혈압"),
    LOW_BLOOD_PRESSURE("저혈압");

    private final String korean;

    Disease(String korean) {
        this.korean = korean;
    }


}
