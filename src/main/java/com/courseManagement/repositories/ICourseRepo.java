package com.courseManagement.repositories;

import com.courseManagement.entities.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICourseRepo extends JpaRepository<CourseEntity, Integer> {
    List<CourseEntity> findByName(String name);

    List<CourseEntity> findByCreatedBy(String name);
}
