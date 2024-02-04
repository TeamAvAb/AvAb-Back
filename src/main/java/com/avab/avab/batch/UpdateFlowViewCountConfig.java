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

import com.avab.avab.domain.Flow;
import com.avab.avab.redis.service.FlowViewCountService;
import com.avab.avab.repository.FlowRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class UpdateFlowViewCountConfig {

    private final FlowRepository flowRepository;
    private final FlowViewCountService flowViewCountService;
    private final ItemReader<Flow> itemReader;

    @Bean
    public Job updateFlowViewCountJob(
            JobRepository jobRepository, Step updateFlowViewCountFirstStep) {
        return new JobBuilder("Update Flow View Count Job", jobRepository)
                .start(updateFlowViewCountFirstStep)
                .build();
    }

    @Bean
    public Step updateFlowViewCountFirstStep(
            JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("First Step", jobRepository)
                .<Flow, Flow>chunk(100, transactionManager)
                .reader(itemReader)
                .processor(itemProcessor())
                .writer(items -> {})
                .build();
    }

    @Bean
    public ItemProcessor<Flow, Flow> itemProcessor() {
        return flow -> {
            Long viewCount = flowViewCountService.getViewCount(flow.getId());

            if (viewCount != null) {
                flowRepository.incrementViewCountById(flow.getId(), viewCount);
            }

            return flow;
        };
    }
}
