package com.avab.avab.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.avab.avab.converter.RecreationConverter;
import com.avab.avab.domain.Recreation;
import com.avab.avab.dto.recreation.RecreationResponseDTO.DescriptionDTO;
import com.avab.avab.dto.recreation.RecreationResponseDTO.PopularRecreationListDTO;
import com.avab.avab.repository.RecreationRepository;
import com.avab.avab.service.RecreationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecreationServiceImpl implements RecreationService {

    private final RecreationRepository recreationRepository;

    public List<PopularRecreationListDTO> getTop3RecreationsByViewCount() {
        List<Recreation> topRecreations =
                recreationRepository.findTop3ByOrderByViewCountDesc(PageRequest.of(0, 3));
        return topRecreations.stream()
                .map(RecreationConverter::convertToDTO)
                .collect(Collectors.toList());
    }

    public DescriptionDTO getRecreationDescription(Long recreationId) {
        Recreation recreation = recreationRepository.findById(recreationId).get();
        return RecreationConverter.toDescriptionDTO(recreation);
    }
}
