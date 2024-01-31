package com.avab.avab.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avab.avab.apiPayload.code.status.ErrorStatus;
import com.avab.avab.apiPayload.exception.UserException;
import com.avab.avab.domain.Flow;
import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.User;
import com.avab.avab.domain.mapping.RecreationFavorite;
import com.avab.avab.repository.FlowRepository;
import com.avab.avab.repository.RecreationFavoriteRepository;
import com.avab.avab.repository.UserRepository;
import com.avab.avab.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RecreationFavoriteRepository recreationFavoriteRepository;
    private final FlowRepository flowRepository;

    private final Integer MY_FLOWS_PAGE_SIZE = 6;

    @Override
    public User findUserById(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new UserException(ErrorStatus.USER_NOT_FOUND));
    }

    public Page<Recreation> getFavoriteRecreations(User user, Integer page) {
        Page<RecreationFavorite> favoritesPage =
                recreationFavoriteRepository.findByUser(user, PageRequest.of(page, 6));

        return favoritesPage.map(RecreationFavorite::getRecreation);
    }

    public User updateUserName(String username, User user) {

        user.updateUserName(username);
        return user;
    }

    @Override
    public Page<Flow> getMyFlows(User user, Integer page) {
        return flowRepository.findAllByAuthorOrderByCreatedAtDesc(
                user, PageRequest.of(page, MY_FLOWS_PAGE_SIZE));
    }
}
