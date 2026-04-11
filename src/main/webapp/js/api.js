function saveToken(token) {
    localStorage.setItem("token", token);
}

function getToken() {
    return localStorage.getItem("token");
}

function removeToken() {
    localStorage.removeItem("token");
}

function saveLoggedUser(user) {
    localStorage.setItem("loggedUser", JSON.stringify(user));
}

function getLoggedUser() {
    const user = localStorage.getItem("loggedUser");
    return user ? JSON.parse(user) : null;
}

function removeLoggedUser() {
    localStorage.removeItem("loggedUser");
}

function getAuthHeaders() {
    const token = getToken();

    const headers = {
        "Content-Type": "application/json"
    };

    if (token) {
        headers["Authorization"] = "Bearer " + token;
    }

    return headers;
}

function logout() {
    removeToken();
    removeLoggedUser();
    window.location.href = "login.html";
}

function requireAuth() {
    const token = getToken();

    if (!token) {
        window.location.href = "login.html";
    }
}

function showMessage(elementId, message, isError = false) {
    const element = document.getElementById(elementId);

    if (!element) {
        return;
    }

    element.textContent = message;
    element.className = isError ? "message error" : "message success";
}