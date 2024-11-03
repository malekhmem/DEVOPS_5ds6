package tn.esprit.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.controllers.SubscriptionRestController;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.services.ISubscriptionServices;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.given;

@WebMvcTest(SubscriptionRestController.class)
public class SubscriptionRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ISubscriptionServices subscriptionServices;

    @Test
    public void testAddSubscription() throws Exception {
        // Arrange
        Subscription subscription = new Subscription(1L, LocalDate.now(), LocalDate.now().plusMonths(1), 100.0f, TypeSubscription.ANNUAL);
        given(subscriptionServices.addSubscription(Mockito.any(Subscription.class))).willReturn(subscription);

        // Act & Assert
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule for LocalDate serialization

        mockMvc.perform(post("/subscription/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(subscription)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numSub").value(1L))
                .andExpect(jsonPath("$.typeSub").value("ANNUAL"));
    }

    @Test
    public void testGetSubscriptionById() throws Exception {
        // Arrange
        Long id = 1L;
        Subscription subscription = new Subscription(id, LocalDate.now(), LocalDate.now().plusMonths(1), 100.0f, TypeSubscription.MONTHLY);
        given(subscriptionServices.retrieveSubscriptionById(id)).willReturn(subscription);

        // Act & Assert
        mockMvc.perform(get("/subscription/get/{id-subscription}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numSub").value(1L))
                .andExpect(jsonPath("$.typeSub").value("MONTHLY"));
    }
}
