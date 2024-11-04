package tn.esprit.spring.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.spring.entities.Color;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.services.IPisteServices;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.*;


@ExtendWith(MockitoExtension.class) // Assurez-vous que cette annotation est présente si nécessaire

@WebMvcTest(PisteRestController.class)
public class PisteRestController {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IPisteServices pisteServices;

    private Piste piste;

    @BeforeEach
    public void setUp() {
        piste = new Piste(1L, "Blue Piste", Color.BLUE, 1000, 30, null);
    }

    @Test
    public void testAddPiste() throws Exception {
        when(pisteServices.addPiste(any(Piste.class))).thenReturn(piste);

        mockMvc.perform(post("/piste/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"namePiste\": \"Blue Piste\", \"color\": \"BLUE\", \"length\": 1000, \"slope\": 30 }"))
                .andExpect(status().isCreated())  // Remplacez .isOk() par .isCreated() si votre API renvoie 201
                .andExpect(jsonPath("$.namePiste").value("Blue Piste"));
    }


    @Test
    public void testGetAllPistes() throws Exception {
        List<Piste> pistes = Arrays.asList(piste);
        when(pisteServices.retrieveAllPistes()).thenReturn(pistes);

        mockMvc.perform(get("/piste/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].namePiste").value("Blue Piste"));
    }



    @Test
    public void testGetById_NotFound() throws Exception {
        when(pisteServices.retrievePiste(1L)).thenReturn(null);

        mockMvc.perform(get("/piste/get/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteById() throws Exception {
        // Simule le comportement de la méthode removePiste dans le service
        doNothing().when(pisteServices).removePiste(1L);

        // Effectue une requête DELETE vers l'URL exacte du contrôleur
        mockMvc.perform(delete("/piste/delete/1"))
                .andExpect(status().isNoContent()); // Vérifie que le statut est 204 No Content
    }

}

