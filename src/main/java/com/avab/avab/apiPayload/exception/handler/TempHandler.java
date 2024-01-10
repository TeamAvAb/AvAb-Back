package com.avab.avab.apiPayload.exception.handler;

import com.avab.avab.apiPayload.code.BaseErrorCode;
import com.avab.avab.apiPayload.exception.GeneralException;

public class TempHandler extends GeneralException {

    public TempHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
