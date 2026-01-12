# API Endpoint Types

Endpoints in the application can be categorized based on **access scope** and **authentication requirements**. The main types are **Public**, **Internal**, **Internal Protected**, and **Protected**.

## Unprotected Endpoints

These endpoints are accessible **without authentication**. Typical use cases include:

- API documentation (e.g., Swagger UI)
- Registration, login, and password reset endpoints

**Key points:**
- No JWT bearer token is required.
- No internal headers are required.
- Accessible by external users.

## Internal Unprotected Endpoints

These endpoints are intended **only for internal use** between services or trusted clients, and are not exposed to external users.

**Key points:**
- Requests **must include the special internal header** (e.g., `X-Internal-Request`) with the correct value.
- Requests with a missing or invalid internal header are automatically **rejected**.
- These endpoints **do not require a JWT bearer token**.

## Internal Protected Endpoints

These endpoints are used for **internal service-to-service communication** and require authentication.

**Key points:**
- Requests **must include a valid JWT bearer token** in the `Authorization` header.
- Requests **must include the internal header** (`X-Internal-Request`) for internal identification.
- Typically used when one service calls another (backend-to-backend communication).
- Requests without a valid token or missing internal header are rejected with `401 Unauthorized`.

## Protected Endpoints

These endpoints are accessible **both internally and externally**, but **always require authentication**.

**Key points:**
- Requests **must include a valid JWT bearer token** in the `Authorization` header.
- Internal requests **must also include the internal header** (`X-Internal-Request`).
- Provides controlled access for both users and internal services while maintaining security.