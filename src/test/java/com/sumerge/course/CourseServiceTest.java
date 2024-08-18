package com.sumerge.course;

import com.sumerge.exception_handling.custom_exceptions.CourseAlreadyExistsException;
import com.sumerge.exception_handling.custom_exceptions.CourseNotFoundException;
import com.sumerge.mapper.MapStructMapper;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @InjectMocks
    private CourseService courseService;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private MapStructMapper mapStructMapper;

    private Course testCourse;
    private final String name = "Test Course";
    private final String description = "Test Description";
    private final int credit = 8;

    @BeforeEach
    void setUp() {
        testCourse = new Course();
        testCourse.setName(name); testCourse.setDescription(description); testCourse.setCredit(credit);
    }


    @Test
    void itShouldAddCourse() {
        CoursePostDTO coursePostDTO = new CoursePostDTO();
        coursePostDTO.setName(name); coursePostDTO.setDescription(description); coursePostDTO.setCredit(credit);
        org.mockito.Mockito.when(mapStructMapper.coursePostDTOToCourse(coursePostDTO)).thenReturn(testCourse);
        org.mockito.Mockito.when(courseRepository.existsByName(name)).thenReturn(false);

        courseService.addCourse(coursePostDTO);

        ArgumentCaptor<Course> courseArgumentCaptor = ArgumentCaptor.forClass(Course.class);
        verify(courseRepository).persist(courseArgumentCaptor.capture());
        Course savedCourse = courseArgumentCaptor.getValue();

        assertThat(savedCourse).isEqualTo(testCourse);

    }

    @Test
    void itShouldNotAddDuplicateCourse() {
        CoursePostDTO coursePostDTO = new CoursePostDTO();
        coursePostDTO.setName(name);
        org.mockito.Mockito.when(courseRepository.existsByName(name)).thenReturn(true);
        assertThatThrownBy(() -> courseService.addCourse(coursePostDTO))
                .isInstanceOf(CourseAlreadyExistsException.class);

    }

    @Test
    void itShouldUpdateCourse() {
        UUID id = UUID.randomUUID();
        testCourse.setId(id);
        CoursePostDTO coursePostDTO = new CoursePostDTO();
        coursePostDTO.setName(name); coursePostDTO.setDescription(description); coursePostDTO.setCredit(credit);
        org.mockito.Mockito.when(courseRepository.findByIdOptional(id)).thenReturn(Optional.of(testCourse));
        org.mockito.Mockito.when(courseRepository.existsByName(name)).thenReturn(false);

        courseService.updateCourse(id, coursePostDTO);

        ArgumentCaptor<Course> courseArgumentCaptor = ArgumentCaptor.forClass(Course.class);
        verify(courseRepository).persist(courseArgumentCaptor.capture());
        Course savedCourse = courseArgumentCaptor.getValue();

        assertThat(savedCourse).isEqualTo(testCourse);
    }

    @Test
    void itShouldFailToUpdateCourseThatDoesNotExist(){
        CoursePostDTO coursePostDTO = new CoursePostDTO();
        UUID id = UUID.randomUUID();

        when(courseRepository.findByIdOptional(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> courseService.updateCourse(id, coursePostDTO))
                .isInstanceOf(CourseNotFoundException.class);
    }

    @Test
    void itShouldFailToUpdateCourseToADuplicateName(){
        CoursePostDTO coursePostDTO = new CoursePostDTO();
        UUID id = UUID.randomUUID();
        coursePostDTO.setName(name); coursePostDTO.setDescription(description); coursePostDTO.setCredit(credit);
        Course courseUpdate = new Course();
        courseUpdate.setName(name + " ");

        when(courseRepository.findByIdOptional(id)).thenReturn(Optional.of(courseUpdate));
        when(courseRepository.existsByName(name)).thenReturn(true);
        assertThatThrownBy(() -> courseService.updateCourse(id, coursePostDTO))
                .isInstanceOf(CourseAlreadyExistsException.class);
    }

    @Test
    void itShouldViewCourseById() {
        UUID id = UUID.randomUUID();
        testCourse.setId(id);

        //setting the DTO
        CourseGetDTO courseGetDTO = new CourseGetDTO();
        courseGetDTO.setName(name); courseGetDTO.setDescription(description); courseGetDTO.setCredit(credit);
        org.mockito.Mockito.when(courseRepository.findByIdOptional(id)).thenReturn(Optional.of(testCourse));
        org.mockito.Mockito.when(mapStructMapper.courseToCourseGetDTO(testCourse)).thenReturn(courseGetDTO);

        CourseGetDTO returnedCourse = courseService.viewCourse(id);

        assertThat(returnedCourse).usingRecursiveComparison().ignoringFields("id").isEqualTo(testCourse);

    }

    @Test
    void itShouldDeleteCourse() {
        UUID id = UUID.randomUUID();
        CoursePostDTO coursePostDTO = new CoursePostDTO();
        coursePostDTO.setName(name); coursePostDTO.setDescription(description); coursePostDTO.setCredit(credit);

        org.mockito.Mockito.when(courseRepository.findByIdOptional(id)).thenReturn(Optional.ofNullable(testCourse));
        org.mockito.Mockito.when(courseRepository.deleteById(id)).thenReturn(true);

        courseService.deleteCourse(id);

        ArgumentCaptor<UUID> idArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(courseRepository).deleteById(idArgumentCaptor.capture());
        UUID deletedID = idArgumentCaptor.getValue();

        assertThat(deletedID).isEqualTo(id);
    }

    @Test
    void itShouldNotDeleteCourseIfInvalidId() {
        UUID id = UUID.randomUUID();
        CoursePostDTO coursePostDTO = new CoursePostDTO();
        coursePostDTO.setName(name); coursePostDTO.setDescription(description); coursePostDTO.setCredit(credit);

        org.mockito.Mockito.when(courseRepository.findByIdOptional(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.deleteCourse(id))
                .isInstanceOf(CourseNotFoundException.class);
    }

    @Test
    void itShouldGetAllCourses() {
        PanacheQuery<Course> mockQuery = Mockito.mock(PanacheQuery.class);
        org.mockito.Mockito.when(courseRepository.findAll()).thenReturn(mockQuery);
        courseService.viewAllCourses();
        verify(courseRepository).findAll();
    }

}
