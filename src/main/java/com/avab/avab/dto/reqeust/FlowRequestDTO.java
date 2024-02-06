package com.avab.avab.dto.reqeust;

import java.util.List;

import com.avab.avab.domain.enums.Age;
import com.avab.avab.domain.enums.Gender;
import com.avab.avab.domain.enums.Keyword;
import com.avab.avab.domain.enums.Purpose;

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
        Long recreationId;
        Integer customPlayTime;
    }
}
