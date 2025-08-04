package com.courseManagement.dto.course;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CourseRequestDto {
    private String name;
    private String createdBy;
}
