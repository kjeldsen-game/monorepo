# Service to Service Communication

Service-to-service communication occurs when two internal modules interact with each other to request or share data.

The modules handle authentication using the **InternalClientTokenProvider**, which manages stored tokens.
If a valid token exists and has not expired, it is directly used in the `Authorization` header of the request.

If no valid token is available, the module first requests a token from the unprotected `/v1/auth/token-service` endpoint.
The returned token is then used for subsequent requests to the target service.

## Protected Service-to-Service Flow without valid Token

When no valid token exists in the cache, ServiceA must first request a new token from the Auth Service.
The new token is then stored in the cache and used to authenticate the request to ServiceB.

```mermaid
sequenceDiagram
participant ServiceA
participant AuthService as Auth Service
participant TokenProvider as InternalClientTokenProvider
participant ServiceB

    ServiceA->>TokenProvider: Request cached token
    TokenProvider-->>ServiceA: No valid token available

    ServiceA->>AuthService: Request new service token (/auth/service-token)
    AuthService-->>ServiceA: Return new token

    ServiceA->>TokenProvider: Store new token in cache
    TokenProvider-->>ServiceA: Token stored

    ServiceA->>ServiceB: Request with Authorization: Bearer <new token>
    ServiceB-->>ServiceA: HTTP 200 OK / Response
```

## Protected Service-to-Service Flow with valid Token

If a valid token is already available in the cache, ServiceA can use it directly in the request to ServiceB, skipping the call to the Auth Service.

```mermaid
sequenceDiagram
    participant ServiceA
    participant TokenProvider as InternalClientTokenProvider
    participant ServiceB

    ServiceA->>TokenProvider: Request cached token
    TokenProvider-->>ServiceA: Return valid token

    ServiceA->>ServiceB: Request with Authorization: Bearer <cached token>
    ServiceB-->>ServiceA: HTTP 200 OK / Response
```