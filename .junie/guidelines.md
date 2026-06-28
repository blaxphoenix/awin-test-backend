# Junie Guidelines - awin-test-backend

This document defines the architectural patterns, coding standards, and best practices to be followed when working on the `awin-test-backend` project.

## Architectural Patterns

### Hexagonal Architecture (Package-by-Feature)
The project follows a hexagonal-like architecture organized by feature packages. Each feature (e.g., `user`, `todo`, `transaction`) resides in its own package under `com.example.awintestbackend`.

Inside each feature package, the following sub-packages must be maintained:
- `controller`: REST controllers and their specific DTOs.
- `service`: Business logic interfaces, implementations, and service-layer data classes.
- `repository`: Database adapters, Spring Data JPA repositories, JPA entities, and repository-layer DTOs.

### Strict Layering and Mappings
To maintain decoupling, each layer must use its own data models. Cross-layer mapping is mandatory and should be implemented using **MapStruct**:

1.  **Controller Layer**:
    - Uses `*ControllerDto` (Java Records) for requests and responses.
    - Responsible for validation (`jakarta.validation`).
    - Maps `*ControllerDto` to/from `*Data` (Service layer) using `*Mapper`.
2.  **Service Layer**:
    - Uses `*Service` (interface) and `*ServiceImpl` (implementation).
    - Uses `*Data` (Java Records) as the primary data exchange format.
    - Contains business logic and `@Transactional` boundaries.
    - Maps `*Data` to/from `*RepositoryDto` (Repository layer) using `*Mapper`.
3.  **Repository Layer**:
    - Uses `*Adapter` as the implementation of the persistence port (though ports are currently implicit via direct calls to the adapter from the service).
    - Uses `*RepositoryDto` (Java Records) to exchange data with the service.
    - Uses `*Entity` (JPA Entity) for database persistence.
    - `*Adapter` maps `*RepositoryDto` to/from `*Entity` using `*Mapper`.

## Coding Standards

### Java and Spring
- **Java Version**: 25.
- **Spring Boot**: 4.1.0.
- **Records**: Use Java Records for all DTOs and data classes (`*ControllerDto`, `*Data`, `*RepositoryDto`).
- **Dependency Injection**: Use constructor injection exclusively. Use Lombok's `@Slf4j` for logging.
- **REST API**: 
    - Use `@RequestMapping(path = "/u2m/v{version}/...", version = "1")`.
    - Use `ProblemDetail` for error responses.

### Database
- **Migrations**: Use Flyway for all schema changes. Migration files are located in `src/main/resources/db/migration`.
- **Naming**: Follow the `V<N>__<description>.sql` convention.
- **Types**: Use `timestamptz` for all timestamp columns.
- **Schema**: The application uses the `awin_schema`.

## Error Handling
- Use `GlobalExceptionHandler` to handle exceptions.
- Throw `ResourceNotFoundException` for missing entities.
- Ensure all public API endpoints return `ProblemDetail` on error.

## Testing Strategy
- **Unit Tests**: Mandatory for Services and Adapters. Use Mockito for mocking dependencies.
- **Integration Tests**: 
    - Use `@WebMvcTest` or `@SpringBootTest` for controller/security integration.
    - Use H2 as the in-memory database for repository tests.
- **HTTP Files**: Maintain `.http` files in `src/test/resources/http` for manual endpoint verification.

## Logging
- Use a `CorrelationIdFilter` to ensure every request has a unique `correlation-id` in the MDC.
- Log meaningful business events in the Service layer.

## Development Workflow
- Always verify changes with existing tests: `mvn test`.
- Before adding new features, ensure the database migration is created if needed.
- Maintain the strict DTO separation even for simple CRUD operations to ensure architectural consistency.
