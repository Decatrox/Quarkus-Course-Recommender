package com.sumerge.course;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Path("/courses")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@AllArgsConstructor
public class CourseController {

    private final CourseService courseService;


    @Operation(summary = "View a course by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course found"),
            @ApiResponse(responseCode = "404", description = "Course not found"),
    })
    @GET
    @Path("/{id}")
    public Response getCourse(@PathParam("id") UUID id) {
        return Response.ok(courseService.viewCourse(id)).build();
    }


    @Operation(summary = "View all courses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful retrieval"),
    })
    @GET
    public Response getAllCourses(){
        return Response.ok(courseService.viewAllCourses()).build();
    }


    @Operation(summary = "Add a new course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Course added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid course data"),
    })
    @POST
    public Response addCourse(@Valid CoursePostDTO course) {
        return Response.ok(courseService.addCourse(course)).build();
    }


    @Operation(summary = "Update a course by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid course data"),
            @ApiResponse(responseCode = "404", description = "Course not found"),
    })
    @PUT
    @Path("/{id}")
    public Response updateCourse(@PathParam("id") UUID id, @Valid CoursePostDTO course) {
        return Response.ok(courseService.updateCourse(id, course)).build();
    }


    @Operation(summary = "Delete a course by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found"),
    })
    @DELETE
    @Path("/{id}")
    public Response deleteCourse(@PathParam("id") UUID id) {
        return Response.ok(courseService.deleteCourse(id)).build();
    }
}
