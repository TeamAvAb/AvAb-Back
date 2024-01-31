package com.avab.avab.service;

import org.springframework.data.domain.Page;

import com.avab.avab.domain.Flow;
import com.avab.avab.domain.User;
import com.avab.avab.domain.mapping.FlowFavorite;

public interface FlowService {

    Page<Flow> getFlows(Integer page);

    Page<FlowFavorite> getScrapFlows(User user, Integer page);
}
