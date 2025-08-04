package com.courseManagement.services;

import com.courseManagement.dto.course.CourseCreationRequestDto;
import com.courseManagement.dto.course.CourseRequestDto;
import com.courseManagement.dto.course.CourseResponseDto;
import com.courseManagement.dto.course.CourseUpdationRequestDto;
import com.courseManagement.entities.CourseEntity;
import com.courseManagement.repositories.ICourseRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final ICourseRepo courseRepo;
    private final ModelMapper modelMapper;
    private final Logger logger = LoggerFactory.getLogger(CourseService.class);

    public List<CourseResponseDto> getCourses(CourseRequestDto courseRequestDto) {
        List<CourseEntity> courses;

        if (courseRequestDto.getName() != null) {
            courses = courseRepo.findByName(courseRequestDto.getName());
        } else if (courseRequestDto.getCreatedBy() != null) {
            courses = courseRepo.findByCreatedBy(courseRequestDto.getCreatedBy());
        } else {
            courses = courseRepo.findAll();
        }

        if (courses.isEmpty()) {
            logger.error("Courses not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Courses not found");
        }

        return courses
                .stream()
                .map((course) -> modelMapper.map(course, CourseResponseDto.class))
                .toList();
    }

    public CourseResponseDto getCourseById(int id) {
        Optional<CourseEntity> courseEntity = courseRepo.findById(id);
        if (courseEntity.isEmpty()) {
            logger.error("Course not found");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
        }

        return modelMapper.map(courseEntity.get(), CourseResponseDto.class);
    }

    public int createCourseData(CourseCreationRequestDto courseCreationRequestDto) {
        CourseEntity courseEntity = courseRepo.save(modelMapper.map(courseCreationRequestDto, CourseEntity.class));
        return courseEntity.getId();
    }

    public int updateCourseData(CourseUpdationRequestDto courseUpdationRequestDto) {
        Optional<CourseEntity> isCourseExists = courseRepo.findById(courseUpdationRequestDto.getId());

        if (isCourseExists.isEmpty()) {
            logger.error("Course not found with courseId {}", courseUpdationRequestDto.getId());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
        }

        CourseEntity course = isCourseExists.get();

        if (courseUpdationRequestDto.getName() != null) {
            logger.info("Update field {}", courseUpdationRequestDto.getName());
            course.setName(courseUpdationRequestDto.getName());
        }

        if (courseUpdationRequestDto.getDescription() != null) {
            logger.info("Update field {}", courseUpdationRequestDto.getDescription());
            course.setDescription(courseUpdationRequestDto.getDescription());
        }

        if (courseUpdationRequestDto.getDuration() != -1) {
            logger.info("Update field {}", courseUpdationRequestDto.getDuration());
            course.setDuration(courseUpdationRequestDto.getDuration());
        }

        if (courseUpdationRequestDto.getPrice() != -1) {
            logger.info("Update field {}", courseUpdationRequestDto.getPrice());
            course.setPrice(courseUpdationRequestDto.getPrice());
        }

        course = courseRepo.save(course);

        return course.getId();
    }

    public void deleteCourseData(int id) {
        Optional<CourseEntity> isCourseExists = courseRepo.findById(id);

        if (isCourseExists.isEmpty()) {
            logger.error("Course not found with courseId {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found");
        }

        courseRepo.deleteById(id);
    }
}
