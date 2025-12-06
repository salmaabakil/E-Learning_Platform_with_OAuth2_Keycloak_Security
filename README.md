# E-Learning Platform with OAuth2 & Keycloak Security

## Description
Plateforme E-Learning sécurisée utilisant **React**, **Spring Boot** et **Keycloak**.  
- Authentification et gestion des rôles avec **OAuth2 / OpenID Connect**.  
- **STUDENT** : accès aux cours disponibles.  
- **ADMIN** : accès aux cours et gestion (ajout) des cours.  
- Communication sécurisée frontend ↔ backend via **JWT**.  
- Rafraîchissement automatique du token et gestion de session.  

---

## Architecture

Le schéma ci-dessous représente l’architecture de l’application :  

Keycloak (Identity Server)
|
| OAuth2 / OIDC (JWT)
|
React Frontend <-----> Spring Boot Backend

Login - Endpoints sécurisés

Affichage cours - Gestion des rôles et autorisations

Logout

---


*Remplace ce schéma par une image si tu veux plus de clarté : `architecture.png`*

---

## Fonctionnalités

### Pour STUDENT
- Visualisation des cours disponibles.
- Informations du profil récupérées depuis Keycloak.

### Pour ADMIN
- Visualisation des cours.
- Ajout de nouveaux cours via l’interface React.
- Gestion sécurisée des accès via les rôles.

---

## Installation et exécution

1. **Keycloak**
   - Installer Keycloak.
   - Créer un **realm** : `elearning-realm`.
   - Créer un **client** : `react-client` (type Public, Standard Flow, redirect URI: `http://localhost:3000/*`).
   - Créer deux rôles : `ROLE_STUDENT` et `ROLE_ADMIN`.
   - Créer deux utilisateurs : `user1` (STUDENT), `admin1` (ADMIN).

2. **Backend Spring Boot**
   - Configurer Spring Security avec OAuth2 Resource Server.
   - Endpoints :
     - `GET /courses` → STUDENT et ADMIN.
     - `POST /courses` → ADMIN uniquement.
     - `GET /me` → informations utilisateur + rôles.

3. **Frontend React**
   - Installer les dépendances :
     ```bash
     npm install
     ```
   - Lancer le projet :
     ```bash
     npm start
     ```

---

## Captures d’écran

### Connexion réussie
![WhatsApp Image 2025-12-05 at 22 51 17_5dfdc125](https://github.com/user-attachments/assets/46fb04c5-bcab-4c60-b4ad-a1288293599b)
![WhatsApp Image 2025-12-05 at 22 51 55_51593a88](https://github.com/user-attachments/assets/e3d1d5ef-ac54-4ab3-b0b8-b0072b93273f)


### Informations du profil avec Rôles affichés dans React

**STUDENT**
![WhatsApp Image 2025-12-06 at 13 11 07_43c9290d](https://github.com/user-attachments/assets/cd862dcc-bcc6-46f4-a621-94d517bfaf2d)

**ADMIN**
![WhatsApp Image 2025-12-06 at 13 11 57_1233c209](https://github.com/user-attachments/assets/a36caba8-c0ab-40a4-b3ae-53ca79a75bf0)



## Stack technique
- **Frontend :** React, keycloak-js, Axios  
- **Backend :** Spring Boot, Spring Security, OAuth2 Resource Server  
- **Authentification :** Keycloak (OAuth2 / OIDC)  
- **Communication sécurisée :** JWT  

---

## Auteur
Salma Abakil – Étudiante Ingénieure en Cybersécurité  
