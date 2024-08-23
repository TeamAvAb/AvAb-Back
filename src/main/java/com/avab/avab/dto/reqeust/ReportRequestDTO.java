package com.avab.avab.dto.reqeust;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.avab.avab.domain.enums.ReportReason;
import com.avab.avab.validation.annotation.ExistRecreation;

import lombok.Getter;
import lombok.Setter;

public class ReportRequestDTO {

    @Getter
    @Setter
    public static class ReportRecreationRequestDTO {

        @NotNull(message = "신고할 레크리에이션 ID는 필수입니다.")
        @ExistRecreation
        private Long recreationId;

        @NotNull(message = "신고 사유는 필수입니다.")
        private ReportReason reason;

        @Size(max = 150, message = "150자 이하로 입력해주세요.")
        private String extraReason;
    }
}
