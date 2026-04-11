package com.medical.reservation.controller;

import com.medical.reservation.model.Specialization;
import com.medical.reservation.service.SpecializationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specializations")
@CrossOrigin(origins = "*")
@Tag(name = "Specializations", description = "Specialization management endpoints")
public class SpecializationController {

    private final SpecializationService specializationService;

    public SpecializationController(SpecializationService specializationService) {
        this.specializationService = specializationService;
    }

    @Operation(summary = "Get all specializations")
    @GetMapping
    public List<Specialization> getAllSpecializations() {
        return specializationService.getAllSpecializations();
    }

    @Operation(summary = "Get specialization by id")
    @GetMapping("/{id}")
    public Specialization getSpecializationById(@PathVariable Long id) {
        return specializationService.getSpecializationById(id);
    }

    @Operation(summary = "Create a specialization")
    @PostMapping
    public Specialization createSpecialization(@RequestBody Specialization specialization) {
        return specializationService.createSpecialization(specialization);
    }

    @Operation(summary = "Update a specialization")
    @PutMapping("/{id}")
    public Specialization updateSpecialization(@PathVariable Long id,
                                               @RequestBody Specialization specialization) {
        return specializationService.updateSpecialization(id, specialization);
    }

    @Operation(summary = "Delete a specialization")
    @DeleteMapping("/{id}")
    public void deleteSpecialization(@PathVariable Long id) {
        specializationService.deleteSpecialization(id);
    }
}