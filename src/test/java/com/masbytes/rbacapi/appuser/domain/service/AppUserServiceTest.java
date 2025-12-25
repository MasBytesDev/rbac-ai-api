package com.masbytes.rbacapi.appuser.domain.service;

import com.masbytes.rbacapi.appuser.domain.mapper.AppUserMapper;
import com.masbytes.rbacapi.appuser.domain.dto.AppUserResponse;
import com.masbytes.rbacapi.appuser.domain.dto.CreateAppUserRequest;
import com.masbytes.rbacapi.appuser.domain.entity.AppUser;
import com.masbytes.rbacapi.appuser.domain.exception.AppUserNotFoundException;
import com.masbytes.rbacapi.appuser.domain.exception.EmailAlreadyExistsException;
import com.masbytes.rbacapi.appuser.domain.repository.AppUserRepository;
import com.masbytes.rbacapi.shared.domain.enums.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unused")
class AppUserServiceTest {

    @Mock
    private AppUserRepository repository;

    @Mock
    private AppUserMapper mapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AppUserService userService;

    private CreateAppUserRequest createRequest;
    private AppUser userEntity;
    private AppUserResponse userResponse;
    private UUID publicId;

    @BeforeEach
    void setUp() {
        publicId = UUID.randomUUID();
        createRequest = new CreateAppUserRequest("Juan Perez", "juan@example.com", "password123");

        userEntity = AppUser.builder()
                .fullname("Juan Perez")
                .email("juan@example.com")
                .passwordHash("encoded_password")
                .appUserStatus(Status.PENDING)
                .build();

        userResponse = new AppUserResponse(
                publicId,
                "Juan Perez",
                "juan@example.com",
                "PENDING",
                "2023-10-27T10:00:00.000Z",
                null
        );
    }

    @Test
    @DisplayName("Should create a user successfully")
    void createUser_Success() {
        // Arrange
        when(repository.existsByEmail(createRequest.email())).thenReturn(false);
        when(passwordEncoder.encode(createRequest.password())).thenReturn("encoded_password");
        when(mapper.toEntity(any(CreateAppUserRequest.class), anyString())).thenReturn(userEntity);
        when(repository.save(any(AppUser.class))).thenReturn(userEntity);
        when(mapper.toResponse(any(AppUser.class))).thenReturn(userResponse);

        // Act
        AppUserResponse result = userService.createUser(createRequest);

        // Assert
        assertNotNull(result);
        assertEquals(createRequest.email(), result.email());
        verify(repository).save(any(AppUser.class));
        verify(passwordEncoder).encode(anyString());
    }

    @Test
    @DisplayName("Should throw EmailAlreadyExistsException when email is taken")
    void createUser_EmailExists_ThrowsException() {
        // Arrange
        when(repository.existsByEmail(createRequest.email())).thenReturn(true);

        // Act & Assert
        // Capturamos la excepción en una variable para que el IDE vea que "usamos" el resultado
        EmailAlreadyExistsException exception = assertThrows(
                EmailAlreadyExistsException.class,
                () -> userService.createUser(createRequest)
        );

        // Opcional: Validar que el mensaje contiene el email (esto ya cuenta como "usar" el objeto)
        assertTrue(exception.getMessage().contains(createRequest.email()));
        verify(repository, never()).save(any(AppUser.class));
    }

    @Test
    @DisplayName("Should throw AppUserNotFoundException when user does not exist")
    void getByPublicId_NotFound_ThrowsException() {
        // Arrange
        when(repository.findByPublicId(publicId)).thenReturn(Optional.empty());

        // Act & Assert
        AppUserNotFoundException exception = assertThrows(
                AppUserNotFoundException.class,
                () -> userService.getByPublicId(publicId)
        );

        // Validamos que el mensaje mencione el ID buscado
        assertTrue(exception.getMessage().contains(publicId.toString()));
    }
}
