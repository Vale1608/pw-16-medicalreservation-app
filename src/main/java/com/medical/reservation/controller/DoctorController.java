package com.medical.reservation.controller;

import com.medical.reservation.model.Doctor;
import com.medical.reservation.service.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "*")
@Tag(name = "Doctors", description = "Doctor management endpoints")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @Operation(summary = "Get all doctors")
    @GetMapping
    public List<Doctor> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @Operation(summary = "Get doctor by id")
    @GetMapping("/{id}")
    public Doctor getDoctorById(@PathVariable Long id) {
        return doctorService.getDoctorById(id);
    }

    @Operation(summary = "Search doctors by last name")
    @GetMapping("/lastname/{lastName}")
    public List<Doctor> getDoctorsByLastName(@PathVariable String lastName) {
        return doctorService.getDoctorsByLastName(lastName);
    }

    @Operation(summary = "Get doctors by specialization id")
    @GetMapping("/specialization/{specializationId}")
    public List<Doctor> getDoctorsBySpecializationId(@PathVariable Long specializationId) {
        return doctorService.getDoctorsBySpecializationId(specializationId);
    }

    @Operation(summary = "Get doctors by specialization name")
    @GetMapping("/specialization/name/{name}")
    public List<Doctor> getDoctorsBySpecializationName(@PathVariable String name) {
        return doctorService.getDoctorsBySpecializationName(name);
    }

    @Operation(summary = "Create a doctor")
    @PostMapping
    public Doctor createDoctor(@RequestBody Doctor doctor) {
        return doctorService.createDoctor(doctor);
    }

    @Operation(summary = "Update a doctor")
    @PutMapping("/{id}")
    public Doctor updateDoctor(@PathVariable Long id, @RequestBody Doctor doctor) {
        return doctorService.updateDoctor(id, doctor);
    }

    @Operation(summary = "Delete a doctor")
    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
    }
}