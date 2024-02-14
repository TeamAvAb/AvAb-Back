package com.avab.avab.dto.reqeust;

import java.util.List;

import jakarta.validation.constraints.Size;

import org.springframework.lang.Nullable;

import com.avab.avab.domain.enums.Age;
import com.avab.avab.domain.enums.Gender;
import com.avab.avab.domain.enums.Keyword;
import com.avab.avab.domain.enums.Purpose;
import com.avab.avab.validation.annotation.ExistRecreation;

import lombok.Getter;
import lombok.Setter;

public class FlowRequestDTO {

    @Getter
    @Setter
    public static class PostFlowDTO {

        String title;
        List<RecreationSpec> recreationSpecList;
        Integer totalPlayTime;
        Integer participants;
        List<Age> ageList;
        List<Purpose> purposeList;
        List<Keyword> keywordList;
        List<Gender> genderList;
    }

    @Getter
    @Setter
    public static class RecreationSpec {

        Integer seq;
        @Nullable @ExistRecreation Long recreationId;

        @Nullable String customTitle;
        @Nullable Integer customPlayTime;

        @Size(min = 1, message = "키워드는 하나라도 존재해야합니다.")
        List<Keyword> customKeywordList;
    }
}
