package com.avab.avab.auth.principal;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.avab.avab.apiPayload.code.status.ErrorStatus;
import com.avab.avab.domain.User;
import com.avab.avab.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user =
                userRepository
                        .findById(Long.parseLong(userId))
                        .orElseThrow(
                                () ->
                                        new UsernameNotFoundException(
                                                ErrorStatus._USER_NOT_FOUND.getMessage()));

        return new PrincipalDetails(user);
    }
}
