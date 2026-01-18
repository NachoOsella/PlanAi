package com.planai.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.planai.model.enums.PriorityEnum;
import com.planai.model.enums.StatusEnum;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateStoryRequest {

    @NotBlank(message = "Title must not be blank")
    private String title;

    @JsonProperty("as_a")
    private String asA;

    @JsonProperty("i_want")
    private String iWant;

    @JsonProperty("so_that")
    private String soThat;

    private PriorityEnum priority;

    private StatusEnum status;
}
