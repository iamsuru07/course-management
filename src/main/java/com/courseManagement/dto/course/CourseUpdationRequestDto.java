package com.courseManagement.dto.course;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourseUpdationRequestDto {
    @NotNull(message = "id is required")
    private int id;
    private String name;
    private String description;
    private int duration = -1;
    private int price = -1;
}
