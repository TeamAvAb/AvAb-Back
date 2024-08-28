package com.avab.avab.service;

import com.avab.avab.domain.Report;
import com.avab.avab.domain.User;
import com.avab.avab.dto.reqeust.ReportRequestDTO.ReportFlowRequestDTO;
import com.avab.avab.dto.reqeust.ReportRequestDTO.ReportRecreationRequestDTO;
import com.avab.avab.dto.reqeust.ReportRequestDTO.ReportRecreationReviewDTO;

public interface ReportService {

    Report reportRecreation(User user, ReportRecreationRequestDTO request);

    Report reportFlow(User user, ReportFlowRequestDTO request);

    Report reportRecreationReview(User user, ReportRecreationReviewDTO request);
}
