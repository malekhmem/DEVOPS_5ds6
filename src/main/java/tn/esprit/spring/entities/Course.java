package tn.esprit.spring.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Course implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long numCourse;

	int level;

	@Enumerated(EnumType.STRING)
	TypeCourse typeCourse;

	@Enumerated(EnumType.STRING)
	Support support;

	Float price;

	int timeSlot;

	// Make registrations private, lazy load it, and ignore during serialization
	@JsonIgnore
	@OneToMany(mappedBy = "course", fetch = FetchType.LAZY) // Lazy loading
	private Set<Registration> registrations; // Keep it private to encapsulate
}
