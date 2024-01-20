package com.avab.avab.apiPayload.exception;

import com.avab.avab.apiPayload.code.BaseErrorCode;

public class RecreationException extends GeneralException {

    public RecreationException(BaseErrorCode code) {
        super(code);
    }
}
