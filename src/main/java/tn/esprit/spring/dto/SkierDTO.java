package tn.esprit.spring.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.entities.Subscription;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level=AccessLevel.PRIVATE)
public class SkierDTO {
    Long numSkier; // Optional, if you want to expose it on creation/update
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    String city;
    Subscription subscription;
    Set<Registration> registrations;
    // You can include other fields as necessary, such as Subscription details or any additional properties.
}
