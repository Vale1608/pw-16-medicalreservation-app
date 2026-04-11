document.addEventListener("DOMContentLoaded", async () => {
    requireAuth();
    await loadAppointments();
});

async function loadAppointments() {
    const appointmentsContainer = document.getElementById("appointmentsContainer");
    appointmentsContainer.innerHTML = "";

    try {
        const response = await fetch(`${API_BASE_URL}/appointments`, {
            method: "GET",
            headers: getAuthHeaders()
        });

        const data = await response.json();

        if (!response.ok) {
            showMessage("appointmentsMessage", data.message || "Unable to load appointments.", true);
            return;
        }

        if (!Array.isArray(data) || data.length === 0) {
            appointmentsContainer.innerHTML = `
                <div class="empty-state">
                    <p>No appointments found.</p>
                </div>
            `;
            return;
        }

        const tableWrapper = document.createElement("div");
        tableWrapper.className = "table-wrapper";

        let tableHtml = `
            <table class="appointments-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Date</th>
                        <th>Time</th>
                        <th>Doctor</th>
                        <th>Status</th>
                        <th>Notes</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
        `;

        data.forEach((appointment) => {
            const doctorName = appointment.doctor
                ? `Dr. ${appointment.doctor.firstName} ${appointment.doctor.lastName}`
                : "-";

            tableHtml += `
                <tr>
                    <td>${appointment.appointmentId}</td>
                    <td>${appointment.appointmentDate || "-"}</td>
                    <td>${appointment.appointmentTime || "-"}</td>
                    <td>${doctorName}</td>
                    <td>${appointment.status || "-"}</td>
                    <td>${appointment.notes || "-"}</td>
                    <td>
                        ${appointment.status === "BOOKED"
                ? `<button class="small-danger-button" onclick="cancelAppointment(${appointment.appointmentId})">Cancel</button>`
                : `<span>-</span>`
            }
                    </td>
                </tr>
            `;
        });

        tableHtml += `
                </tbody>
            </table>
        `;

        tableWrapper.innerHTML = tableHtml;
        appointmentsContainer.appendChild(tableWrapper);

    } catch (error) {
        showMessage("appointmentsMessage", "Unable to connect to the server.", true);
    }
}

async function cancelAppointment(appointmentId) {
    const confirmed = window.confirm("Do you want to cancel this appointment?");

    if (!confirmed) {
        return;
    }

    try {
        const response = await fetch(`${API_BASE_URL}/appointments/${appointmentId}/cancel`, {
            method: "PUT",
            headers: getAuthHeaders()
        });

        const data = await response.json();

        if (!response.ok) {
            showMessage("appointmentsMessage", data.message || "Unable to cancel appointment.", true);
            return;
        }

        showMessage("appointmentsMessage", "Appointment cancelled successfully.");
        await loadAppointments();

    } catch (error) {
        showMessage("appointmentsMessage", "Unable to connect to the server.", true);
    }
}