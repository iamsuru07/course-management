package com.courseManagement.controllers;

import com.courseManagement.dto.course.CourseCreationRequestDto;
import com.courseManagement.dto.course.CourseRequestDto;
import com.courseManagement.dto.course.CourseResponseDto;
import com.courseManagement.dto.course.CourseUpdationRequestDto;
import com.courseManagement.services.CourseService;
import com.courseManagement.utils.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @GetMapping
    public ResponseEntity<ApiResponse<List<CourseResponseDto>>> fetchCourses(@ModelAttribute CourseRequestDto courseRequestDto) {
        logger.debug("Request received for fetching courses {}", courseRequestDto);
        List<CourseResponseDto> courseResponseDto = courseService.getCourses(courseRequestDto);
        logger.info("Courses found and request fulfilled successfully {}", courseRequestDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Courses fetched successfully", courseResponseDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CourseResponseDto>> fetchCourseById(@PathVariable("id") int id) {
        logger.debug("Request received for fetching course by courseId {}", id);
        CourseResponseDto courseResponseDto = courseService.getCourseById(id);
        logger.info("Course found and request fulfilled successfully for courseId {}", id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Course fetched successfully", courseResponseDto));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createCourse(@RequestBody @Valid CourseCreationRequestDto courseCreationRequestDto) {
        logger.debug("Request received for creating course {}", courseCreationRequestDto);
        int id = courseService.createCourseData(courseCreationRequestDto);
        logger.info("Course created successfully with courseId {}", id);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Course created successfully"));
    }

    @PatchMapping
    public ResponseEntity<ApiResponse<String>> updateCourse(@RequestBody @Valid CourseUpdationRequestDto courseUpdationRequestDto) {
        logger.debug("Request received for updating course {}", courseUpdationRequestDto);
        int id = courseService.updateCourseData(courseUpdationRequestDto);
        logger.info("Course updated successfully with courseId {}", id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Course updated successfully"));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<String>> deleteCourse(@PathVariable("id") int id) {
        logger.debug("Request received for deleting course for courseId {}", id);
        courseService.deleteCourseData(id);
        logger.info("Course deleted successfully with courseId {}", id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("Course deleted successfully"));
    }
}
