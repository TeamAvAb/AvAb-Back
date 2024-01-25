package com.avab.avab.batch;

import java.time.LocalDateTime;

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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfig {

    private final JobLauncher jobLauncher;
    private final Job updateRecreationViewCountJob;

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
}
