package com.avab.avab.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.avab.avab.domain.Flow;
import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.Age;
import com.avab.avab.domain.enums.Gender;
import com.avab.avab.domain.enums.Keyword;
import com.avab.avab.domain.enums.Purpose;
import com.avab.avab.dto.reqeust.FlowRequestDTO.PostFlowDTO;

public interface FlowService {

    Page<Flow> getFlows(Integer page);

    Flow postFlow(PostFlowDTO request, User user);

    Flow getFlowDetail(Long flowId);

    Boolean existsByFlowId(Long flowId);

    List<Long> getUpdateTargetFlowIds(List<Long> flowIdList);

    void updateFlowViewCount(Long flowId, Long viewCount);

    Boolean toggleScrapeFlow(User user, Long flowId);

    void deleteFlow(Long flowId, User user);

    List<Flow> recommendFlows(
            List<Keyword> keywords,
            Integer participants,
            Integer totalPlayTime,
            List<Purpose> purposes,
            List<Gender> genders,
            List<Age> ages);

    Flow updateFlow(PostFlowDTO request, User user, Long flowId);
}
