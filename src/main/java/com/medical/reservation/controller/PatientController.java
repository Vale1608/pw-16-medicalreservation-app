package com.medical.reservation.controller;

import com.medical.reservation.model.Patient;
import com.medical.reservation.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
@Tag(name = "Patients", description = "Patient management endpoints")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @Operation(summary = "Get authenticated patient profile")
    @GetMapping("/me")
    public Patient getCurrentPatient() {
        return patientService.getCurrentPatient();
    }

    @Operation(summary = "Get all patients")
    @GetMapping
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }

    @Operation(summary = "Get patient by id")
    @GetMapping("/{id}")
    public Patient getPatientById(@PathVariable Long id) {
        return patientService.getPatientById(id);
    }

    @Operation(summary = "Search patients by last name")
    @GetMapping("/lastname/{lastName}")
    public List<Patient> getPatientsByLastName(@PathVariable String lastName) {
        return patientService.getPatientsByLastName(lastName);
    }

    @Operation(summary = "Create a patient")
    @PostMapping
    public Patient createPatient(@RequestBody Patient patient) {
        return patientService.createPatient(patient);
    }

    @Operation(summary = "Update a patient")
    @PutMapping("/{id}")
    public Patient updatePatient(@PathVariable Long id, @RequestBody Patient patient) {
        return patientService.updatePatient(id, patient);
    }

    @Operation(summary = "Delete a patient")
    @DeleteMapping("/{id}")
    public void deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
    }
}