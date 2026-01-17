package com.planai.model.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.planai.model.enums.PriorityEnum;
import com.planai.model.enums.StatusEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStoryResponse {

    private Long id;

    private String title;

    @JsonProperty("as_a")
    private String asA;

    @JsonProperty("i_want")
    private String iWant;

    @JsonProperty("so_that")
    private String soThat;

    private PriorityEnum priority;

    private StatusEnum status;

    private Integer orderIndex;

    private List<TaskResponse> tasks;
}
