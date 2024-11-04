package tn.esprit.spring.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.services.ISkierServices;
import java.util.Collections;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.mock.mockito.MockBean;



@WebMvcTest(SkierRestController.class)
@AutoConfigureMockMvc
class SkierControllerRestTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ISkierServices skierServices;

    private Skier skier;

    @BeforeEach
    public void setUp() {
        skier = new Skier();
        skier.setNumSkier(1L);
        skier.setFirstName("John");
        skier.setLastName("Doe");
        skier.setCity("Aspen");
        skier.setSubscription(null); // Add subscription as necessary
    }

    @Test
    void testAddSkier() throws Exception {
        when(skierServices.addSkier(any(Skier.class))).thenReturn(skier);

        mockMvc.perform(post("/skier/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(skier)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numSkier").value(skier.getNumSkier()))
                .andExpect(jsonPath("$.firstName").value(skier.getFirstName()));
    }

    @Test
    void testAddSkierAndAssignToCourse() throws Exception {
        when(skierServices.addSkierAndAssignToCourse(any(Skier.class), anyLong())).thenReturn(skier);

        mockMvc.perform(post("/skier/addAndAssign/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(skier)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numSkier").value(skier.getNumSkier()));
    }

    @Test
    void testAssignToSubscription() throws Exception {
        when(skierServices.assignSkierToSubscription(anyLong(), anyLong())).thenReturn(skier);

        mockMvc.perform(put("/skier/assignToSub/1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numSkier").value(skier.getNumSkier()));
    }

    @Test
    void testAssignToPiste() throws Exception {
        when(skierServices.assignSkierToPiste(anyLong(), anyLong())).thenReturn(skier);

        mockMvc.perform(put("/skier/assignToPiste/1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numSkier").value(skier.getNumSkier()));
    }

    @Test
    void testRetrieveSkiersBySubscriptionType() throws Exception {
        when(skierServices.retrieveSkiersBySubscriptionType(TypeSubscription.ANNUAL)).thenReturn(Collections.singletonList(skier));

        mockMvc.perform(get("/skier/getSkiersBySubscription")
                        .param("typeSubscription", TypeSubscription.ANNUAL.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numSkier").value(skier.getNumSkier()));
    }

    @Test
    void testGetById() throws Exception {
        when(skierServices.retrieveSkier(anyLong())).thenReturn(skier);

        mockMvc.perform(get("/skier/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numSkier").value(skier.getNumSkier()));
    }

    @Test
    void testDeleteById() throws Exception {
        mockMvc.perform(delete("/skier/delete/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllSkiers() throws Exception {
        List<Skier> skiList = Collections.singletonList(skier);
        when(skierServices.retrieveAllSkiers()).thenReturn(skiList);

        mockMvc.perform(get("/skier/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numSkier").value(skier.getNumSkier()));
    }
}
