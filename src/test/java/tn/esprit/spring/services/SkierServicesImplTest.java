package tn.esprit.spring.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@Slf4j // Adds a logger with Lombok
@SpringBootTest
@RunWith(SpringRunner.class)
public class SkierServicesImplTest {

    @Autowired
    private SkierServicesImpl skierServices;

    @MockBean
    private ISkierRepository skierRepository;

    @MockBean
    private ISubscriptionRepository subscriptionRepository;

    @MockBean
    private IPisteRepository pisteRepository;

    @MockBean
    private ICourseRepository courseRepository;

    @MockBean
    private IRegistrationRepository registrationRepository;

    private Skier skier;
    private Subscription subscription;

    @Before
    public void setUp() {
        skier = new Skier();
        skier.setNumSkier(1L);
        skier.setFirstName("John");
        skier.setLastName("Doe");
        skier.setCity("Aspen");



        subscription = new Subscription();
        subscription.setNumSub(1L);
        subscription.setTypeSub(TypeSubscription.ANNUAL);
        subscription.setStartDate(LocalDate.now());
        skier.setSubscription(subscription);
        skier.setRegistrations(new HashSet<>());
        skier.setPistes(new HashSet<>());
    }

    @Test
    public void testRetrieveAllSkiers() {
        when(skierRepository.findAll()).thenReturn(Arrays.asList(skier));
        List<Skier> result = skierServices.retrieveAllSkiers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(skier.getNumSkier(), result.get(0).getNumSkier());
        verify(skierRepository).findAll();
        log.info("\nRetrieved all skiers: {}", result);
    }

    @Test
    public void testAddSkier() {
        when(skierRepository.save(any(Skier.class))).thenReturn(skier);

        Skier result = skierServices.addSkier(skier);
        assertNotNull(result);
        assertEquals(skier.getNumSkier(), result.getNumSkier());
        assertEquals(subscription.getEndDate(), skier.getSubscription().getEndDate());
        verify(skierRepository).save(skier);
        log.info("\nAdded skier: {}", result.toString());
    }

    @Test
    public void testAssignSkierToSubscription() {
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));
        when(skierRepository.save(any(Skier.class))).thenReturn(skier);

        Skier result = skierServices.assignSkierToSubscription(1L, 1L);
        assertNotNull(result);
        assertEquals(subscription, result.getSubscription());
        verify(skierRepository).save(skier);
        log.info("\nAssigned skier {} to subscription {}", skier.getNumSkier(), subscription.getNumSub());
    }

    @Test
    public void testAddSkierAndAssignToCourse() {
        Course course = new Course();
        course.setNumCourse(1L);

        when(courseRepository.getById(1L)).thenReturn(course);
        when(skierRepository.save(any(Skier.class))).thenReturn(skier);

        Skier result = skierServices.addSkierAndAssignToCourse(skier, 1L);
        assertNotNull(result);
        verify(courseRepository).getById(1L);
        verify(registrationRepository, times(skier.getRegistrations().size())).save(any(Registration.class));
        log.info("\nAdded skier {} and assigned to course {}", skier.getNumSkier(), course.getNumCourse());
    }

    @Test
    public void testRemoveSkier() {
        doNothing().when(skierRepository).deleteById(1L);
        skierServices.removeSkier(1L);
        verify(skierRepository).deleteById(1L);
        log.info("\nRemoved skier with ID {}", 1L);
    }

    @Test
    public void testRetrieveSkier() {
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        Skier result = skierServices.retrieveSkier(1L);

        assertNotNull(result);
        assertEquals(skier.getNumSkier(), result.getNumSkier());
        verify(skierRepository).findById(1L);
        log.info("\nRetrieved skier with ID {}", 1L);
    }

    @Test
    public void testAssignSkierToPiste() {
        Piste piste = new Piste();
        piste.setNumPiste(1L);

        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(pisteRepository.findById(1L)).thenReturn(Optional.of(piste));
        when(skierRepository.save(any(Skier.class))).thenReturn(skier);

        Skier result = skierServices.assignSkierToPiste(1L, 1L);
        assertNotNull(result);
        assertTrue(result.getPistes().contains(piste));
        verify(skierRepository).save(skier);
        log.info("\nAssigned skier {} to piste {}", skier.getNumSkier(), piste.getNumPiste());
    }

    @Test
    public void testRetrieveSkiersBySubscriptionType() {
        when(skierRepository.findBySubscription_TypeSub(TypeSubscription.ANNUAL)).thenReturn(Arrays.asList(skier));
        List<Skier> result = skierServices.retrieveSkiersBySubscriptionType(TypeSubscription.ANNUAL);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(skier.getNumSkier(), result.get(0).getNumSkier());
        verify(skierRepository).findBySubscription_TypeSub(TypeSubscription.ANNUAL);
        log.info("\nRetrieved skiers by subscription type: {}", TypeSubscription.ANNUAL);
    }
}
