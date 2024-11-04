package tn.esprit.spring;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.repositories.IInstructorRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@SpringBootTest
@Slf4j
@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
 class InstructorRepositoryTest {
    @Autowired
    IInstructorRepository instructorRepository;
    static Instructor i = Instructor.builder().firstName("foulen").courses(new HashSet<>()).build();

    @Test
    @Order(1)
     void ajouterInstructor(){
        i = instructorRepository.save(i);
        log.info("id :"+i.getNumInstructor());
        log.info("ajout ==> "+ i.toString());
        Assertions.assertNotNull(i.getNumInstructor());
        Assertions.assertNotEquals(0,i.getNumInstructor());

    }

    @Test
    @Order(2)
     void modifierInstructor(){
        log.info("id :"+i.getNumInstructor());
        i.setFirstName("modif_foulen");
        i = instructorRepository.save(i);
        log.info("Modif ==> " + i);
        Assertions.assertNotEquals("foulen", i.getFirstName());
        Assertions.assertSame("modif_foulen", i.getFirstName());
    }

    @Test
    @Order(3)
     void trouverInstructor(){
        List<Instructor> instructors = new ArrayList<>();
        instructorRepository.findAll().forEach(instructors::add);
        log.info("find all ==> " + instructors.size());
        Assertions.assertTrue(instructors.size()>0);
    }

    @Test
    @Order(4)
     void chercherInstructor(){
        log.info("Chercher ==> " + i.getNumInstructor());
        Instructor instructor ;
        instructor = instructorRepository.findById(i.getNumInstructor()).get();
        Assertions.assertEquals(instructor.getNumInstructor(),i.getNumInstructor());
    }


    @Test
    @Order(5)
     void count(){
        long x = instructorRepository.count();
        log.info("count ==> " + x);
        Assertions.assertEquals(x, instructorRepository.findAll().size());
    }

    @Test
    @Order(6)
     void supprimerInstructor(){
        instructorRepository.deleteById(i.getNumInstructor());
        Instructor ins = instructorRepository.findById(i.getNumInstructor()).orElse(null);
        Assertions.assertNull(ins);
        log.info("Supprimer");
    }


}
