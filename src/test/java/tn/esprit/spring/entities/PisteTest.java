package tn.esprit.spring.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

public class PisteTest {

    private Piste piste;

    @BeforeEach
    public void setUp() {
        // Initialize a new Piste object before each test
        piste = new Piste();
    }

    @Test
    public void testNumPiste() {
        Long numPiste = 1L;
        piste.setNumPiste(numPiste);
        assertEquals(numPiste, piste.getNumPiste());
    }

    @Test
    public void testNamePiste() {
        String name = "Green Piste";
        piste.setNamePiste(name);
        assertEquals(name, piste.getNamePiste());
    }

    @Test
    public void testColor() {
        Color color = Color.GREEN;
        piste.setColor(color);
        assertEquals(color, piste.getColor());
    }

    @Test
    public void testLength() {
        int length = 1200;
        piste.setLength(length);
        assertEquals(length, piste.getLength());
    }

    @Test
    public void testSlope() {
        int slope = 30;
        piste.setSlope(slope);
        assertEquals(slope, piste.getSlope());
    }

    @Test
    public void testSkiers() {
        Set<Skier> skiers = new HashSet<>();
        Skier skier1 = new Skier();
        skier1.setNumSkier(1L);
        Skier skier2 = new Skier();
        skier2.setNumSkier(2L);

        skiers.add(skier1);
        skiers.add(skier2);

        piste.setSkiers(skiers);
        assertEquals(skiers, piste.getSkiers());
    }
}
