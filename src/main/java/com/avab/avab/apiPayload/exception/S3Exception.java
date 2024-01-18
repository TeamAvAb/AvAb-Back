package com.avab.avab.apiPayload.exception;

import com.avab.avab.apiPayload.code.BaseErrorCode;

public class S3Exception extends GeneralException {

    public S3Exception(BaseErrorCode code) {
        super(code);
    }
}
