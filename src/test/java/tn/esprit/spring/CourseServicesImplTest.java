package tn.esprit.spring;


import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;
        import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IRegistrationRepository;
import tn.esprit.spring.services.CourseServicesImpl;
import org.mockito.MockitoAnnotations;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class CourseServicesImplTest {

    @Mock
    private ICourseRepository courseRepository;

    @Mock
    private IRegistrationRepository registrationRepository;

    @InjectMocks
    private CourseServicesImpl courseService;

    private Course course;
    private Registration registration;
    private Date startDate;
    private Date endDate;

    @BeforeEach
    void setup() {  // Le mot-clé 'public' a été retiré ici
        MockitoAnnotations.openMocks(this); // Initialiser les mocks explicitement

        // Initialiser l'objet Course
        course = new Course();
        course.setNumCourse(1L);
        course.setLevel(1);
        course.setTypeCourse(TypeCourse.COLLECTIVE_ADULT);
        course.setSupport(Support.SKI);
        course.setPrice(100.0f);
        course.setTimeSlot(2);

        // Utiliser lenient pour éviter des erreurs de stubbing non utilisées
        lenient().when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        lenient().when(courseRepository.save(any(Course.class))).thenReturn(course);

        // Initialiser l'objet Registration et les dates pour les tests
        registration = new Registration();
        registration.setNumRegistration(1L);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -30);
        startDate = cal.getTime();

        cal.add(Calendar.DATE, 15);
        registration.setRegistrationDate(cal.getTime());

        cal.add(Calendar.DATE, 15);
        endDate = cal.getTime();
    }

    @Test
    void testRetrieveAllCourses() {  // Le mot-clé 'public' a été retiré ici
        // Given
        Course course1 = new Course();
        course1.setNumCourse(1L);
        course1.setLevel(1);
        course1.setTypeCourse(TypeCourse.COLLECTIVE_CHILDREN);
        course1.setSupport(Support.SKI);
        course1.setPrice(100.0f);
        course1.setTimeSlot(2);

        Course course2 = new Course();
        course2.setNumCourse(2L);
        course2.setLevel(2);
        course2.setTypeCourse(TypeCourse.COLLECTIVE_ADULT);
        course2.setSupport(Support.SNOWBOARD);
        course2.setPrice(150.0f);
        course2.setTimeSlot(3);

        List<Course> courses = Arrays.asList(course1, course2);
        when(courseRepository.findAll()).thenReturn(courses);

        // When
        List<Course> result = courseService.retrieveAllCourses();

        // Then
        assertEquals(2, result.size());
        assertEquals(courses, result);
    }

    @Test
    void testAddCourse() {  // Le mot-clé 'public' a été retiré ici
        // Given
        when(courseRepository.save(course)).thenReturn(course);

        // When
        Course result = courseService.addCourse(course);

        // Then
        assertEquals(course, result);
    }

    @Test
    void testUpdateCourse() {  // Le mot-clé 'public' a été retiré ici
        // Stub pour s'assurer que le cours existe
        when(courseRepository.existsById(1L)).thenReturn(true);  // Assure que existsById renvoie true
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        // Modifier les informations du cours
        course.setPrice(150.0f);
        course.setLevel(3);

        // Appeler la méthode de mise à jour
        Course updatedCourse = courseService.updateCourse(course);

        // Vérifications
        assertNotNull(updatedCourse);
        assertEquals(1L, updatedCourse.getNumCourse());
        assertEquals(150.0f, updatedCourse.getPrice());
        assertEquals(3, updatedCourse.getLevel());

        // Vérifier que existsById et save ont été appelés
        verify(courseRepository, times(1)).existsById(1L);
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testRetrieveCourse() {  // Le mot-clé 'public' a été retiré ici
        // Given
        Long courseId = 1L;
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // When
        Course result = courseService.retrieveCourse(courseId);

        // Then
        assertEquals(course, result);
    }

    @Test
    void testAddCourseAndAssignToRegistration() {  // Le mot-clé 'public' a été retiré ici
        // Simuler le comportement du repository pour la récupération d'inscription
        when(registrationRepository.findById(1L)).thenReturn(Optional.of(registration));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        // Appeler la méthode testée
        Course result = courseService.addCourseAndAssignToregistre(course, 1L);

        // Vérifier les résultats
        assertNotNull(result);
        assertTrue(result.getRegistrations().contains(registration));
    }

    @Test
    void testCalculateRevenuePerCourse() {  // Le mot-clé 'public' a été retiré ici
        registration.setRegistrationDate(new Date(startDate.getTime() + (1000L * 60 * 60 * 24 * 10))); // 10 jours après startDate
        course.setRegistrations(new HashSet<>(Collections.singletonList(registration)));

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        float revenue = courseService.calculateRevenuePerCourse(1L, startDate, endDate);
        assertEquals(100.0f, revenue);
    }

    @Test
    void testGetTotalRevenueAndRegistrations() {  // Le mot-clé 'public' a été retiré ici
        registration.setRegistrationDate(new Date(startDate.getTime() + (1000L * 60 * 60 * 24 * 10))); // 10 jours après startDate
        course.setRegistrations(new HashSet<>(Collections.singletonList(registration)));
        List<Course> courses = Arrays.asList(course);

        when(courseRepository.findAll()).thenReturn(courses);

        Map<String, Object> result = courseService.getTotalRevenueAndRegistrations(startDate, endDate);

        assertEquals(100.0f, result.get("totalRevenue"));
        assertEquals(1, result.get("totalRegistrations"));
    }

    @Test
    void testGetCoursePopularity() {  // Le mot-clé 'public' a été retiré ici
        registration.setRegistrationDate(new Date(startDate.getTime() + (1000L * 60 * 60 * 24 * 10))); // 10 jours après startDate
        course.setRegistrations(new HashSet<>(Collections.singletonList(registration)));

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        int popularity = courseService.getCoursePopularity(1L, startDate, endDate);

        assertEquals(1, popularity);
    }
}
