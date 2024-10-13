package com.ringmabell.whichme_backend.dto;

import java.util.List;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DispatchPositionDto {
	private LocationDto currentLocation;
	private List<LocationDto> predictedRoute;
}
