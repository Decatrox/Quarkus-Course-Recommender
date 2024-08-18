package com.sumerge.course_recommender.course;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class CoursePostDTO {
    @JsonProperty("name")
    @NotBlank(message = "Name is required")
    private String name;

    @JsonProperty("description")
    @NotBlank(message = "Description is required")
    private String description;

    @JsonProperty("credit")
    @Min(value = 0, message = "Course Credit Points cannot be Negative")
    @Max(value = 12, message = "Maximum Course Credit Points is 12")
    private int credit;
}
