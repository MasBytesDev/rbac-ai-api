package com.masbytes.rbacapi.appuser.domain.service;

import com.masbytes.rbacapi.appuser.domain.mapper.AppUserMapper;
import com.masbytes.rbacapi.appuser.domain.dto.*;
import com.masbytes.rbacapi.appuser.domain.entity.AppUser;
import com.masbytes.rbacapi.appuser.domain.exception.AppUserNotFoundException;
import com.masbytes.rbacapi.appuser.domain.exception.EmailAlreadyExistsException;
import com.masbytes.rbacapi.appuser.domain.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service layer for managing application users.
 * <p>
 * Encapsulates business logic for creating, retrieving, updating, and changing
 * the status of users. Ensures validation, mapping, and transactional
 * consistency when interacting with the repository.
 */
@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserRepository repository;
    private final AppUserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * Creates a new user in the system. Validates that the email is unique,
     * encodes the password, and persists the user entity.
     *
     * @param request the request containing user details
     * @return the created user as a response DTO
     * @throws EmailAlreadyExistsException if the email is already registered
     */
    @Transactional
    public AppUserResponse createUser(CreateAppUserRequest request) {

        if (repository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        AppUser user = mapper.toEntity(request, encodedPassword);

        return mapper.toResponse(repository.save(user));
    }

    /**
     * Retrieves a user by their public UUID.
     *
     * @param publicId the unique public identifier of the user
     * @return the user as a response DTO
     * @throws AppUserNotFoundException if no user is found with the given ID
     */
    @Transactional(readOnly = true)
    public AppUserResponse getByPublicId(UUID publicId) {

        return repository.findByPublicId(publicId)
                .map(mapper::toResponse)
                .orElseThrow(() -> new AppUserNotFoundException(publicId));
    }

    /**
     * Updates the fullname of a user identified by their public UUID.
     *
     * @param publicId the unique public identifier of the user
     * @param request the request containing the new fullname
     * @return the updated user as a response DTO
     * @throws AppUserNotFoundException if no user is found with the given ID
     */
    @Transactional
    public AppUserResponse updateFullname(UUID publicId, UpdateAppUserFullnameRequest request) {

        AppUser user = repository.findByPublicId(publicId)
                .orElseThrow(() -> new AppUserNotFoundException(publicId));

        user.updateFullname(request.fullname());
        return mapper.toResponse(repository.save(user));
    }

    /**
     * Changes the status of a user identified by their public UUID.
     *
     * @param publicId the unique public identifier of the user
     * @param request the request containing the new status
     * @return the updated user as a response DTO
     * @throws AppUserNotFoundException if no user is found with the given ID
     */
    @Transactional
    public AppUserResponse changeStatus(UUID publicId, ChangeAppUserStatusRequest request) {

        AppUser user = repository.findByPublicId(publicId)
                .orElseThrow(() -> new AppUserNotFoundException(publicId));

        user.changeAppUserStatus(request.newStatus());
        return mapper.toResponse(repository.save(user));
    }
}
