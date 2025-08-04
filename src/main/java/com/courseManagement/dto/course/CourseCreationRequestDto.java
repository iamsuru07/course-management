package com.courseManagement.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CourseCreationRequestDto {
    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "createdBy is required")
    private String createdBy;

    private String description = "";

    @NotNull(message = "duration is required (in days)")
    private int duration;

    private int price = 0;
}
