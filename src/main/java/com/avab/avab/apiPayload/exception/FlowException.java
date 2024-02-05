package com.avab.avab.apiPayload.exception;

import com.avab.avab.apiPayload.code.BaseErrorCode;

public class FlowException extends GeneralException {

    public FlowException(BaseErrorCode code) {
        super(code);
    }
}
