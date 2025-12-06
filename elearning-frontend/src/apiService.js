import axios from "axios";
import keycloak from "./keycloak";

const API_BASE = "http://localhost:8081/api";

const api = axios.create({
    baseURL: API_BASE,
});

// Intercepteur pour ajouter le token à chaque requête
api.interceptors.request.use(config => {
    const token = keycloak.token;
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
}, error => Promise.reject(error));

// Intercepteur pour gérer erreurs 401 / 403
api.interceptors.response.use(
    response => response,
    error => {
        if (error.response) {
            if (error.response.status === 401) {
                // Token invalide → forcer login
                keycloak.login();
            } else if (error.response.status === 403) {
                alert("Accès refusé : rôle insuffisant");
            }
        }
        return Promise.reject(error);
    }
);

export default api;
