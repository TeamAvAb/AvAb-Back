package com.avab.avab.service;

import java.util.List;

import com.avab.avab.domain.Recreation;
import com.avab.avab.dto.recreation.RecreationResponseDTO.PopularRecreationListDTO;

public interface RecreationService {
    List<PopularRecreationListDTO> getTop3RecreationsByViewCount();

    Recreation getRecreationDescription(Long recreationId);
}
