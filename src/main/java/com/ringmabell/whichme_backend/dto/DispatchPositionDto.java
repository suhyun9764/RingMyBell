package com.ringmabell.whichme_backend.dto;

import java.util.List;

import lombok.Getter;

@Getter
public class DispatchPositionDto {
	private LocationDto currentLocation;
	private List<LocationDto> predictedRoute;
}
