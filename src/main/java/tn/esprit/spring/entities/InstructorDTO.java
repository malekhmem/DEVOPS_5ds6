package tn.esprit.spring.entities;


import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
public class InstructorDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfHire;
    private Set<Long> courseIds; // Use course IDs instead of full Course objects to keep it lightweight
}