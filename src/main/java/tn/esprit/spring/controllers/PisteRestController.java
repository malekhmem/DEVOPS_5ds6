package tn.esprit.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.entities.PisteDTO;
import tn.esprit.spring.services.IPisteServices;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "\uD83C\uDFBF Piste Management")
@RestController
@RequestMapping("/piste")
@RequiredArgsConstructor
public class PisteRestController {

    private final IPisteServices pisteServices;

    @Operation(description = "Add Piste")
    @PostMapping("/add")
    public PisteDTO addPiste(@RequestBody PisteDTO pisteDTO) {
        Piste piste = toEntity(pisteDTO);
        Piste savedPiste = pisteServices.addPiste(piste);
        return toDTO(savedPiste);
    }

    @Operation(description = "Retrieve all Pistes")
    @GetMapping("/all")
    public List<PisteDTO> getAllPistes() {
        return pisteServices.retrieveAllPistes().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Operation(description = "Retrieve Piste by Id")
    @GetMapping("/get/{id-piste}")
    public PisteDTO getById(@PathVariable("id-piste") Long numPiste) {
        Piste piste = pisteServices.retrievePiste(numPiste);
        return toDTO(piste);
    }

    @Operation(description = "Delete Piste by Id")
    @DeleteMapping("/delete/{id-piste}")
    public void deleteById(@PathVariable("id-piste") Long numPiste) {
        pisteServices.removePiste(numPiste);
    }

    // Helper methods to convert between Piste and PisteDTO
    private Piste toEntity(PisteDTO dto) {
        Piste piste = new Piste();
        piste.setId(dto.getId());
        piste.setName(dto.getName());
        piste.setLevel(dto.getLevel());
        // Map other fields as necessary
        return piste;
    }

    private PisteDTO toDTO(Piste piste) {
        PisteDTO dto = new PisteDTO();
        dto.setId(piste.getNumPiste());
        dto.setName(piste.getNamePiste());
        dto.setSlope(piste.getSlope());
        // Map other fields as necessary
        return dto;
    }
}
