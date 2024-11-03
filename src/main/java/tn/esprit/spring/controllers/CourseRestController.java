package tn.esprit.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.IRegistrationRepository;
import tn.esprit.spring.services.ICourseServices;
import tn.esprit.spring.services.IRegistrationServices;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name = "\uD83D\uDCDA Course Management")
@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseRestController {

    private final ICourseServices courseServices;
    private final IRegistrationRepository registrationRepository;

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


    @Operation(description = "Add Course and Assign To Registration")
    @PutMapping("/addAndAssignToRegistration/{numRegistration}")
    public Course addAndAssignToCourse(@RequestBody Course course, @PathVariable("numRegistration") Long numRegistration) {
        return courseServices.addCourseAndAssignToregistre(course, numRegistration);
    }

    @PostMapping("/course/{registrationId}/assign")
    public ResponseEntity<Course> addCourseAndAssignToRegistration(@RequestBody Course course, @PathVariable("registrationId") Long numRegistration) {
        Course savedCourse = courseServices.addCourseAndAssignToregistre(course, numRegistration);
        return ResponseEntity.ok(savedCourse);
    }

    // New Advanced Methods

    @Operation(description = "Filter Courses by Type")
    @GetMapping("/filterByType/{typeCourse}")
    public List<Course> filterCoursesByType(@PathVariable("typeCourse") TypeCourse typeCourse) {
        return courseServices.filterCoursesByType(typeCourse);
    }

    @Operation(description = "Remove Course from Registration")
    @DeleteMapping("/removeCourseFromRegistration/{numCourse}/{numRegistration}")
    public ResponseEntity<String> removeCourseFromRegistration(@PathVariable("numCourse") Long numCourse, @PathVariable("numRegistration") Long numRegistration) {
        courseServices.removeCourseFromRegistration(numCourse, numRegistration);
        return ResponseEntity.ok("Course successfully removed from registration.");
    }

    @Operation(description = "Check if a Course is Already Assigned to a Registration")
    @GetMapping("/isCourseAssigned/{numCourse}/{numRegistration}")
    public ResponseEntity<Boolean> isCourseAlreadyAssigned(@PathVariable("numCourse") Long numCourse, @PathVariable("numRegistration") Long numRegistration) {
        Course course = courseServices.retrieveCourse(numCourse);
        Registration registration = registrationRepository.findById(numRegistration).orElse(null); // Assuming registration repository is accessible
        boolean isAssigned = courseServices.isCourseAlreadyAssigned(course, registration);
        return ResponseEntity.ok(isAssigned);
    }






    // ================================ FINANCIAL ANALYSIS ==================================

    @Operation(description = "Calculate Revenue for a Specific Course Over a Period")
    @GetMapping("/revenuePerCourse/{courseId}")
    public ResponseEntity<Float> calculateRevenuePerCourse(
            @PathVariable("courseId") Long courseId,
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return ResponseEntity.ok(courseServices.calculateRevenuePerCourse(courseId, startDate, endDate));
    }

    @Operation(description = "Calculate Total Revenue Over a Period")
    @GetMapping("/revenueOverPeriod")
    public ResponseEntity<Float> calculateRevenueOverPeriod(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return ResponseEntity.ok(courseServices.calculateRevenueOverPeriod(startDate, endDate));
    }

    @Operation(description = "Calculate Course Popularity Over a Period")
    @GetMapping("/coursePopularity/{courseId}")
    public ResponseEntity<Integer> getCoursePopularity(
            @PathVariable("courseId") Long courseId,
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return ResponseEntity.ok(courseServices.getCoursePopularity(courseId, startDate, endDate));
    }

    @Operation(description = "Get Total Revenue and Registrations Over a Period")
    @GetMapping("/totalRevenueAndRegistrations")
    public ResponseEntity<Map<String, Object>> getTotalRevenueAndRegistrations(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return ResponseEntity.ok(courseServices.getTotalRevenueAndRegistrations(startDate, endDate));
    }

    @Operation(description = "Calculate Average Course Price Over a Period")
    @GetMapping("/averagePrice")
    public ResponseEntity<Float> calculateAveragePrice(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return ResponseEntity.ok(courseServices.calculateAveragePrice(startDate, endDate));
    }

}
