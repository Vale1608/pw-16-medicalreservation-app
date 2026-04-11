USE medical_reservation;

-- =====================================
-- SPECIALIZATIONS
-- =====================================

INSERT IGNORE INTO Specialization (name, description)
VALUES ('Cardiology', 'Diagnosis and treatment of heart diseases');

INSERT IGNORE INTO Specialization (name, description)
VALUES ('Dermatology', 'Diagnosis and treatment of skin diseases');

INSERT IGNORE INTO Specialization (name, description)
VALUES ('Neurology', 'Diagnosis and treatment of nervous system disorders');

INSERT IGNORE INTO Specialization (name, description)
VALUES ('Orthopedics', 'Diagnosis and treatment of musculoskeletal system disorders');

INSERT IGNORE INTO Specialization (name, description)
VALUES ('Pediatrics', 'Medical care for infants, children and adolescents');

-- =====================================
-- DOCTORS
-- =====================================

INSERT IGNORE INTO Doctor (first_name, last_name, email, phone, specialization_id)
VALUES ('Luca', 'Rossi', 'luca.rossi@medicalreservation.com', '3331001001',
        (SELECT specialization_id FROM Specialization WHERE name = 'Cardiology'));

INSERT IGNORE INTO Doctor (first_name, last_name, email, phone, specialization_id)
VALUES ('Maria', 'Bianchi', 'maria.bianchi@medicalreservation.com', '3331001002',
        (SELECT specialization_id FROM Specialization WHERE name = 'Dermatology'));

INSERT IGNORE INTO Doctor (first_name, last_name, email, phone, specialization_id)
VALUES ('Giovanni', 'Verdi', 'giovanni.verdi@medicalreservation.com', '3331001003',
        (SELECT specialization_id FROM Specialization WHERE name = 'Neurology'));

INSERT IGNORE INTO Doctor (first_name, last_name, email, phone, specialization_id)
VALUES ('Elena', 'Ferrari', 'elena.ferrari@medicalreservation.com', '3331001004',
        (SELECT specialization_id FROM Specialization WHERE name = 'Orthopedics'));

INSERT IGNORE INTO Doctor (first_name, last_name, email, phone, specialization_id)
VALUES ('Chiara', 'Romano', 'chiara.romano@medicalreservation.com', '3331001005',
        (SELECT specialization_id FROM Specialization WHERE name = 'Pediatrics'));

-- =====================================
-- PATIENTS
-- =====================================

INSERT IGNORE INTO Patient (first_name, last_name, tax_code, email, phone, birth_date)
VALUES ('Marco', 'Neri', 'MRONER90A01H501Z', 'marco.neri@email.com', '3332001001', '1990-01-01');

INSERT IGNORE INTO Patient (first_name, last_name, tax_code, email, phone, birth_date)
VALUES ('Anna', 'Russo', 'ANNRSS85B02H501Y', 'anna.russo@email.com', '3332001002', '1985-02-02');

INSERT IGNORE INTO Patient (first_name, last_name, tax_code, email, phone, birth_date)
VALUES ('Paolo', 'Galli', 'PLLGLL92C15H501X', 'paolo.galli@email.com', '3332001003', '1992-03-15');

-- =====================================
-- APPOINTMENTS
-- =====================================

INSERT IGNORE INTO Appointment (appointment_date, appointment_time, status, notes, created_at, patient_id, doctor_id)
VALUES ('2026-03-25', '09:00:00', 'BOOKED', 'First cardiology consultation', '2026-03-20 10:15:00',
        (SELECT patient_id FROM Patient WHERE tax_code = 'MRONER90A01H501Z'),
        (SELECT doctor_id FROM Doctor WHERE email = 'luca.rossi@medicalreservation.com'));

INSERT IGNORE INTO Appointment (appointment_date, appointment_time, status, notes, created_at, patient_id, doctor_id)
VALUES ('2026-03-26', '11:30:00', 'BOOKED', 'Skin rash follow-up', '2026-03-20 11:00:00',
        (SELECT patient_id FROM Patient WHERE tax_code = 'ANNRSS85B02H501Y'),
        (SELECT doctor_id FROM Doctor WHERE email = 'maria.bianchi@medicalreservation.com'));