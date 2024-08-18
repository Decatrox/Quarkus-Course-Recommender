package com.sumerge.course;


import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class CourseRepository implements PanacheRepositoryBase<Course, UUID> {
    boolean existsByName(String name) {
        return count("name", name) > 0;
    }
}
