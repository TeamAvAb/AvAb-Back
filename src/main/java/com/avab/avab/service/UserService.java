package com.avab.avab.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;

import com.avab.avab.domain.Flow;
import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.User;
import com.avab.avab.dto.reqeust.UserRequestDTO.UpdateUserDTO;

public interface UserService {

    User findUserById(Long userId);

    Page<Recreation> getFavoriteRecreations(User user, Integer page);

    User updateUser(UpdateUserDTO request, User user);

    Page<Flow> getMyFlows(User user, Integer page);

    Page<Flow> getScrapFlows(User user, Integer page);

    User deleteUser(User user);

    void hardDeleteOldUser(LocalDate threshold);
}
