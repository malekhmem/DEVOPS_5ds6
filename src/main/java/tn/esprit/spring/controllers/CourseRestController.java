package tn.esprit.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.CourseDTO;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.services.ICourseServices;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "\uD83D\uDCDA Course Management")
@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseRestController {

    private final ICourseServices courseServices;

    @Operation(description = "Add Course")
    @PostMapping("/add")
    public CourseDTO addCourse(@RequestBody CourseDTO courseDTO) {
        Course course = convertToEntity(courseDTO);
        Course savedCourse = courseServices.addCourse(course);
        return convertToDTO(savedCourse);
    }

    @Operation(description = "Retrieve all Courses")
    @GetMapping("/all")
    public List<CourseDTO> getAllCourses() {
        return courseServices.retrieveAllCourses()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Operation(description = "Update Course")
    @PutMapping("/update")
    public CourseDTO updateCourse(@RequestBody CourseDTO courseDTO) {
        Course course = convertToEntity(courseDTO);
        Course updatedCourse = courseServices.updateCourse(course);
        return convertToDTO(updatedCourse);
    }

    @Operation(description = "Retrieve Course by Id")
    @GetMapping("/get/{id-course}")
    public CourseDTO getById(@PathVariable("id-course") Long numCourse) {
        Course course = courseServices.retrieveCourse(numCourse);
        return convertToDTO(course);
    }

    // Helper methods to convert between Course and CourseDTO
    private CourseDTO convertToDTO(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setNumCourse(course.getNumCourse());
        dto.setLevel(course.getLevel());
        dto.setTypeCourse(course.getTypeCourse().toString());
        dto.setSupport(course.getSupport().toString());
        dto.setPrice(course.getPrice());
        dto.setTimeSlot(course.getTimeSlot());
        return dto;
    }

    private Course convertToEntity(CourseDTO courseDTO) {
        Course course = new Course();
        course.setNumCourse(courseDTO.getNumCourse());
        course.setLevel(courseDTO.getLevel());

        // Safe conversion from String to Enum with error handling
        try {
            course.setTypeCourse(TypeCourse.valueOf(courseDTO.getTypeCourse().toUpperCase()));
        } catch (IllegalArgumentException | NullPointerException e) {
            // Log the error and/or set a default value if desired
            // Example: Setting a default value if conversion fails
            course.setTypeCourse(TypeCourse.INDIVIDUAL);  // or any other default
        }

        try {
            course.setSupport(Support.valueOf(courseDTO.getSupport().toUpperCase()));
        } catch (IllegalArgumentException | NullPointerException e) {
            // Log the error and/or set a default value if desired
            course.setSupport(Support.SKI);  // or any other default
        }

        course.setPrice(courseDTO.getPrice());
        course.setTimeSlot(courseDTO.getTimeSlot());

        return course;
    }

}
