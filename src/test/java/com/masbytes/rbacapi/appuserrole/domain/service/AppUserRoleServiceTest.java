package com.masbytes.rbacapi.appuserrole.domain.service;

import com.masbytes.rbacapi.appuser.domain.entity.AppUser;
import com.masbytes.rbacapi.appuser.domain.exception.AppUserNotFoundException;
import com.masbytes.rbacapi.appuser.domain.repository.AppUserRepository;
import com.masbytes.rbacapi.appuserrole.domain.mapper.AppUserRoleMapper;
import com.masbytes.rbacapi.appuserrole.domain.dto.AssignRoleRequest;
import com.masbytes.rbacapi.appuserrole.domain.dto.UpdateUserRoleRequest;
import com.masbytes.rbacapi.appuserrole.domain.entity.AppUserRole;
//  import com.masbytes.rbacapi.appuserrole.domain.entity.AppUserRole;
import com.masbytes.rbacapi.appuserrole.domain.exception.AppUserRoleAlreadyExistsException;
import com.masbytes.rbacapi.appuserrole.domain.exception.AppUserRoleNotFoundException;
import com.masbytes.rbacapi.appuserrole.domain.repository.AppUserRoleRepository;
import com.masbytes.rbacapi.role.domain.entity.Role;
//  import com.masbytes.rbacapi.role.domain.exception.RoleNotFoundException;
import com.masbytes.rbacapi.role.domain.repository.RoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unused")
class AppUserRoleServiceTest {

    @Mock
    private AppUserRoleRepository appUserRoleRepository;
    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private AppUserRoleMapper mapper;

    @InjectMocks
    private AppUserRoleService service;

    @Nested
    @DisplayName("Assign Role Tests")
    class AssignRoleTests {

        @Test
        @DisplayName("Should assign role successfully")
        void assignRole_Success() {
            // Arrange
            UUID userUuid = UUID.randomUUID();
            UUID roleUuid = UUID.randomUUID();
            AssignRoleRequest request = new AssignRoleRequest(userUuid, roleUuid);

            // Mocks de entidades
            AppUser mockUser = mock(AppUser.class);
            Role mockRole = mock(Role.class);
            AppUserRole mockEntity = mock(AppUserRole.class);

            // 1. Configuramos los Mocks de las entidades para que devuelvan sus IDs
            when(mockUser.getPublicId()).thenReturn(userUuid);
            when(mockRole.getPublicId()).thenReturn(roleUuid);

            // 2. Stubbing de repositorios usando los IDs reales
            when(appUserRepository.findByPublicId(userUuid)).thenReturn(Optional.of(mockUser));
            when(roleRepository.findByPublicId(roleUuid)).thenReturn(Optional.of(mockRole));
            when(appUserRoleRepository.existsByAppUserPublicIdAndRolePublicId(userUuid, roleUuid)).thenReturn(false);

            // 3. Stubbing de mapper y save
            when(mapper.toEntity(mockUser, mockRole)).thenReturn(mockEntity);
            when(appUserRoleRepository.save(any(AppUserRole.class))).thenReturn(mockEntity);

            // Act
            service.assignRole(request);

            // Assert
            verify(appUserRoleRepository).save(any());
        }

        @Test
        @DisplayName("Should throw exception when role already exists for user")
        void assignRole_AlreadyExists() {
            // Arrange
            UUID userUuid = UUID.randomUUID();
            UUID roleUuid = UUID.randomUUID();
            AssignRoleRequest request = new AssignRoleRequest(userUuid, roleUuid);

            AppUser mockUser = mock(AppUser.class);
            Role mockRole = mock(Role.class);

            // Importante: dar comportamiento al mock para que el Service obtenga el UUID
            when(mockUser.getPublicId()).thenReturn(userUuid);
            when(mockRole.getPublicId()).thenReturn(roleUuid);

            when(appUserRepository.findByPublicId(userUuid)).thenReturn(Optional.of(mockUser));
            when(roleRepository.findByPublicId(roleUuid)).thenReturn(Optional.of(mockRole));
            when(appUserRoleRepository.existsByAppUserPublicIdAndRolePublicId(userUuid, roleUuid)).thenReturn(true);

            // Act & Assert
            AppUserRoleAlreadyExistsException ex = assertThrows(AppUserRoleAlreadyExistsException.class,
                    () -> service.assignRole(request));

            assertEquals("USER_ROLE_ALREADY_EXISTS", ex.getErrorCode());
        }

        @Test
        @DisplayName("Should throw exception when user not found")
        void assignRole_UserNotFound() {
            UUID userUuid = UUID.randomUUID();
            AssignRoleRequest request = new AssignRoleRequest(userUuid, UUID.randomUUID());

            when(appUserRepository.findByPublicId(userUuid)).thenReturn(Optional.empty());

            // Capturamos la excepción en una variable
            AppUserNotFoundException exception = assertThrows(AppUserNotFoundException.class,
                    () -> service.assignRole(request));

            // Verificamos el contenido (esto quita el warning y añade valor)
            assertTrue(exception.getMessage().contains(userUuid.toString()));
            assertEquals("USER_NOT_FOUND", exception.getErrorCode());

            verify(appUserRoleRepository, never()).save(any());
        }

    }

    @Nested
    @DisplayName("Update Role (Sustitution) Tests")
    class UpdateRoleTests {

        @Test
        @DisplayName("Should substitute role successfully")
        void updateRole_Success() {
            UUID userUuid = UUID.randomUUID();
            UUID oldRoleUuid = UUID.randomUUID();
            UUID newRoleUuid = UUID.randomUUID();
            UpdateUserRoleRequest request = new UpdateUserRoleRequest(oldRoleUuid, newRoleUuid);

            AppUser mockUser = mock(AppUser.class);
            Role mockNewRole = mock(Role.class);

            when(appUserRepository.findByPublicId(userUuid)).thenReturn(Optional.of(mockUser));
            when(appUserRoleRepository.existsByAppUserPublicIdAndRolePublicId(userUuid, oldRoleUuid)).thenReturn(true);
            when(roleRepository.findByPublicId(newRoleUuid)).thenReturn(Optional.of(mockNewRole));
            when(appUserRoleRepository.existsByAppUserPublicIdAndRolePublicId(userUuid, newRoleUuid)).thenReturn(false);

            service.updateRole(userUuid, request);

            verify(appUserRoleRepository).deleteByAppUserPublicIdAndRolePublicId(userUuid, oldRoleUuid);
            verify(appUserRoleRepository).save(any());
        }

        @Test
        @DisplayName("Should throw exception if user doesn't have the old role")
        void updateRole_OldRoleNotFound() {
            UUID userUuid = UUID.randomUUID();
            UpdateUserRoleRequest request = new UpdateUserRoleRequest(UUID.randomUUID(), UUID.randomUUID());

            when(appUserRepository.findByPublicId(userUuid)).thenReturn(Optional.of(mock(AppUser.class)));
            when(appUserRoleRepository.existsByAppUserPublicIdAndRolePublicId(any(), any())).thenReturn(false);

            AppUserRoleNotFoundException exception = assertThrows(AppUserRoleNotFoundException.class,
                    () -> service.updateRole(userUuid, request));

            assertEquals("USER_ROLE_NOT_FOUND", exception.getErrorCode());
        }
    }
}
