# Security Overview

Our application uses **JWT Bearer Tokens** for authentication and **Spring Security** to enforce access control. 
All protected endpoints require a valid token to access, and the security configuration is handled centrally using Spring Security mechanisms.

## Authentication Service

The application includes an **Authentication Module** responsible for issuing and validating JWT tokens. 
This service manages user authentication and generates tokens that clients must include in the `Authorization` header 
when accessing protected endpoints.