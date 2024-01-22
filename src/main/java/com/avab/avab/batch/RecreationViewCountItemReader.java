package com.avab.avab.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import com.avab.avab.domain.Recreation;
import com.avab.avab.redis.service.RecreationViewCountService;
import com.avab.avab.repository.RecreationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecreationViewCountItemReader implements ItemReader<Recreation> {

    private final RecreationViewCountService recreationViewCountService;
    private final RecreationRepository recreationRepository;
    private List<Recreation> items;

    @Override
    public Recreation read() {
        if (items == null) {
            items =
                    new ArrayList<>(
                            recreationViewCountService.getAllRecreationIds().stream()
                                    .map(recreationRepository::findById)
                                    .filter(Optional::isPresent)
                                    .map(Optional::get)
                                    .toList());

            log.info("조회수 업데이트 대상 레크레이션 ID: {}", items.stream().map(Recreation::getId).toList());
        }

        if (!items.isEmpty()) {
            return items.remove(0);
        }
        items = null;
        return null;
    }
}
