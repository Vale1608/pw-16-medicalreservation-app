
-- ===============================
-- TABLE: Specialization
-- ===============================

CREATE TABLE IF NOT EXISTS Specialization (
    specialization_id BIGINT AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    PRIMARY KEY (specialization_id),
    UNIQUE KEY uk_specialization_name (name)
);

-- ===============================
-- TABLE: Doctor
-- ===============================

CREATE TABLE IF NOT EXISTS Doctor (
    doctor_id BIGINT AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    phone VARCHAR(20),
    specialization_id BIGINT NOT NULL,
    PRIMARY KEY (doctor_id),
    UNIQUE KEY uk_doctor_email (email),
    CONSTRAINT fk_doctor_specialization
        FOREIGN KEY (specialization_id)
        REFERENCES Specialization(specialization_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

-- ===============================
-- TABLE: Patient
-- ===============================

CREATE TABLE IF NOT EXISTS Patient (
    patient_id BIGINT AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    tax_code VARCHAR(16) NOT NULL,
    email VARCHAR(150) NOT NULL,
    phone VARCHAR(20),
    birth_date DATE,
    PRIMARY KEY (patient_id),
    UNIQUE KEY uk_patient_tax_code (tax_code),
    UNIQUE KEY uk_patient_email (email)
);

-- ===============================
-- TABLE: Appointment
-- ===============================

CREATE TABLE IF NOT EXISTS Appointment (
    appointment_id BIGINT AUTO_INCREMENT,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    status VARCHAR(30) NOT NULL,
    notes VARCHAR(500),
    created_at DATETIME NOT NULL,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    PRIMARY KEY (appointment_id),
    CONSTRAINT fk_appointment_patient
        FOREIGN KEY (patient_id)
        REFERENCES Patient(patient_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_appointment_doctor
        FOREIGN KEY (doctor_id)
        REFERENCES Doctor(doctor_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

-- ===============================
-- TABLE: User
-- ===============================

CREATE TABLE IF NOT EXISTS User (
                      user_id BIGINT AUTO_INCREMENT,
                      username VARCHAR(50) NOT NULL,
                      password VARCHAR(255) NOT NULL,
                      role VARCHAR(20) NOT NULL,
                      enabled BIT NOT NULL,
                      patient_id BIGINT,
                      PRIMARY KEY (user_id),
                      CONSTRAINT uk_user_username UNIQUE (username),
                      CONSTRAINT uk_user_patient UNIQUE (patient_id),
                      CONSTRAINT fk_user_patient
                          FOREIGN KEY (patient_id)
                              REFERENCES Patient(patient_id)
                              ON DELETE RESTRICT
                              ON UPDATE CASCADE
);