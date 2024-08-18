package com.sumerge.course;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.quarkus.test.InjectMock;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

@QuarkusTest
class CourseControllerTest {

    @InjectMock
    private CourseService courseService;

    @BeforeEach
    public void setUp() {
        RestAssured.basePath = "/courses";
    }


    @Test
    void shouldViewCourseById() {
        UUID id = UUID.randomUUID();
        CourseGetDTO courseGetDTO = new CourseGetDTO();
        courseGetDTO.setName("Test Name");
        courseGetDTO.setDescription("Test Description");
        courseGetDTO.setCredit(6);

        when(courseService.viewCourse(id)).thenReturn(courseGetDTO);

        given()
                .when().get("/{id}", id)
                .then()
                .statusCode(200)
                .body("name", equalTo("Test Name"))
                .body("description", equalTo("Test Description"))
                .body("credit", equalTo(6));

        verify(courseService).viewCourse(id);
    }

    @Test
    void shouldCallUpdateCourseService() {
        CoursePostDTO coursePostDTO = new CoursePostDTO();
        coursePostDTO.setName("Test Name");
        coursePostDTO.setDescription("Test Description");
        coursePostDTO.setCredit(6);
        UUID testCourseId = UUID.randomUUID();

        when(courseService.updateCourse(eq(testCourseId), any(CoursePostDTO.class))).thenReturn("updated");

        given()
                .contentType("application/json")
                .body(coursePostDTO)
                .when().put("/{id}", testCourseId)
                .then()
                .statusCode(200);

        verify(courseService).updateCourse(eq(testCourseId), any(CoursePostDTO.class));
    }

    @Test
    void shouldFailToCallUpdateCourseServiceIfInvalid() {
        CoursePostDTO coursePostDTO = new CoursePostDTO();
        UUID testCourseId = UUID.randomUUID();

        given()
                .contentType("application/json")
                .body(coursePostDTO)
                .when().put("/{id}", testCourseId)
                .then()
                .statusCode(400);

        coursePostDTO.setName("");
        coursePostDTO.setDescription("a");
        coursePostDTO.setCredit(6);

        given()
                .contentType("application/json")
                .body(coursePostDTO)
                .when().put("/{id}", testCourseId)
                .then()
                .statusCode(400);

        coursePostDTO.setName("a");
        coursePostDTO.setDescription("");
        coursePostDTO.setCredit(6);

        given()
                .contentType("application/json")
                .body(coursePostDTO)
                .when().put("/{id}", testCourseId)
                .then()
                .statusCode(400);

        coursePostDTO.setName("a");
        coursePostDTO.setDescription("a");
        coursePostDTO.setCredit(-1);

        given()
                .contentType("application/json")
                .body(coursePostDTO)
                .when().put("/{id}", testCourseId)
                .then()
                .statusCode(400);

        coursePostDTO.setCredit(14);

        given()
                .contentType("application/json")
                .body(coursePostDTO)
                .when().put("/{id}", testCourseId)
                .then()
                .statusCode(400);
    }

    @Test
    void shouldCallDeleteCourseByIdService() {
        UUID testCourseId = UUID.randomUUID();

        when(courseService.deleteCourse(testCourseId)).thenReturn("deleted");

        given()
                .when().delete("/{id}", testCourseId)
                .then()
                .statusCode(200);

        verify(courseService).deleteCourse(testCourseId);
    }

    @Test
    void shouldCallAddCourseServiceIfValid() {
        CoursePostDTO coursePostDTO = new CoursePostDTO();
        coursePostDTO.setName("Test Name");
        coursePostDTO.setDescription("Test Description");
        coursePostDTO.setCredit(6);

        when(courseService.addCourse(any(CoursePostDTO.class))).thenReturn("added");

        given()
                .contentType("application/json")
                .body(coursePostDTO)
                .when().post()
                .then()
                .statusCode(200);

        verify(courseService).addCourse(any(CoursePostDTO.class));
    }

    @Test
    void shouldFailToCallAddCourseServiceIfInvalid() {
        CoursePostDTO coursePostDTO = new CoursePostDTO();

        given()
                .contentType("application/json")
                .body(coursePostDTO)
                .when().post()
                .then()
                .statusCode(400);

        coursePostDTO.setName("");
        coursePostDTO.setDescription("a");
        coursePostDTO.setCredit(6);

        given()
                .contentType("application/json")
                .body(coursePostDTO)
                .when().post()
                .then()
                .statusCode(400);

        coursePostDTO.setName("a");
        coursePostDTO.setDescription("");

        given()
                .contentType("application/json")
                .body(coursePostDTO)
                .when().post()
                .then()
                .statusCode(400);

        coursePostDTO.setCredit(-1);

        given()
                .contentType("application/json")
                .body(coursePostDTO)
                .when().post()
                .then()
                .statusCode(400);

        coursePostDTO.setCredit(14);

        given()
                .contentType("application/json")
                .body(coursePostDTO)
                .when().post()
                .then()
                .statusCode(400);
    }

    @Test
    void itShouldCallViewAllCourse() {
        List<CourseGetDTO> courseGetDTOList = new ArrayList<>();
        when(courseService.viewAllCourses()).thenReturn(courseGetDTOList);

        given()
                .contentType("application/json")
                .when().get()
                .then()
                .statusCode(200);

        verify(courseService).viewAllCourses();
    }
}