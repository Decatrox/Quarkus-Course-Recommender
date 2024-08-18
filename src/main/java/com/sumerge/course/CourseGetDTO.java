package com.sumerge.course_recommender.course;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class CourseGetDTO {
    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("credit")
    private int credit;
}
