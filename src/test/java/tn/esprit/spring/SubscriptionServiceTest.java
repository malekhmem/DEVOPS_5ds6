package tn.esprit.spring;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.repositories.ISubscriptionRepository;
import tn.esprit.spring.services.SubscriptionServicesImpl;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubscriptionServiceTest {

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SubscriptionServicesImpl subscriptionServiceImpl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddSubscription() {
        // Arrange
        Subscription subscription = new Subscription(1L, LocalDate.now(), LocalDate.now().plusMonths(1), 50.0f, TypeSubscription.MONTHLY);
        when(subscriptionRepository.save(subscription)).thenReturn(subscription);

        // Act
        Subscription createdSubscription = subscriptionServiceImpl.addSubscription(subscription);

        // Assert
        assertEquals(50.0f, createdSubscription.getPrice());
        assertEquals(TypeSubscription.MONTHLY, createdSubscription.getTypeSub());
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    @Test
    public void testRetrieveSubscriptionById() {
        // Arrange
        Long id = 1L;
        Subscription subscription = new Subscription(id, LocalDate.now(), LocalDate.now().plusMonths(1), 100.0f, TypeSubscription.ANNUAL);
        when(subscriptionRepository.findById(id)).thenReturn(java.util.Optional.of(subscription));

        // Act
        Subscription foundSubscription = subscriptionServiceImpl.retrieveSubscriptionById(id);

        // Assert
        assertEquals(id, foundSubscription.getNumSub());
        verify(subscriptionRepository, times(1)).findById(id);
    }

    @Test
    public void testShowMonthlyRecurringRevenue() {
        // Test Case 1: When recurring revenue is null
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY)).thenReturn(null);

        Float recurringRevenue = subscriptionServiceImpl.showMonthlyRecurringRevenue();
        assertEquals(0.0f, recurringRevenue, "Recurring revenue should be 0.0 when repository returns null");

        // Test Case 2: When recurring revenue is non-null
        when(subscriptionRepository.recurringRevenueByTypeSubEquals(TypeSubscription.MONTHLY)).thenReturn(200.0f);

        recurringRevenue = subscriptionServiceImpl.showMonthlyRecurringRevenue();
        assertEquals(200.0f, recurringRevenue, "Recurring revenue should match the value returned by the repository");
    }
}
