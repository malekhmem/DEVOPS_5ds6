//package tn.esprit.spring.services;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import tn.esprit.spring.entities.Course;
//import tn.esprit.spring.entities.Registration;
//import tn.esprit.spring.entities.Skier;
//import tn.esprit.spring.entities.TypeCourse;
//import tn.esprit.spring.repositories.ICourseRepository;
//import tn.esprit.spring.repositories.IRegistrationRepository;
//import tn.esprit.spring.repositories.ISkierRepository;
//import tn.esprit.spring.services.RegistrationServicesImpl;
//
//import java.time.LocalDate;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest
//class RegistrationServicesImplTest {
//
//    @Autowired
//    private ISkierRepository skierRepository;
//
//    @Autowired
//    private ICourseRepository courseRepository;
//
//    @Autowired
//    private IRegistrationRepository registrationRepository;
//
//    @Autowired
//    private RegistrationServicesImpl registrationService;
//
//    private Skier testSkier;
//    private Course testCourse;
//    private Registration testRegistration;
//
//    @BeforeEach
//    public void setUp() {
//        // Ensure clean state by removing existing test data
//        registrationRepository.deleteAll();
//
//        // Create a test Skier
//        testSkier = new Skier();
//        testSkier.setDateOfBirth(LocalDate.of(2010, 1, 1)); // Age < 16 for CHILD course
//        skierRepository.save(testSkier);
//
//        // Create a test Course for children
//        testCourse = new Course();
//        testCourse.setTypeCourse(TypeCourse.COLLECTIVE_CHILDREN);
//        courseRepository.save(testCourse);
//
//        // Create a test Registration
//        testRegistration = new Registration();
//        testRegistration.setNumWeek(1);
//    }
//
//    @Test
//    void testAddRegistrationAndAssignToSkierAndCourse_SuccessfulChildRegistration() {
//        // Act
//        Registration result = registrationService.addRegistrationAndAssignToSkierAndCourse(
//                testRegistration, testSkier.getNumSkier(), testCourse.getNumCourse());
//
//        // Assert
//        assertNotNull(result);
//        assertEquals(testSkier.getNumSkier(), result.getSkier().getNumSkier());
//        assertEquals(testCourse.getNumCourse(), result.getCourse().getNumCourse());
//    }
//
//    @Test
//    void testAddRegistrationAndAssignToSkierAndCourse_FullCourseForChildren() {
//        // Pre-fill the course with the maximum number of registrations for children
//        for (int i = 0; i < 6; i++) {
//            Registration registration = new Registration();
//            registration.setNumWeek(1);
//            registration.setCourse(testCourse);
//            registrationRepository.save(registration);
//        }
//
//        // Act
//        Registration result = registrationService.addRegistrationAndAssignToSkierAndCourse(
//                testRegistration, testSkier.getNumSkier(), testCourse.getNumCourse());
//
//        // Assert: Course should be full, so registration should be null
//        assertNull(result);
//    }
//
//    @Test
//    void testAddRegistrationAndAssignToSkierAndCourse_AgeRestriction() {
//        // Update testSkier's date of birth to make them an adult
//        testSkier.setDateOfBirth(LocalDate.of(1990, 1, 1)); // Age > 16 for ADULT course
//        skierRepository.save(testSkier);
//
//        // Update course type to ADULT
//        testCourse.setTypeCourse(TypeCourse.COLLECTIVE_ADULT);
//        courseRepository.save(testCourse);
//
//        // Act
//        Registration result = registrationService.addRegistrationAndAssignToSkierAndCourse(
//                testRegistration, testSkier.getNumSkier(), testCourse.getNumCourse());
//
//        // Assert: Registration should be successful for an adult course
//        assertNotNull(result);
//        assertEquals(testSkier.getNumSkier(), result.getSkier().getNumSkier());
//        assertEquals(testCourse.getNumCourse(), result.getCourse().getNumCourse());
//    }
//
//    @Test
//    void testAddRegistrationAndAssignToSkierAndCourse_DuplicateRegistration() {
//        // Pre-create a registration for the same week, skier, and course
//        registrationService.addRegistrationAndAssignToSkierAndCourse(testRegistration, testSkier.getNumSkier(), testCourse.getNumCourse());
//
//        // Try to register the same skier for the same course and week again
//        Registration duplicateResult = registrationService.addRegistrationAndAssignToSkierAndCourse(testRegistration, testSkier.getNumSkier(), testCourse.getNumCourse());
//
//        // Assert: Duplicate registration should not be allowed
//        assertNull(duplicateResult);
//    }
//}
