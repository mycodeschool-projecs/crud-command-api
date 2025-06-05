# CRUD Command API

## Overview
The CRUD Command API is a microservice responsible for managing client data and operations. It provides a RESTful API for creating, reading, updating, and deleting client records.

## Key Features
- Create new clients
- Update existing client information
- Retrieve client details by ID or email
- Delete clients
- List all clients
- Swagger/OpenAPI documentation

## Service Interactions
- **Auth Service**: The Command API is accessed through the Auth Service, which handles authentication and authorization before forwarding requests to this service.
- **Notifications Service**: When client data changes occur, notifications may be generated to inform users of these changes.

## Technologies
- Spring Boot
- Spring Data JPA
- RESTful API
- Swagger/OpenAPI for API documentation

## Running the Service
### Prerequisites
- Java 11 or higher
- Maven
- Database (configured in application properties)

### Local Development
1. Clone the repository
2. Navigate to the crud-command-api directory
3. Run `mvn spring-boot:run`
4. The service will be available at http://localhost:8080

### Docker
The service can also be run using Docker:
```
docker build -t crud-command-api .
docker run -p 8080:8080 crud-command-api
```

### Kubernetes
Kubernetes deployment configurations are available in the crud-app-k8s directory.

## API Documentation
Once the service is running, you can access the Swagger UI at:
http://localhost:8080/swagger-ui.html