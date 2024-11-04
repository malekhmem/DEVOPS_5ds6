package tn.esprit.spring;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.repositories.IInstructorRepository;
import tn.esprit.spring.services.IInstructorServices;
import tn.esprit.spring.services.InstructorServicesImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@ExtendWith(MockitoExtension.class)
public class InstructorServiceTest {
    @Mock
    private IInstructorRepository instructorRepository;
    Instructor ins = Instructor.builder().firstName("foulen").build();
    @InjectMocks
    private InstructorServicesImpl instructorServices;

    @Test
    void addDepartement() {
        Mockito.when(instructorRepository.save(Mockito.any(Instructor.class))).then(inv -> {
            Instructor i = inv.getArgument(0, Instructor.class);
            i.setNumInstructor(1L);
            return i;
        });
        log.info("Before : " + ins.getNumInstructor());
        Instructor instr = instructorServices.addInstructor(ins);
        Assertions.assertSame(instr, ins);
        log.info("After : " + ins.getNumInstructor());
        Mockito.verify(instructorRepository).save(ins);;
    }

    @Test
    void retrieveOneInstructorTest() {
        Mockito.when(instructorRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(new Instructor()));
        Instructor retrievedInstructor = instructorServices.retrieveInstructor(1L);
        Assertions.assertNotNull(retrievedInstructor);
        Mockito.verify(instructorRepository).findById(Mockito.anyLong());
    }

    @Test
    void retrieveAllInstructorsTest() {
        List<Instructor> instructors = new ArrayList<>();
        instructors.add(new Instructor());
        instructors.add(new Instructor());
        Mockito.when(instructorRepository.findAll()).thenReturn(instructors);
        List<Instructor> retrievedInstructors = instructorServices.retrieveAllInstructors();
        Assertions.assertNotNull(retrievedInstructors);
        Mockito.verify(instructorRepository).findAll();
    }
}

