package com.medical.reservation.dto;

public class AuthResponse {

    private String message;
    private Long userId;
    private Long patientId;
    private String username;
    private String role;

    public AuthResponse() {
    }

    public AuthResponse(String message, Long userId, Long patientId, String username, String role) {
        this.message = message;
        this.userId = userId;
        this.patientId = patientId;
        this.username = username;
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}