package tn.esprit.spring.entities;

import lombok.Data;

@Data
public class CourseDTO {
    private Long numCourse;
    private int level;
    private String typeCourse;  // Enum as String
    private String support;     // Enum as String
    private Float price;
    private int timeSlot;
}
