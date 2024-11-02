package tn.esprit.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.InstructorDTO;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.services.IInstructorServices;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "\uD83D\uDC69\u200D\uD83C\uDFEB Instructor Management")
@RestController
@RequestMapping("/instructor")
@RequiredArgsConstructor
public class InstructorRestController {

    private final IInstructorServices instructorServices;

    @Operation(description = "Add Instructor")
    @PostMapping("/add")
    public InstructorDTO addInstructor(@RequestBody InstructorDTO instructorDTO){
        Instructor instructor = toEntity(instructorDTO);
        Instructor savedInstructor = instructorServices.addInstructor(instructor);
        return toDTO(savedInstructor);
    }

    @Operation(description = "Add Instructor and Assign To Course")
    @PutMapping("/addAndAssignToCourse/{numCourse}")
    public InstructorDTO addAndAssignToInstructor(@RequestBody InstructorDTO instructorDTO, @PathVariable("numCourse") Long numCourse){
        Instructor instructor = toEntity(instructorDTO);
        Instructor savedInstructor = instructorServices.addInstructorAndAssignToCourse(instructor, numCourse);
        return toDTO(savedInstructor);
    }

    @Operation(description = "Retrieve all Instructors")
    @GetMapping("/all")
    public List<InstructorDTO> getAllInstructors(){
        return instructorServices.retrieveAllInstructors().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Operation(description = "Update Instructor")
    @PutMapping("/update")
    public InstructorDTO updateInstructor(@RequestBody InstructorDTO instructorDTO){
        Instructor instructor = toEntity(instructorDTO);
        Instructor updatedInstructor = instructorServices.updateInstructor(instructor);
        return toDTO(updatedInstructor);
    }

    @Operation(description = "Retrieve Instructor by Id")
    @GetMapping("/get/{id-instructor}")
    public InstructorDTO getById(@PathVariable("id-instructor") Long numInstructor){
        Instructor instructor = instructorServices.retrieveInstructor(numInstructor);
        return toDTO(instructor);
    }

    // Helper methods to convert between Instructor and InstructorDTO
    private Instructor toEntity(InstructorDTO dto) {
        Instructor instructor = new Instructor();
        instructor.setId(dto.getId());
        instructor.setName(dto.getName());
        instructor.setExpertise(dto.getExpertise());
        // Map other fields as needed
        return instructor;
    }

    private InstructorDTO toDTO(Instructor instructor) {
        InstructorDTO dto = new InstructorDTO();
        dto.setId(instructor.getId());
        dto.setName(instructor.getName());
        dto.setExpertise(instructor.getExpertise());
        // Map other fields as needed
        return dto;
    }
}
