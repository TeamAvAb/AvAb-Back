package com.avab.avab.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avab.avab.apiPayload.code.status.ErrorStatus;
import com.avab.avab.apiPayload.exception.AuthException;
import com.avab.avab.apiPayload.exception.UserException;
import com.avab.avab.domain.Flow;
import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.User;
import com.avab.avab.domain.enums.UserStatus;
import com.avab.avab.dto.reqeust.UserRequestDTO.UpdateUserDTO;
import com.avab.avab.repository.FlowRepository;
import com.avab.avab.repository.RecreationRepository;
import com.avab.avab.repository.UserRepository;
import com.avab.avab.security.provider.JwtTokenProvider;
import com.avab.avab.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RecreationRepository recreationRepository;
    private final FlowRepository flowRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private final Integer FAVORITE_SCRAP_PAGE_SIZE = 6;

    @Override
    public User findUserById(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new UserException(ErrorStatus.USER_NOT_FOUND));
    }

    public Page<Recreation> getFavoriteRecreations(User user, Integer page) {

        return recreationRepository.findFavoriteRecreationsByUser(
                user, PageRequest.of(page, FAVORITE_SCRAP_PAGE_SIZE));
    }

    @Override
    @Transactional
    public User updateUser(UpdateUserDTO request, User user) {

        user.setUsername(request.getUsername());
        return user;
    }

    @Override
    public Page<Flow> getScrapFlows(User user, Integer page) {
        return flowRepository.findScrapFlowsByUser(
                user, PageRequest.of(page, FAVORITE_SCRAP_PAGE_SIZE));
    }

    @Override
    @Transactional
    public User deleteUser(User user) {
        if (user.getUserStatus().equals(UserStatus.DELETED)) {
            throw new UserException(ErrorStatus.USER_ALREADY_DELETED);
        }

        user.deleteUser();
        return user;
    }

    @Override
    public Page<Flow> getMyFlows(User user, Integer page) {
        return flowRepository.findAllByAuthorAndDeletedAtIsNullOrderByCreatedAtDesc(
                user, PageRequest.of(page, FAVORITE_SCRAP_PAGE_SIZE));
    }

    @Override
    @Transactional
    public void hardDeleteOldUser(LocalDate threshold) {
        List<User> oldUsers =
                userRepository.findByUserStatusAndDeletedTimeLessThanEqual(
                        UserStatus.DELETED, threshold);
        userRepository.deleteAll(oldUsers);
    }

    @Override
    @Transactional
    public User restoreUserDeletion(String restoreToken) {
        if (!jwtTokenProvider.isRestoreTokenValid(restoreToken)) {
            throw new AuthException(ErrorStatus.AUTH_INVALID_TOKEN);
        }

        Long id = jwtTokenProvider.getId(restoreToken);
        User user =
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new UserException(ErrorStatus.USER_NOT_FOUND));

        if (!user.isDeleted()) {
            throw new UserException(ErrorStatus.USER_NOT_DELETED);
        }

        user.enableUser();
        return user;
    }
}
