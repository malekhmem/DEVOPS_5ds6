package tn.esprit.spring.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Color;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.services.IPisteServices;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PisteRestControllerTest {

    @Mock
    private IPisteServices pisteServices;

    @InjectMocks
    private PisteRestController pisteRestController;

    public PisteRestControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddPiste() {
        // Création d'une piste factice
        Piste piste = new Piste();
        piste.setNumPiste(1L);
        piste.setNamePiste("Piste Verte");
        piste.setColor(Color.GREEN);
        piste.setLength(1500);
        piste.setSlope(10);

        // Définir le comportement du mock
        when(pisteServices.addPiste(piste)).thenReturn(piste);

        // Appel de la méthode du contrôleur
        Piste result = pisteRestController.addPiste(piste);

        // Vérification du résultat
        assertEquals(piste, result);

        // Vérification de l'interaction avec le mock
        verify(pisteServices).addPiste(piste);
    }
}
