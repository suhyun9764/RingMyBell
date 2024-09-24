package com.ringmabell.whichme_backend.exception;

import org.springframework.dao.DuplicateKeyException;

public class DuplicateUserException extends DuplicateKeyException {
    public DuplicateUserException(String msg) {
        super(msg);
    }
}
