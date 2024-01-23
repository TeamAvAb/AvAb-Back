package com.avab.avab.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.avab.avab.domain.Recreation;
import com.avab.avab.redis.service.RecreationViewCountService;
import com.avab.avab.repository.RecreationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class UpdateRecreationViewCountConfig {

    private final RecreationRepository recreationRepository;
    private final RecreationViewCountService recreationViewCountService;
    private final ItemReader<Recreation> itemReader;

    @Bean
    public Job updateRecreationViewCountJob(
            JobRepository jobRepository, Step updateRecreationViewCountFirstStep) {
        return new JobBuilder("Update Recreation View Count Job", jobRepository)
                .start(updateRecreationViewCountFirstStep)
                .build();
    }

    @Bean
    public Step updateRecreationViewCountFirstStep(
            JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("First Step", jobRepository)
                .<Recreation, Recreation>chunk(100, transactionManager)
                .reader(itemReader)
                .processor(itemProcessor())
                .writer(items -> {})
                .build();
    }

    @Bean
    public ItemProcessor<Recreation, Recreation> itemProcessor() {
        return recreation -> {
            Long viewCount = recreationViewCountService.getViewCount(recreation.getId());

            if (viewCount != null) {
                recreationRepository.incrementViewCountById(recreation.getId(), viewCount);
            }

            return recreation;
        };
    }
}
