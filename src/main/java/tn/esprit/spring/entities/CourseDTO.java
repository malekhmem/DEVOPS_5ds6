package tn.esprit.spring.entities;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseDTO {

    Long numCourse;
    int level;
    TypeCourse typeCourse;  // Using the existing enum
    Support support;         // Using the existing enum
    Float price;
    int timeSlot;

    // Note: Exclude complex fields like `registrations` that are typically unnecessary in a DTO.
}
