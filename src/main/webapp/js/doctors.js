document.addEventListener("DOMContentLoaded", async () => {
    requireAuth();

    const specializationSelect = document.getElementById("specializationSelect");
    const closeModalButton = document.getElementById("closeModalButton");
    const bookingForm = document.getElementById("bookingForm");

    await loadSpecializations();
    await loadDoctors();

    specializationSelect.addEventListener("change", async () => {
        const specializationId = specializationSelect.value;
        await loadDoctors(specializationId);
    });

    closeModalButton.addEventListener("click", closeBookingModal);

    bookingForm.addEventListener("submit", async (event) => {
        event.preventDefault();
        await createAppointment();
    });
});

async function loadSpecializations() {
    try {
        const response = await fetch(`${API_BASE_URL}/specializations`, {
            method: "GET",
            headers: getAuthHeaders()
        });

        const data = await response.json();

        if (!response.ok) {
            showMessage("doctorMessage", data.message || "Unable to load specializations.", true);
            return;
        }

        const specializationSelect = document.getElementById("specializationSelect");

        data.forEach((specialization) => {
            const option = document.createElement("option");
            option.value = specialization.specializationId;
            option.textContent = specialization.name;
            specializationSelect.appendChild(option);
        });

    } catch (error) {
        showMessage("doctorMessage", "Unable to connect to the server.", true);
    }
}

async function loadDoctors(specializationId = "") {
    const doctorsContainer = document.getElementById("doctorsContainer");
    doctorsContainer.innerHTML = "";

    let endpoint = `${API_BASE_URL}/doctors`;

    if (specializationId) {
        endpoint = `${API_BASE_URL}/doctors/specialization/${specializationId}`;
    }

    try {
        const response = await fetch(endpoint, {
            method: "GET",
            headers: getAuthHeaders()
        });

        const data = await response.json();

        if (!response.ok) {
            showMessage("doctorMessage", data.message || "Unable to load doctors.", true);
            return;
        }

        if (!Array.isArray(data) || data.length === 0) {
            doctorsContainer.innerHTML = `
                <div class="empty-state">
                    <p>No doctors found for the selected specialization.</p>
                </div>
            `;
            return;
        }

        data.forEach((doctor) => {
            const card = document.createElement("div");
            card.className = "doctor-card";

            const specializationName = doctor.specialization
                ? doctor.specialization.name
                : "N/A";

            card.innerHTML = `
                <h3>Dr. ${doctor.firstName} ${doctor.lastName}</h3>
                <p><strong>Specialization:</strong> ${specializationName}</p>
                <p><strong>Email:</strong> ${doctor.email || "-"}</p>
                <p><strong>Phone:</strong> ${doctor.phone || "-"}</p>
                <button type="button" class="book-button">Book Appointment</button>
            `;

            const bookButton = card.querySelector(".book-button");
            bookButton.addEventListener("click", () => {
                openBookingModal(doctor);
            });

            doctorsContainer.appendChild(card);
        });

    } catch (error) {
        showMessage("doctorMessage", "Unable to connect to the server.", true);
    }
}

function openBookingModal(doctor) {
    const bookingModal = document.getElementById("bookingModal");
    const doctorId = document.getElementById("doctorId");
    const doctorName = document.getElementById("doctorName");
    const bookingMessage = document.getElementById("bookingMessage");

    doctorId.value = doctor.doctorId;
    doctorName.value = `Dr. ${doctor.firstName} ${doctor.lastName}`;
    bookingMessage.innerHTML = "";

    bookingModal.classList.remove("hidden");
}

function closeBookingModal() {
    const bookingModal = document.getElementById("bookingModal");
    const bookingForm = document.getElementById("bookingForm");
    const bookingMessage = document.getElementById("bookingMessage");

    bookingForm.reset();
    document.getElementById("doctorId").value = "";
    bookingMessage.innerHTML = "";
    bookingModal.classList.add("hidden");
}

async function createAppointment() {
  const doctorId = document.getElementById("doctorId").value;
  const appointmentDate = document.getElementById("appointmentDate").value;
  const appointmentTime = document.getElementById("appointmentTime").value;
  const appointmentNotes = document.getElementById("appointmentNotes").value.trim();

  if (!doctorId || !appointmentDate || !appointmentTime) {
    showMessage("bookingMessage", "Date and time are required.", true);
    return;
  }

  const selectedDate = new Date(`${appointmentDate}T00:00:00`);
  const day = selectedDate.getDay(); // 0 domenica, 6 sabato

  if (day === 0 || day === 6) {
    showMessage("bookingMessage", "Appointments are available only from Monday to Friday.", true);
    return;
  }

  const allowedSlots = ["09:00", "10:00", "11:00", "12:00", "14:00", "15:00", "16:00", "17:00"];
  if (!allowedSlots.includes(appointmentTime)) {
    showMessage("bookingMessage", "Please select a valid one-hour slot.", true);
    return;
  }

  try {
    const patientResponse = await fetch(`${API_BASE_URL}/patients/me`, {
      method: "GET",
      headers: getAuthHeaders()
    });

    const patientData = await patientResponse.json();

    if (!patientResponse.ok) {
      showMessage(
        "bookingMessage",
        patientData.message || "Unable to identify the authenticated patient.",
        true
      );
      return;
    }

    const appointmentPayload = {
      appointmentDate: appointmentDate,
      appointmentTime: `${appointmentTime}:00`,
      status: "BOOKED",
      notes: appointmentNotes,
      patient: {
        patientId: patientData.patientId
      },
      doctor: {
        doctorId: Number(doctorId)
      }
    };

    const response = await fetch(`${API_BASE_URL}/appointments`, {
      method: "POST",
      headers: getAuthHeaders(),
      body: JSON.stringify(appointmentPayload)
    });

    const data = await response.json();

    if (!response.ok) {
      showMessage("bookingMessage", data.message || "Unable to create appointment.", true);
      return;
    }

    showMessage("bookingMessage", "Appointment created successfully.");
    setTimeout(() => {
      window.location.href = "appointments.html";
    }, 1000);

  } catch (error) {
    showMessage("bookingMessage", "Unable to connect to the server.", true);
  }
}