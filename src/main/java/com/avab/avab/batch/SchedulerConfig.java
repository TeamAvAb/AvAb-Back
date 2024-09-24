package com.avab.avab.batch;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.avab.avab.redis.service.FlowViewCountService;
import com.avab.avab.redis.service.RecreationViewCountService;
import com.avab.avab.service.FlowService;
import com.avab.avab.service.RecreationService;
import com.avab.avab.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfig {

    private final FlowViewCountService flowViewCountService;
    private final RecreationViewCountService recreationViewCountService;
    private final FlowService flowService;
    private final RecreationService recreationService;
    private final UserService userService;

    // 매 30분 마다 수행
    @Scheduled(cron = "0 */30 * * * *")
    public void updateRecreationViewCount() {
        log.info("레크레이션 조회수 업데이트 시작");

        Map<Long, Long> viewCountsMap = recreationViewCountService.getTargetRecreationsViewCounts();

        log.info(
                "업데이트 대상 레크레이션: {}",
                viewCountsMap.keySet().stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(", ")));

        viewCountsMap.forEach(recreationService::incrementViewCountById);

        log.info("레크레이션 조회수 업데이트 완료");
    }

    @Scheduled(cron = "0 */30 * * * *")
    public void updateFlowViewCount() {
        log.info("플로우 조회수 업데이트 시작");

        Map<Long, Long> viewCountsMap = flowViewCountService.getTargetFlowsViewCounts();

        log.info(
                "업데이트 대상 플로우: {}",
                viewCountsMap.keySet().stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(", ")));

        viewCountsMap.forEach(flowService::incrementViewCountById);

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

    @Scheduled(cron = "0 0 0,8,16 * * *")
    public void updateRecreationViewCountLast7Days() {
        log.info("레크레이션 7일간 조회수 업데이트 시작");

        List<Long> recreationIdList =
                recreationViewCountService.getAllFlowIdsToUpdateViewCountLast7Days();
        log.info(
                "업데이트 대상 레크레이션: {}",
                recreationIdList.stream().map(Object::toString).collect(Collectors.joining(", ")));

        recreationIdList.parallelStream()
                .forEach(
                        id -> {
                            Long viewCount =
                                    recreationViewCountService.getTotalViewCountLast7Days(id);
                            recreationService.updateFlowViewCountLast7Days(id, viewCount);
                        });

        log.info("레크레이션 7일간 조회수 업데이트 완료");
    }
}
