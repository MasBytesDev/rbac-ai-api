package com.masbytes.rbacapi.permission.domain.service;

import com.masbytes.rbacapi.permission.domain.dto.*;
import com.masbytes.rbacapi.permission.domain.exception.PermissionAlreadyExistsException;
import com.masbytes.rbacapi.permission.domain.exception.PermissionNotFoundException;
import com.masbytes.rbacapi.permission.domain.mapper.PermissionMapper;
import com.masbytes.rbacapi.permission.domain.entity.Permission;
import com.masbytes.rbacapi.permission.domain.repository.PermissionRepository;
import com.masbytes.rbacapi.shared.domain.enums.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Permission Service Unit Tests")
@SuppressWarnings("unused")
class PermissionServiceTest {

    @Mock
    private PermissionRepository repository;

    @Mock
    private PermissionMapper mapper;

    @InjectMocks
    private PermissionService service;

    @Nested
    @DisplayName("Creation Tests")
    class CreateTests {

        @Test
        @DisplayName("Should create permission successfully")
        void create_ShouldReturnResponse_WhenRequestIsValid() {
            // Arrange (Preparar)
            var request = new CreatePermissionRequest("USER_READ", "Read access");
            var entity = Permission.builder().permissionName("USER_READ").build();
            var response = new PermissionResponse(UUID.randomUUID(), "USER_READ", "Read access", null, null, null);

            when(repository.existsByPermissionName(request.permissionName())).thenReturn(false);
            when(mapper.toEntity(request)).thenReturn(entity);
            when(repository.save(entity)).thenReturn(entity);
            when(mapper.toResponse(entity)).thenReturn(response);

            // Act (Actuar)
            PermissionResponse result = service.create(request);

            // Assert (Verificar)
            assertNotNull(result);
            assertEquals("USER_READ", result.permissionName());
            verify(repository).save(any(Permission.class));
        }

        @Test
        @DisplayName("Should throw exception when permission name already exists")
        void create_ShouldThrowException_WhenNameExists() {
            // Arrange
            var request = new CreatePermissionRequest("USER_READ", "Read access");
            when(repository.existsByPermissionName("USER_READ")).thenReturn(true);

            // Act & Assert
            // Aquí asignamos el valor de retorno a una variable 'exception'
            PermissionAlreadyExistsException exception = assertThrows(
                    PermissionAlreadyExistsException.class,
                    () -> service.create(request)
            );

            // Ahora usamos esa variable para una validación extra
            // Esto demuestra que no solo lanzaste la excepción correcta, sino con el mensaje correcto
            assertTrue(exception.getMessage().contains("USER_READ"));

            verify(repository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Update Tests")
    class UpdateTests {

        @Test
        @DisplayName("Should throw NotFoundException when permission does not exist")
        void update_ShouldThrowNotFound_WhenIdInvalid() {
            // Arrange
            UUID id = UUID.randomUUID();
            var request = new UpdatePermissionDescriptionRequest("New Desc");
            when(repository.findByPublicId(id)).thenReturn(Optional.empty());

            // Act & Assert
            // 1. Asignamos el retorno a una variable para quitar la advertencia
            PermissionNotFoundException exception = assertThrows(
                    PermissionNotFoundException.class,
                    () -> service.updateDescription(id, request)
            );

            // 2. Opcional: Verificamos que el mensaje de error sea el esperado
            // Esto es muy útil porque confirma que el ID que pasamos es el que reporta el error
            assertTrue(exception.getMessage().contains(id.toString()));
        }
    }

    @Nested
    @DisplayName("Status Change Tests")
    class StatusChangeTests {

        @Test
        @DisplayName("Should change status successfully")
        void changeStatus_ShouldWork_WhenTransitionIsLegal() {
            // Arrange
            UUID id = UUID.randomUUID();
            var request = new ChangePermissionStatusRequest(Status.ACTIVE);
            var permission = spy(Permission.builder()
                    .permissionName("TEST_PERM")
                    .permissionStatus(Status.PENDING) // Estado inicial
                    .build());

            var response = new PermissionResponse(id, "TEST_PERM", "Desc", Status.ACTIVE, null, null);

            when(repository.findByPublicId(id)).thenReturn(Optional.of(permission));
            // No mockeamos el void del mapper, dejamos que se ejecute
            when(repository.save(permission)).thenReturn(permission);
            when(mapper.toResponse(permission)).thenReturn(response);

            // Act
            PermissionResponse result = service.changeStatus(id, request);

            // Assert
            assertEquals(Status.ACTIVE, result.permissionStatus());
            verify(mapper).updateEntityFromStatusRequest(request, permission);
            verify(repository).save(permission);
        }

        @Test
        @DisplayName("Should propagate exception when entity transition fails")
        void changeStatus_ShouldThrowException_WhenTransitionIsIllegal() {
            // Arrange
            UUID id = UUID.randomUUID();
            var request = new ChangePermissionStatusRequest(Status.ARCHIVED);
            var permission = Permission.builder()
                    .permissionName("TEST_PERM")
                    .permissionStatus(Status.PENDING)
                    .build();

            when(repository.findByPublicId(id)).thenReturn(Optional.of(permission));

            // Simulamos que el mapper lanza la excepción
            doThrow(new IllegalStateException("Invalid transition"))
                    .when(mapper).updateEntityFromStatusRequest(request, permission);

            // Act & Assert
            // 1. Recogemos la excepción para satisfacer al IDE y validar el mensaje
            IllegalStateException exception = assertThrows(
                    IllegalStateException.class,
                    () -> service.changeStatus(id, request)
            );

            // 2. Verificamos que el mensaje sea exactamente el que simulamos
            assertEquals("Invalid transition", exception.getMessage());

            // 3. Verificamos que NUNCA se llamó al save tras el error
            verify(repository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Query Tests")
    class QueryTests {

        @Test
        @DisplayName("Should return response when permission exists")
        void getByPublicId_ShouldReturnResponse_WhenExists() {
            UUID id = UUID.randomUUID();
            var entity = Permission.builder().permissionName("READ").build();
            var response = new PermissionResponse(id, "READ", "Desc", Status.ACTIVE, null, null);

            when(repository.findByPublicId(id)).thenReturn(Optional.of(entity));
            when(mapper.toResponse(entity)).thenReturn(response);

            var result = service.getByPublicId(id);

            assertNotNull(result);
            assertEquals("READ", result.permissionName());
        }

        @Test
        @DisplayName("Should throw NotFoundException when permission is missing")
        void getByPublicId_ShouldThrowNotFound_WhenNotExists() {
            // Arrange
            UUID id = UUID.randomUUID();
            when(repository.findByPublicId(id)).thenReturn(Optional.empty());

            // Act & Assert
            // Capturamos el retorno para que NetBeans esté feliz
            PermissionNotFoundException exception = assertThrows(
                    PermissionNotFoundException.class,
                    () -> service.getByPublicId(id)
            );

            // Verificamos que el mensaje contenga el ID que no se encontró
            assertTrue(exception.getMessage().contains(id.toString()));
        }
    }
}
