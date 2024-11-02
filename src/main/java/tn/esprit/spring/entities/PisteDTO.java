package tn.esprit.spring.entities;

import lombok.Data;

import java.util.Set;

@Data
public class PisteDTO {
    private Long id;  // Corresponds to numPiste in Piste
    private String name;
    private String color;
    private int length;
    private int slope;
    private Set<Long> skierIds;  // Represents skier IDs instead of full Skier objects
}
