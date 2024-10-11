package com.ringmabell.whichme_backend.dto;

import com.ringmabell.whichme_backend.entitiy.dispatch.SubUnit;

import lombok.Getter;

@Getter
public class SubunitDto {
	private Long subunitId;
	private String subunitName;

	public SubunitDto(SubUnit subUnit) {
		this.subunitId = subUnit.getId();
		this.subunitName = subUnit.getName();
	}
}
