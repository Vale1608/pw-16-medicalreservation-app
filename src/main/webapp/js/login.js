document.addEventListener("DOMContentLoaded", () => {
    const loginForm = document.getElementById("loginForm");

    loginForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        const username = document.getElementById("username").value.trim();
        const password = document.getElementById("password").value.trim();

        if (!username || !password) {
            showMessage("loginMessage", "Username and password are required.", true);
            return;
        }

        try {
            const response = await fetch(`${API_BASE_URL}/auth/login`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    username: username,
                    password: password
                })
            });

            const data = await response.json();

            if (!response.ok) {
                showMessage("loginMessage", data.message || "Login failed.", true);
                return;
            }

            saveToken(data.token);
            saveLoggedUser({
                userId: data.userId,
                username: data.username,
                role: data.role
            });

            showMessage("loginMessage", "Login successful.");

            setTimeout(() => {
                window.location.href = "doctors.html";
            }, 700);

        } catch (error) {
            showMessage("loginMessage", "Unable to connect to the server.", true);
        }
    });
});