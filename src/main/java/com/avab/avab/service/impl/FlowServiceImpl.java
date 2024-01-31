package com.avab.avab.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avab.avab.domain.Flow;
import com.avab.avab.domain.User;
import com.avab.avab.domain.mapping.FlowFavorite;
import com.avab.avab.repository.FlowFavoriteRepository;
import com.avab.avab.repository.FlowRepository;
import com.avab.avab.service.FlowService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FlowServiceImpl implements FlowService {

    private final FlowRepository flowRepository;
    private final FlowFavoriteRepository flowFavoriteRepository;

    @Override
    public Page<Flow> getFlows(Integer page) {
        return flowRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, 6));
    }

    @Override
    public Page<FlowFavorite> getScrapFlows(User user, Integer page) {
        return flowFavoriteRepository.findByUser(user, PageRequest.of(page, 4));
    }
}
