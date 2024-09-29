package com.ringmabell.whichme_backend.exception.errorcode;

public class ExpiredTokenException extends RuntimeException {
	public ExpiredTokenException(String message) {
		super(message);
	}
}

