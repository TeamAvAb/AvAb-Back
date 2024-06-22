package com.avab.avab.scheduler.service;

import com.avab.avab.domain.User;
import com.avab.avab.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SchedulerService {

    @Value("${schedule.use}")
    private Boolean useSchedule;

    private final UserRepository userRepository;

    @Transactional
    @Scheduled(cron = "${schedule.cron}")
    public void hardDeleteUser() {
        log.info("user hard delete 작동");
        if (useSchedule) {
            LocalDate threshold = LocalDate.now().minusDays(30);
            Optional<List<User>> userList = userRepository.findOldUsers(threshold);
            userList.ifPresent(userRepository::deleteAll);
        }
    }
}
