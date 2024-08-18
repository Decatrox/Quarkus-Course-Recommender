package com.sumerge.mapper;

import com.sumerge.course.Course;
import com.sumerge.course.CourseGetDTO;
import com.sumerge.course.CoursePostDTO;
import org.mapstruct.Mapper;


@Mapper(componentModel = "cdi")
public interface MapStructMapper {
    CourseGetDTO courseToCourseGetDTO(Course course);
    Course coursePostDTOToCourse(CoursePostDTO coursePostDTO);
}
