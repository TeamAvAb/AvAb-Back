package com.avab.avab.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import com.avab.avab.domain.Flow;
import com.avab.avab.redis.service.FlowViewCountService;
import com.avab.avab.repository.FlowRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FlowViewCountItemReader implements ItemReader<Flow> {

    private final FlowViewCountService flowViewCountService;
    private final FlowRepository flowRepository;
    private List<Flow> items;

    @Override
    public Flow read()
            throws Exception,
                    UnexpectedInputException,
                    ParseException,
                    NonTransientResourceException {
        if (items == null) {
            items =
                    new ArrayList<>(
                            flowViewCountService.getAllFlowIds().stream()
                                    .map(flowRepository::findById)
                                    .filter(Optional::isPresent)
                                    .map(Optional::get)
                                    .toList());
        }

        if (!items.isEmpty()) {
            return items.remove(0);
        }
        items = null;
        return null;
    }
}
