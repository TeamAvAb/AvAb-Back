package com.avab.avab.apiPayload.exception;

import com.avab.avab.apiPayload.code.BaseErrorCode;

public class AuthException extends GeneralException {

    public AuthException(BaseErrorCode code) {
        super(code);
    }
}
