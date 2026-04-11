document.addEventListener("DOMContentLoaded", () => {
    const registerForm = document.getElementById("registerForm");

    registerForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        const requestBody = {
            firstName: document.getElementById("firstName").value.trim(),
            lastName: document.getElementById("lastName").value.trim(),
            taxCode: document.getElementById("taxCode").value.trim(),
            email: document.getElementById("email").value.trim(),
            phone: document.getElementById("phone").value.trim(),
            birthDate: document.getElementById("birthDate").value || null,
            username: document.getElementById("username").value.trim(),
            password: document.getElementById("password").value,
            confirmPassword: document.getElementById("confirmPassword").value
        };

        if (!requestBody.firstName || !requestBody.lastName || !requestBody.taxCode ||
            !requestBody.email || !requestBody.username || !requestBody.password || !requestBody.confirmPassword) {
            showMessage("registerMessage", "Please fill in all required fields.", true);
            return;
        }

        if (requestBody.password !== requestBody.confirmPassword) {
            showMessage("registerMessage", "Passwords do not match.", true);
            return;
        }

        try {
            const response = await fetch(`${API_BASE_URL}/auth/register`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(requestBody)
            });

            const data = await response.json();

            if (!response.ok) {
                showMessage("registerMessage", data.message || "Registration failed.", true);
                return;
            }

            showMessage("registerMessage", "Registration successful. Redirecting to login...");

            setTimeout(() => {
                window.location.href = "login.html";
            }, 1200);

        } catch (error) {
            showMessage("registerMessage", "Unable to connect to the server.", true);
        }
    });
});