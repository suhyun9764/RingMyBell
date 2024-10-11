package com.ringmabell.whichme_backend.entitiy.dispatch;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "차량 종류")
public enum VehicleType {
    @Schema(description = "0: 응급 차량")
    EMERGENCY,

    @Schema(description = "1: 소방차량")
    FIRE,

    @Schema(description = "2: 경찰 차량")
    POLICE,

    @Schema(description = "3: 법정 차량")
    COURT,

    @Schema(description = "4: 후송차량")
    EVACUATION,

    @Schema(description = "5: 기타")
    ETC
}
