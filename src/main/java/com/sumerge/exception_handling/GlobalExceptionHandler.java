package com.sumerge.exception_handling;

import com.sumerge.exception_handling.custom_exceptions.CourseAlreadyExistsException;
import com.sumerge.exception_handling.custom_exceptions.CourseNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof CourseNotFoundException) {
            return buildErrorResponse("Not Found", exception.getMessage(), Response.Status.NOT_FOUND);
        } else if (exception instanceof CourseAlreadyExistsException) {
            return buildErrorResponse("Conflict", exception.getMessage(), Response.Status.CONFLICT);
        } else if (exception instanceof ConstraintViolationException || exception instanceof BadRequestException) {
            return handleValidationExceptions((ConstraintViolationException) exception);
        } else {
            return buildErrorResponse("Internal Server Error", exception.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    private Response handleValidationExceptions(ConstraintViolationException exception) {
        Map<String, ArrayList<String>> errors = new HashMap<>();
        exception.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.computeIfAbsent(fieldName, nothing -> new ArrayList<>()).add(errorMessage);
        });
        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity(errors)
                .build();
    }

    private Response buildErrorResponse(String error, String message, Response.Status status) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", error);
        errorResponse.put("message", message);
        return Response.status(status)
                .type(MediaType.APPLICATION_JSON)
                .entity(errorResponse)
                .build();
    }
}
