# Changelog

All notable changes to this project will be documented in this file.  
This project adheres to [Semantic Versioning](https://semver.org/).

---

## [v1.0] - 2025-12-25
### Added
- Initial commit with complete **RBAC API** built on Java 21 and Spring Boot.
- CRUD operations for **Users, Roles, and Permissions**.
- Domain-driven **DTOs** for request/response handling.
- Custom **exceptions** and centralized error management.
- Professional **Javadoc documentation** for core classes.
- `.gitignore` configuration for IDEs, build artifacts, and YAML files.

### Notes
- This release establishes the foundation for secure authentication and authorization.
- Future versions will integrate **Spring Security** and **AI-powered features**.

---

## [v2.0] - 2025-12-25
### Added
- Implemented `AppUserDetails` to adapt `AppUser` entity to Spring Security.
- Created `AppUserDetailsService` to load users from database by email and enforce `ACTIVE` status.
- Added `AuthController` under `auth.infrastructure.rest` with `/api/v1/auth/login` and `/api/v1/auth/logout` endpoints.
- Updated `SecurityConfig` to enable **session-based authentication** with form login and logout.
- Integrated custom `UserDetailsService` into Spring Security filter chain.

### Notes
- This release introduces **session-based authentication** instead of JWT, aligned with project goals.
- Future iterations will expand role/permission mapping into `GrantedAuthority` for fine-grained authorization.

## [v2.1] - 2025-12-26
### Added
- Extended `AppUser` entity to expose assigned roles via `AppUserRole` relationship.
- Updated `AppUserDetails` to map roles into `GrantedAuthority` using `SimpleGrantedAuthority`.
- Enhanced `SecurityConfig` with role-based access rules:
  - `/api/v1/admin/**` restricted to `ROLE_ADMIN`.
  - `/api/v1/user/**` accessible to `ROLE_USER` and `ROLE_ADMIN`.

### Notes
- This release completes the integration of **role-based authorization**.
- Database cleanup and formal dataset seeding will follow to ensure consistent test users and roles.

## [v2.2] - 2025-12-26
### Added
- Created `AdminController` under `shared.infrastructure.rest` with `/api/v1/admin/test` endpoint.
- Created `UserController` under `shared.infrastructure.rest` with `/api/v1/user/test` endpoint.
- Created `AuthController` under `shared.infrastructure.rest` with `/api/v1/auth/status` endpoint to expose session state and authorities.

### Fixed
- Corrected `AppUserDetails` authority mapping to avoid duplicate `ROLE_` prefix.

### Notes
- This release validates the complete **authentication and authorization flow**:
  - Admin can access both `/admin/test` and `/user/test`.
  - User can only access `/user/test` and is forbidden from `/admin/test`.
- Endpoints now provide clear JSON responses for testing and demonstration purposes.
