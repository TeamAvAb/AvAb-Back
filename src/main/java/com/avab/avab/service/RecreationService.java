package com.avab.avab.service;

import java.util.List;

import com.avab.avab.dto.recreation.RecreationResponseDTO.DescriptionDTO;
import com.avab.avab.dto.recreation.RecreationResponseDTO.PopularRecreationListDTO;

public interface RecreationService {
    List<PopularRecreationListDTO> getTop3RecreationsByViewCount();

    DescriptionDTO getRecreationDescription(Long recreationId);
}
