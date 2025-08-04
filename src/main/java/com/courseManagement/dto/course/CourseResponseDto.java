package com.courseManagement.dto.course;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CourseResponseDto {
    private int id;
    private String name;
    private String createdBy;
    private String description;
    private int duration;
    private int price;
    private Date createdAt;
}
