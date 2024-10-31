
# Keycloak Custom REST API for Credential Management

This repository implements a custom REST API to extend Keycloak’s default capabilities, focusing on credential management. The repository provides endpoints for listing user credentials accessible by standard clients, as well as admin-only endpoints for creating and listing user credentials.

## Features

- **Credential Listing**: Retrieve a list of credentials for a user, accessible by standard clients.
- **Admin Credential Management**: Create and retrieve credentials with elevated permissions restricted to admin clients.

This project was built referencing Keycloak’s official documentation on custom REST endpoints: [Add custom REST endpoints](https://www.keycloak.org/docs/latest/server_development/index.html#_extensions_rest).

## Prerequisites

- **Keycloak Version**: This implementation requires Keycloak v22 or later.
- **Java**: Java 17 is required for compatibility with Keycloak v22.
- **Maven**: Ensure Maven is installed to build the project.

For local development and testing, Docker is recommended for running Keycloak.

## Getting Started

1. **Clone the Repository**:

   ```bash
   git clone https://github.com/YamazakiNorihito/keycloak-custom-rest-endpoint.git
   cd keycloak-custom-rest-endpoint
   ```

2. **Build the Project**:

   ```bash
   mvn clean package
   ```

3. **Run with Docker**:
   Use the provided `compose.yml` file to start Keycloak in a Docker container:

   ```bash
   docker-compose up
   ```

4. **Access Keycloak**:
   The application should now be running at `http://localhost:8080`. Configure the API endpoints as needed in your Keycloak admin console.

## API Endpoints

### Admin Endpoints (Admin Client Required)

- **Get Credential**  
  Retrieves all credentials for a specific user.

  ```http
  GET {{baseuri}}/admin/realms/{{target_realm}}/credential-admin-api/users/{user_id}/credentials
  Authorization: Bearer {{access_token}}
  ```

- **Create Credential**  
  Adds a credential to a specific user.

  ```http
  POST {{baseuri}}/admin/realms/{{target_realm}}/credential-admin-api/users/{user_id}/credentials
  Authorization: Bearer {{access_token}}
  Content-Type: application/json
  
  {
    "question": "What is your favorite color?",
    "answer": "Blue"
  }
  ```

### Non-Admin Endpoints (Standard Client Access)

- **Get Credential**  
  Allows a standard client to retrieve a user’s credential information.

  ```http
  GET {{baseuri}}/realms/{{target_realm}}/credential-api/users/{user_id}/credentials
  Authorization: Bearer {{access_token}}
  ```

## Project Structure

The repository’s structure is organized as follows:

```plaintext
.
├── Dockerfile
├── README.md
├── cache-ispn-jdbc-ping-mysql.xml
├── compose.yml
├── custom-rest-api
│   ├── pom.xml
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── example
│       │   │           └── keycloak
│       │   │               └── rest
│       │   │                   ├── UserCredentialAdminResource.java
│       │   │                   ├── UserCredentialRestAdminProvider.java
│       │   │                   ├── UserCredentialRestAdminProviderFactory.java
│       │   │                   ├── UserCredentialRestProvider.java
│       │   │                   ├── UserCredentialRestProviderFactory.java
│       │   │                   └── credential
│       │   │                       ├── SecretQuestionCredentialModel.java
│       │   │                       └── dto
│       │   │                           ├── SecretQuestionCredentialData.java
│       │   │                           └── SecretQuestionSecretData.java
│       └── resources
│           └── META-INF
│               ├── beans.xml
│               └── services
│                   ├── org.keycloak.services.resource.RealmResourceProviderFactory
│                   └── org.keycloak.services.resources.admin.ext.AdminRealmResourceProviderFactory
│       └── test
│           └── java
│               └── com
│                   └── example
│                       └── keycloak
│                           └── AppTest.java
├── keycloak-admin.http
└── keycloak-non-admin.http
```

## Related Repositories

- [Keycloak Authentication SPI](https://github.com/YamazakiNorihito/keycloak-authentication-spi)

## Disclaimer

This project is intended for educational and development purposes and may not be secure for production use. The code in this repository may not meet security and authorization requirements, so please ensure thorough security reviews and testing before deploying it in a production environment.
