package com.avab.avab.service;

import com.avab.avab.domain.Report;
import com.avab.avab.domain.User;
import com.avab.avab.dto.reqeust.ReportRequestDTO.ReportRecreationRequestDTO;

public interface ReportService {

    Report reportRecreation(User user, ReportRecreationRequestDTO request);
}
