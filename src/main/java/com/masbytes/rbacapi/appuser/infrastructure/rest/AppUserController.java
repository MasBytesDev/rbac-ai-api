package com.masbytes.rbacapi.appuser.infrastructure.rest;

import com.masbytes.rbacapi.appuser.domain.service.AppUserService;
import com.masbytes.rbacapi.appuser.domain.dto.*;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * REST controller for managing application users.
 * <p>
 * Exposes endpoints for creating, retrieving, updating, and changing the status
 * of users. Delegates business logic to AppUserService and ensures proper
 * request validation and response handling.
 */
@RestController
@RequestMapping("/api/v1/app-users")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService userService;

    /**
     * Creates a new user.
     *
     * @param request the request containing user details
     * @return ResponseEntity with the created user and HTTP 201 status
     */
    @PostMapping
    @PreAuthorize("hasAuthority('USER_WRITE') or hasRole('ADMIN')")
    public ResponseEntity<AppUserResponse> createUser(@Valid @RequestBody CreateAppUserRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    /**
     * Retrieves a user by their public UUID.
     *
     * @param publicId the unique public identifier of the user
     * @return ResponseEntity with the user details and HTTP 200 status
     */
    @GetMapping("/{publicId}")
    @PreAuthorize("hasAuthority('USER_READ')")
    public ResponseEntity<AppUserResponse> getByPublicId(@PathVariable UUID publicId) {
        return ResponseEntity.ok(userService.getByPublicId(publicId));
    }

    /**
     * Retrieves all users.
     *
     * @return ResponseEntity with the list of users and HTTP 200 status
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AppUserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Updates the fullname of a user.
     *
     * @param publicId the unique public identifier of the user
     * @param request the request containing the new fullname
     * @return ResponseEntity with the updated user and HTTP 200 status
     */
    @PatchMapping("/{publicId}/description")
    @PreAuthorize("hasAuthority('USER_WRITE')")
    public ResponseEntity<AppUserResponse> updateDescription(
            @PathVariable UUID publicId,
            @Valid @RequestBody UpdateAppUserFullnameRequest request) {
        return ResponseEntity.ok(userService.updateFullname(publicId, request));
    }

    /**
     * Changes the status of a user.
     *
     * @param publicId the unique public identifier of the user
     * @param request the request containing the new status
     * @return ResponseEntity with the updated user and HTTP 200 status
     */
    @PatchMapping("/{publicId}/status")
    @PreAuthorize("hasAuthority('ROLE_MANAGE') or hasRole('ADMIN')")
    public ResponseEntity<AppUserResponse> changeStatus(
            @PathVariable UUID publicId,
            @Valid @RequestBody ChangeAppUserStatusRequest request) {
        return ResponseEntity.ok(userService.changeStatus(publicId, request));
    }
}
