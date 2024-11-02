package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IRegistrationRepository;
import java.util.*;

@AllArgsConstructor
@Service
public class CourseServicesImpl implements ICourseServices {

    private final ICourseRepository courseRepository;
    private final IRegistrationRepository registrationRepository;

    @Override
    public List<Course> retrieveAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course addCourse(Course course) {
        return validateAndSaveCourse(course);
    }

    @Override
    public Course updateCourse(Course course) {
        // Optionnel : vérification de l'existence du cours avant la mise à jour
        return validateAndSaveCourse(course);
    }

    @Override
    public Course retrieveCourse(Long numCourse) {
        return courseRepository.findById(numCourse).orElse(null);
    }

    /**
     * Validates and saves the course.
     * @param course the course to validate and save.
     * @return the saved course.
     */
    private Course validateAndSaveCourse(Course course) {
        validateCourse(course);
        return courseRepository.save(course);
    }

    /**
     * Validates the course before saving or updating it.
     * @param course the course to validate.
     */
    private void validateCourse(Course course) {
        if (course.getPrice() <= 0) {
            throw new IllegalArgumentException("Course price must be greater than zero.");
        }
        if (course.getLevel() < 1 || course.getLevel() > 5) {
            throw new IllegalArgumentException("Course level must be between 1 and 5.");
        }
        if (course.getTypeCourse() == null) {
            throw new IllegalArgumentException("Course type cannot be null.");
        }
    }
}
