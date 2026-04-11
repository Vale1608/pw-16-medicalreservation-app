package com.medical.reservation.service;

import com.medical.reservation.dto.AuthResponse;
import com.medical.reservation.dto.LoginRequest;
import com.medical.reservation.dto.LoginResponse;
import com.medical.reservation.dto.RegisterRequest;
import com.medical.reservation.model.Patient;
import com.medical.reservation.model.User;
import com.medical.reservation.repository.PatientRepository;
import com.medical.reservation.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PatientRepository patientRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       CustomUserDetailsService customUserDetailsService,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse registerPatient(RegisterRequest request) {

        validateRegisterRequest(request);

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already in use.");
        }

        if (patientRepository.existsByTaxCode(request.getTaxCode())) {
            throw new IllegalArgumentException("A patient with this tax code already exists.");
        }

        if (patientRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("A patient with this email already exists.");
        }

        Patient patient = new Patient();
        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setTaxCode(request.getTaxCode());
        patient.setEmail(request.getEmail());
        patient.setPhone(request.getPhone());
        patient.setBirthDate(request.getBirthDate());

        Patient savedPatient = patientRepository.save(patient);

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("PATIENT");
        user.setEnabled(true);
        user.setPatient(savedPatient);

        User savedUser = userRepository.save(user);

        return new AuthResponse(
                "Registration completed successfully.",
                savedUser.getUserId(),
                savedPatient.getPatientId(),
                savedUser.getUsername(),
                savedUser.getRole()
        );
    }

    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password."));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtService.generateToken(userDetails);

        return new LoginResponse(
                token,
                "Bearer",
                user.getUserId(),
                user.getUsername(),
                user.getRole()
        );
    }

    private void validateRegisterRequest(RegisterRequest request) {

        if (request.getUsername() == null || request.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username is required.");
        }

        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required.");
        }

        if (request.getPassword().length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long.");
        }

        if (request.getConfirmPassword() == null || !request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Password and confirmPassword do not match.");
        }

        if (request.getFirstName() == null || request.getFirstName().isBlank()) {
            throw new IllegalArgumentException("First name is required.");
        }

        if (request.getLastName() == null || request.getLastName().isBlank()) {
            throw new IllegalArgumentException("Last name is required.");
        }

        if (request.getTaxCode() == null || request.getTaxCode().isBlank()) {
            throw new IllegalArgumentException("Tax code is required.");
        }

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required.");
        }
    }
}