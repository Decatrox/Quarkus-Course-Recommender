package com.sumerge.course;

import com.sumerge.exception_handling.custom_exceptions.CourseAlreadyExistsException;
import com.sumerge.exception_handling.custom_exceptions.CourseNotFoundException;
import com.sumerge.mapper.MapStructMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
@AllArgsConstructor
@Slf4j
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;
    private final MapStructMapper mapStructMapper;
    private static final String COURSE_WITH_ID = "Course with id: ";
    private static final String DOES_NOT_EXIST = " does not exist";


    public String addCourse(CoursePostDTO course){
        log.info("CourseService-Add recommended course");
        if (courseRepository.existsByName(course.getName())) {
            throw new CourseAlreadyExistsException("Course with the name: " + course.getName() + " already exists");
        }
        courseRepository.persist(mapStructMapper.coursePostDTOToCourse(course));
        return "Added course: " + course.getName();
    }

    public String updateCourse(UUID courseId, CoursePostDTO course){
        log.info("CourseService-Update recommended course");
        Course c = courseRepository.findByIdOptional(courseId)
                .orElseThrow(() -> new CourseNotFoundException(COURSE_WITH_ID + courseId + DOES_NOT_EXIST));
        if (courseRepository.existsByName(course.getName()) && !c.getName().equals(course.getName())) {
            throw new CourseAlreadyExistsException("Course with the name: " + c.getName() + " already exists");
        }
        c.setName(course.getName());
        c.setDescription(course.getDescription());
        c.setCredit(course.getCredit());
        courseRepository.persist(c);
        return "Updated " + COURSE_WITH_ID + courseId + " to become\n" + course;
    }

    public CourseGetDTO viewCourse(UUID courseId){
        log.info("CourseService-View recommended course");
        return courseRepository.findByIdOptional(courseId)
                .map(mapStructMapper::courseToCourseGetDTO)
                .orElseThrow(() -> new CourseNotFoundException(COURSE_WITH_ID + courseId + DOES_NOT_EXIST));
    }

    public String deleteCourse(UUID courseId){
        log.info("CourseService-Delete recommended course");
        courseRepository.findByIdOptional(courseId).orElseThrow(
                () -> new CourseNotFoundException(COURSE_WITH_ID + courseId + DOES_NOT_EXIST));
        courseRepository.deleteById(courseId);
        return "Deleted " + COURSE_WITH_ID + courseId;
    }

    public List<CourseGetDTO> viewAllCourses(){
        log.info("CourseService-View all courses");
        return courseRepository.findAll().stream().map(mapStructMapper::courseToCourseGetDTO).toList();
    }

}
