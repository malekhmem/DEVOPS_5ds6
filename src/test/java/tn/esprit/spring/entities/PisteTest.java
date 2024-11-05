package tn.esprit.spring.entities;

import org.junit.jupiter.api.Test;
import tn.esprit.spring.entities.Color;
import tn.esprit.spring.entities.Piste;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PisteTest {

    @Test
    public void testPisteCreation() {
        // Création d'une instance de Piste
        Piste piste = new Piste();
        piste.setNumPiste(1L);
        piste.setNamePiste("Piste Rouge");
        piste.setColor(Color.RED);
        piste.setLength(3000);
        piste.setSlope(20);

        // Vérification des valeurs des attributs
        assertEquals(1L, piste.getNumPiste());
        assertEquals("Piste Rouge", piste.getNamePiste());
        assertEquals(Color.RED, piste.getColor());
        assertEquals(3000, piste.getLength());
        assertEquals(20, piste.getSlope());
    }
}
