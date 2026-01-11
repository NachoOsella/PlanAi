package com.planai.mapper;

import java.util.Collections;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.planai.model.dto.request.CreateProjectRequest;
import com.planai.model.dto.request.UpdateProjectRequest;
import com.planai.model.dto.response.ProjectDetailResponse;
import com.planai.model.dto.response.ProjectResponse;
import com.planai.model.entity.ProjectEntity;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProjectMapper {

    private final ModelMapper modelMapper;
    private final EpicMapper epicMapper;

    public ProjectEntity toEntity(CreateProjectRequest request) {
        return modelMapper.map(request, ProjectEntity.class);
    }

    public void updateEntity(UpdateProjectRequest request, ProjectEntity entity) {
        modelMapper.map(request, entity);
    }

    public ProjectResponse toResponse(ProjectEntity entity) {
        ProjectResponse response = modelMapper.map(entity, ProjectResponse.class);

        long epicsCount = 0L;
        long storiesCount = 0L;
        long tasksCount = 0L;
        if (entity.getEpics() != null) {
            epicsCount = entity.getEpics().size();
            storiesCount = entity.getEpics().stream()
                    .mapToLong(epic -> epic.getStories() == null ? 0L : epic.getStories().size())
                    .sum();
            tasksCount = entity.getEpics().stream()
                    .mapToLong(epic -> {
                        if (epic.getStories() == null) {
                            return 0L;
                        }
                        return epic.getStories().stream()
                                .mapToLong(story -> story.getTasks() == null ? 0L : story.getTasks().size())
                                .sum();
                    })
                    .sum();
        }

        long conversationsCount = entity.getConversations() == null ? 0L : entity.getConversations().size();

        response.setEpicsCount(epicsCount);
        response.setStoriesCount(storiesCount);
        response.setTasksCount(tasksCount);
        response.setConversationsCount(conversationsCount);
        return response;
    }

    public ProjectDetailResponse toDetailResponse(ProjectEntity entity) {
        ProjectDetailResponse response = modelMapper.map(entity, ProjectDetailResponse.class);
        
        if (entity.getEpics() != null) {
            response.setEpics(entity.getEpics().stream()
                    .map(epicMapper::toResponse)
                    .collect(Collectors.toList()));
        } else {
            response.setEpics(Collections.emptyList());
        }
        
        return response;
    }
}
