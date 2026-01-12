package com.planai.mapper;

import java.util.Collections;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.planai.model.dto.request.CreateEpicRequest;
import com.planai.model.dto.response.EpicResponse;
import com.planai.model.entity.EpicEntity;

import lombok.RequiredArgsConstructor;

/**
 * Mapper component for converting between EpicEntity and DTOs.
 */
@Component
@RequiredArgsConstructor
public class EpicMapper {

    private final ModelMapper modelMapper;
    private final UserStoryMapper userStoryMapper;

    public EpicEntity toEntity(CreateEpicRequest request) {
        return modelMapper.map(request, EpicEntity.class);
    }

    public void updateEntityFromRequest(CreateEpicRequest request, EpicEntity entity) {
        modelMapper.map(request, entity);
    }

    public EpicResponse toResponse(EpicEntity entity) {
        EpicResponse response = modelMapper.map(entity, EpicResponse.class);
        
        if (entity.getStories() != null) {
            response.setStories(entity.getStories().stream()
                    .map(userStoryMapper::toResponse)
                    .collect(Collectors.toList()));
        } else {
            response.setStories(Collections.emptyList());
        }
        
        return response;
    }
}
