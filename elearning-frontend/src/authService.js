import keycloak from "./keycloak";

// Récupérer les infos utilisateur depuis Keycloak
export const getUserInfo = async () => {
    const token = keycloak.token;
    const userInfo = await fetch(
        "http://localhost:8080/realms/elearning-realm/protocol/openid-connect/userinfo",
        {
            headers: { Authorization: `Bearer ${token}` },
        }
    );
    return await userInfo.json();
};

// Récupérer les rôles directement depuis le token Keycloak
export const getBackendRoles = () => {
    if (!keycloak.tokenParsed) return { roles: [] };

    // Les rôles se trouvent souvent dans realm_access.roles
    const realmRoles = keycloak.tokenParsed.realm_access?.roles || [];
    return { roles: realmRoles };
};
