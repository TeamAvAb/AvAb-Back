package com.avab.avab.apiPayload.exception;

import com.avab.avab.apiPayload.code.BaseErrorCode;

public class UserException extends GeneralException {

    public UserException(BaseErrorCode code) {
        super(code);
    }
}
