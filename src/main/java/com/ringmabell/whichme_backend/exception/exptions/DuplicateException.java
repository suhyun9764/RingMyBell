package com.ringmabell.whichme_backend.exception.exptions;

import org.springframework.dao.DuplicateKeyException;

public class DuplicateException extends DuplicateKeyException {
    public DuplicateException(String msg) {
        super(msg);
    }
}
