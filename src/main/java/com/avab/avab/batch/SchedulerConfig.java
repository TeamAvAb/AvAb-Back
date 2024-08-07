package com.avab.avab.batch;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.avab.avab.redis.service.FlowViewCountService;
import com.avab.avab.service.FlowService;
import com.avab.avab.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfig {

    private final JobLauncher jobLauncher;
    private final Job updateRecreationViewCountJob;

    private final FlowViewCountService flowViewCountService;
    private final FlowService flowService;
    private final UserService userService;

    // 매 30분 마다 수행
    @Scheduled(cron = "0 */30 * * * *")
    public void updateRecreationViewCount()
            throws JobInstanceAlreadyCompleteException,
                    JobExecutionAlreadyRunningException,
                    JobParametersInvalidException,
                    JobRestartException {
        JobParameters jobParameters =
                new JobParametersBuilder()
                        .addLocalDateTime("TIMESTAMP", LocalDateTime.now())
                        .toJobParameters();
        jobLauncher.run(updateRecreationViewCountJob, jobParameters);
    }

    @Scheduled(cron = "0 */30 * * * *")
    public void updateFlowViewCount() {
        log.info("플로우 조회수 업데이트 시작");

        List<Long> flowIdList = flowViewCountService.getAllFlowIds();
        List<Long> targetFlowIdList = flowService.getUpdateTargetFlowIds(flowIdList);
        log.info(
                "업데이트 대상 플로우: {}",
                targetFlowIdList.stream().map(Object::toString).collect(Collectors.joining(", ")));

        targetFlowIdList.forEach(
                id -> {
                    Long viewCount = flowViewCountService.getViewCount(id);
                    flowService.updateFlowViewCount(id, viewCount);
                });

        log.info("플로우 조회수 업데이트 완료");
    }

    // 매일 0시, 8시, 16시에 수행
    @Scheduled(cron = "0 0 0,8,16 * * *")
    public void updateFlowViewCountLast7Days() {
        log.info("플로우 7일간 조회수 업데이트 시작");

        List<Long> flowIdList = flowViewCountService.getAllFlowIdsToUpdateViewCountLast7Days();
        log.info(
                "업데이트 대상 플로우: {}",
                flowIdList.stream().map(Object::toString).collect(Collectors.joining(", ")));

        flowIdList.parallelStream()
                .forEach(
                        id -> {
                            Long viewCount = flowViewCountService.getTotalViewCountLast7Days(id);
                            flowService.updateFlowViewCountLast7Days(id, viewCount);
                        });

        log.info("플로우 7일간 조회수 업데이트 완료");
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void hardDeleteUser() {
        log.info("user hard delete 작동");

        LocalDate threshold = LocalDate.now().minusDays(30);
        userService.hardDeleteOldUser(threshold);

        log.info("user hard delete 완료");
    }
}
