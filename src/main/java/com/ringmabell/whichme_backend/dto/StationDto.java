package com.ringmabell.whichme_backend.dto;

import java.util.List;

import com.ringmabell.whichme_backend.entitiy.dispatch.Station;

import lombok.Getter;

@Getter
public class StationDto {
	private Long stationId;
	private String stationName;
	private List<SubunitDto> subunits;

	public StationDto(Station station, List<SubunitDto> subunits) {
		this.stationId = station.getId();
		this.stationName = station.getName();
		this.subunits = subunits;
	}
}
