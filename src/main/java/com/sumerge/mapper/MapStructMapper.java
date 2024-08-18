package com.sumerge.course_recommender.mapper;

import com.sumerge.course_recommender.author.Author;
import com.sumerge.course_recommender.author.AuthorGetDTO;
import com.sumerge.course_recommender.author.AuthorPostDTO;
import com.sumerge.course_recommender.course.Course;
import com.sumerge.course_recommender.course.CourseGetDTO;
import com.sumerge.course_recommender.course.CoursePostDTO;
import com.sumerge.course_recommender.course.CourseRecommender;
import com.sumerge.course_recommender.course.recommenders.GetCoursesResponse;
import com.sumerge.course_recommender.user.AppUser;
import com.sumerge.course_recommender.user.UserPostDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Mapper(
        componentModel = "spring"
)

public interface MapStructMapper {
    CourseGetDTO courseToCourseGetDTO(Course course);
    Course coursePostDTOToCourse(CoursePostDTO coursePostDTO);
    AuthorGetDTO authorToAuthorGetDTO(Author author);
    Author authorPostDTOToAuthor(AuthorPostDTO authorPostDTO);
    Course courseRecommenderToCourse (GetCoursesResponse.Course course);

    @Mapping(source = "userName", target = "userName")
    @Mapping(source = "password", target = "password")
    AppUser userPostDTOToAppUser(UserPostDTO user);

     default Page<CourseGetDTO> pageCourseToPageCourseGetDTO(Page<Course> coursePage) {
        List<CourseGetDTO> courseGetDTOList = coursePage
                .map(this::courseToCourseGetDTO)
                .getContent();

        return new PageImpl<>(courseGetDTOList, coursePage.getPageable(), coursePage.getTotalElements());
    }
}
