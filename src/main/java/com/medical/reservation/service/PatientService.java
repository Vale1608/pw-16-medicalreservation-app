package com.medical.reservation.service;

import com.medical.reservation.exception.ResourceNotFoundException;
import com.medical.reservation.model.Patient;
import com.medical.reservation.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final CurrentUserService currentUserService;

    public PatientService(PatientRepository patientRepository,
                          CurrentUserService currentUserService) {
        this.patientRepository = patientRepository;
        this.currentUserService = currentUserService;
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient getPatientById(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient not found with id: " + patientId
                ));
    }

    public Patient getCurrentPatient() {
        Long currentPatientId = currentUserService.getCurrentPatientId();
        return getPatientById(currentPatientId);
    }

    public List<Patient> getPatientsByLastName(String lastName) {
        return patientRepository.findByLastNameContainingIgnoreCase(lastName);
    }

    public Patient createPatient(Patient patient) {
        if (patientRepository.existsByEmail(patient.getEmail())) {
            throw new IllegalArgumentException("A patient with this email already exists.");
        }

        if (patientRepository.existsByTaxCode(patient.getTaxCode())) {
            throw new IllegalArgumentException("A patient with this tax code already exists.");
        }

        return patientRepository.save(patient);
    }

    public Patient updatePatient(Long patientId, Patient updatedPatient) {
        Patient patient = getPatientById(patientId);

        patient.setFirstName(updatedPatient.getFirstName());
        patient.setLastName(updatedPatient.getLastName());
        patient.setTaxCode(updatedPatient.getTaxCode());
        patient.setEmail(updatedPatient.getEmail());
        patient.setPhone(updatedPatient.getPhone());
        patient.setBirthDate(updatedPatient.getBirthDate());

        return patientRepository.save(patient);
    }

    public void deletePatient(Long patientId) {
        Patient patient = getPatientById(patientId);
        patientRepository.delete(patient);
    }
}