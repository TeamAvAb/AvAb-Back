package com.avab.avab.service;

import org.springframework.stereotype.Service;

import com.avab.avab.apiPayload.code.status.ErrorStatus;
import com.avab.avab.apiPayload.exception.handler.TempHandler;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TempQueryServiceImpl implements TempQueryService {

    @Override
    public void CheckFlag(Integer flag) {
        if (flag == 1) throw new TempHandler(ErrorStatus.TEMP_EXCEPTION);
    }
}
