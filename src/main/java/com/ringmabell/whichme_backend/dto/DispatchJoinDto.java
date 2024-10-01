package com.ringmabell.whichme_backend.dto;

import com.ringmabell.whichme_backend.entitiy.dispatch.SubUnit;

import lombok.Getter;

@Getter
public class DispatchJoinDto {
	private String vehicleNumber;
	private Long subUnit;
}
