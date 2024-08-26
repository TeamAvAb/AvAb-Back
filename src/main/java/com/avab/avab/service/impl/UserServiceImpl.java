package com.avab.avab.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avab.avab.apiPayload.code.status.ErrorStatus;
import com.avab.avab.apiPayload.exception.UserException;
import com.avab.avab.domain.Flow;
import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.UserStatus;
import com.avab.avab.domain.mapping.FlowFavorite;
import com.avab.avab.domain.mapping.RecreationFavorite;
import com.avab.avab.dto.reqeust.UserRequestDTO.UpdateUserDTO;
import com.avab.avab.repository.FlowFavoriteRepository;
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
    private final FlowFavoriteRepository flowFavoriteRepository;

    private final Integer FLOWS_PAGE_SIZE = 6;

    @Override
    public User findUserById(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new UserException(ErrorStatus.USER_NOT_FOUND));
    }

    public Page<Recreation> getFavoriteRecreations(User user, Integer page) {
        Page<RecreationFavorite> favoritesPage =
                recreationFavoriteRepository.findByUser(
                        user, PageRequest.of(page, FLOWS_PAGE_SIZE));

        return favoritesPage.map(RecreationFavorite::getRecreation);
    }

    @Override
    @Transactional
    public User updateUser(UpdateUserDTO request, User user) {

        user.setUsername(request.getUsername());
        return user;
    }

    @Override
    public Page<Flow> getScrapFlows(User user, Integer page) {
        return flowFavoriteRepository
                .findByUser(user, PageRequest.of(page, FLOWS_PAGE_SIZE))
                .map(FlowFavorite::getFlow);
    }

    @Override
    @Transactional
    public User deleteUser(User user) {
        if (user.getUserStatus().equals(UserStatus.DELETED)) {
            throw new UserException(ErrorStatus.USER_ALREADY_DELETE);
        }

        user.deleteUser();
        return user;
    }

    @Override
    public Page<Flow> getMyFlows(User user, Integer page) {
        return flowRepository.findAllByAuthorOrderByCreatedAtDesc(
                user, PageRequest.of(page, FLOWS_PAGE_SIZE));
    }

    @Override
    @Transactional
    public void hardDeleteOldUser(LocalDate threshold) {
        Optional<List<User>> userList = userRepository.findOldUsers(threshold);
        userList.ifPresent(userRepository::deleteAll);
    }

    @Override
    @Transactional
    public void enableUser(User user) {
        user.enableUser();
        userRepository.save(user);
    }
}
