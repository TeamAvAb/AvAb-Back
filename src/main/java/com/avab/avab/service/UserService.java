package com.avab.avab.service;

import org.springframework.data.domain.Page;

import com.avab.avab.domain.Recreation;
import com.avab.avab.domain.User;
import com.avab.avab.domain.mapping.FlowFavorite;

public interface UserService {

    User findUserById(Long userId);

    Page<Recreation> getFavoriteRecreations(User user, Integer page);

    User updateUserName(String username, User user);

    Page<FlowFavorite> getScrapFlows(User user, Integer page);
}
