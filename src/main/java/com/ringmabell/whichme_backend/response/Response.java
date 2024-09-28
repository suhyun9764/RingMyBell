package com.ringmabell.whichme_backend.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Response {
	private final boolean success;
	private final String message;

}
