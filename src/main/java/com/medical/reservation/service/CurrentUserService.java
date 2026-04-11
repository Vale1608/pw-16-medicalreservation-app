package com.medical.reservation.service;

import com.medical.reservation.model.User;
import com.medical.reservation.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private final UserRepository userRepository;

    public CurrentUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new IllegalArgumentException("Authenticated user not found.");
        }

        return userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new IllegalArgumentException("Authenticated user not found."));
    }

    public boolean isAdmin() {
        return getCurrentUser().getRole() != null &&
                getCurrentUser().getRole().equalsIgnoreCase("ADMIN");
    }

    public Long getCurrentPatientId() {
        User user = getCurrentUser();

        if (user.getPatient() == null || user.getPatient().getPatientId() == null) {
            throw new IllegalArgumentException("The authenticated user is not associated with a patient.");
        }

        return user.getPatient().getPatientId();
    }
}