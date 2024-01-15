package com.avab.avab.apiPayload.exception.S3;

import com.avab.avab.apiPayload.code.BaseErrorCode;
import com.avab.avab.apiPayload.exception.GeneralException;

public class S3UploadException extends GeneralException {
    public S3UploadException(BaseErrorCode code) {
        super(code);
    }
}
