package com.ringmabell.whichme_backend.entitiy.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public enum Disease {
    @Schema(description = "0: 심장병")
    HEART_DISEASE("심장병"),

    @Schema(description = "1: 빈혈")
    ANEMIA("빈혈"),

    @Schema(description = "2: 이석증")
    OTOLITHIASIS("이석증"),

    @Schema(description = "3: 눈 관련 질환")
    EYE_DISEASE("눈 관련 질환"),

    @Schema(description = "4: 고혈압")
    HIGH_BLOOD_PRESSURE("고혈압"),

    @Schema(description = "5: 저혈압")
    LOW_BLOOD_PRESSURE("저혈압");

    private final String korean;

    Disease(String korean) {
        this.korean = korean;
    }
}
