package com.masbytes.rbacapi.rolepermission.domain.service;

import com.masbytes.rbacapi.permission.domain.entity.Permission;
import com.masbytes.rbacapi.permission.domain.repository.PermissionRepository;
import com.masbytes.rbacapi.role.domain.entity.Role;
import com.masbytes.rbacapi.role.domain.repository.RoleRepository;
import com.masbytes.rbacapi.rolepermission.domain.mapper.RolePermissionMapper;
import com.masbytes.rbacapi.rolepermission.domain.dto.AssignPermissionRequest;
import com.masbytes.rbacapi.rolepermission.domain.dto.RolePermissionResponse;
import com.masbytes.rbacapi.rolepermission.domain.entity.RolePermission;
import com.masbytes.rbacapi.rolepermission.domain.exception.PermissionAlreadyAssignedException;
import com.masbytes.rbacapi.rolepermission.domain.repository.RolePermissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("unused")
class RolePermissionServiceTest {

    @Mock
    private RolePermissionRepository rolePermissionRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PermissionRepository permissionRepository;
    @Mock
    private RolePermissionMapper mapper;

    @InjectMocks
    private RolePermissionService service;

    private UUID roleId;
    private UUID permissionId;
    private Role mockRole;
    private Permission mockPermission;

    @BeforeEach
    void setUp() {
        roleId = UUID.randomUUID();
        permissionId = UUID.randomUUID();

        // Configuramos objetos básicos para los tests
        mockRole = mock(Role.class);
        mockPermission = mock(Permission.class);
    }

    @Test
    @DisplayName("Should assign permission to role successfully")
    void assignPermissionToRole_Success() {
        // Arrange
        AssignPermissionRequest request = new AssignPermissionRequest(roleId, permissionId);

        when(roleRepository.findByPublicId(roleId)).thenReturn(Optional.of(mockRole));
        when(permissionRepository.findByPublicId(permissionId)).thenReturn(Optional.of(mockPermission));
        when(rolePermissionRepository.existsByRolePublicIdAndPermissionPublicId(roleId, permissionId)).thenReturn(false);
        when(rolePermissionRepository.save(any(RolePermission.class))).thenReturn(mock(RolePermission.class));
        when(mapper.toRolePermissionResponse(any())).thenReturn(mock(RolePermissionResponse.class));

        // Act
        RolePermissionResponse response = service.assignPermissionToRole(request);

        // Assert
        assertNotNull(response);
        verify(rolePermissionRepository, times(1)).save(any(RolePermission.class));
    }

    @Test
    @DisplayName("Should throw exception when permission is already assigned")
    void assignPermissionToRole_AlreadyExists() {
        // Arrange
        AssignPermissionRequest request = new AssignPermissionRequest(roleId, permissionId);

        // Configuramos los mocks para que devuelvan nombres específicos
        when(mockRole.getRoleName()).thenReturn("ROLE_MODERATOR");
        when(mockPermission.getPermissionName()).thenReturn("USER_WRITE");

        when(roleRepository.findByPublicId(roleId)).thenReturn(Optional.of(mockRole));
        when(permissionRepository.findByPublicId(permissionId)).thenReturn(Optional.of(mockPermission));
        when(rolePermissionRepository.existsByRolePublicIdAndPermissionPublicId(roleId, permissionId)).thenReturn(true);

        // Act & Assert
        // Capturamos el objeto retornado para que Netbeans esté feliz
        PermissionAlreadyAssignedException exception = assertThrows(PermissionAlreadyAssignedException.class, () -> {
            service.assignPermissionToRole(request);
        });

        // Ahora usamos el objeto para validar el contenido real
        assertTrue(exception.getMessage().contains("ROLE_MODERATOR"));
        assertTrue(exception.getMessage().contains("USER_WRITE"));
        assertEquals("PERMISSION_ALREADY_ASSIGNED", exception.getErrorCode());

        verify(rolePermissionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should revoke permission from role successfully")
    void revokePermissionFromRole_Success() {
        // Arrange
        RolePermission mockAssociation = mock(RolePermission.class);
        when(rolePermissionRepository.findByRolePublicIdAndPermissionPublicId(roleId, permissionId))
                .thenReturn(Optional.of(mockAssociation));

        // Act
        assertDoesNotThrow(() -> service.revokePermissionFromRole(roleId, permissionId));

        // Assert
        verify(rolePermissionRepository, times(1)).delete(mockAssociation);
    }
}
