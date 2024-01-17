package com.avab.avab.apiPayload.exception.auth;

import com.avab.avab.apiPayload.code.BaseErrorCode;
import com.avab.avab.apiPayload.exception.GeneralException;

public class AuthException extends GeneralException {

    public AuthException(BaseErrorCode code) {
        super(code);
    }
}
