package com.masbytes.rbacapi.role.domain.service;

import com.masbytes.rbacapi.role.domain.dto.ChangeRoleStatusRequest;
import com.masbytes.rbacapi.role.domain.mapper.RoleMapper;
import com.masbytes.rbacapi.role.domain.dto.CreateRoleRequest;
import com.masbytes.rbacapi.role.domain.dto.RoleResponse;
import com.masbytes.rbacapi.role.domain.dto.UpdateRoleDescriptionRequest;
import com.masbytes.rbacapi.role.domain.entity.Role;
import com.masbytes.rbacapi.role.domain.exception.RoleAlreadyExistsException;
import com.masbytes.rbacapi.role.domain.exception.RoleNotFoundException;
import com.masbytes.rbacapi.role.domain.repository.RoleRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RoleService Unit Tests")
@SuppressWarnings("unused")
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private RoleService roleService;

    @Nested
    @DisplayName("Tests for createRole")
    class CreateRoleTests {

        @Test
        @DisplayName("Should create a role successfully")
        void shouldCreateRoleSuccessfully() {
            // Given
            CreateRoleRequest request = new CreateRoleRequest("ROLE_ADMIN", "Administrator Role");
            Role roleEntity = Role.builder()
                    .roleName(request.roleName())
                    .roleDescription(request.roleDescription())
                    .roleStatus(Status.PENDING)
                    .build();
            RoleResponse expectedResponse = new RoleResponse(
                    UUID.randomUUID(), "ROLE_ADMIN", "Administrator Role", Status.PENDING, null, null, null
            );

            given(roleRepository.existsByRoleName(request.roleName())).willReturn(false);
            given(roleMapper.toEntity(request)).willReturn(roleEntity);
            given(roleRepository.save(any(Role.class))).willReturn(roleEntity);
            given(roleMapper.toResponse(roleEntity)).willReturn(expectedResponse);

            // When
            RoleResponse actualResponse = roleService.createRole(request);

            // Then
            assertThat(actualResponse).isNotNull();
            assertThat(actualResponse.roleName()).isEqualTo("ROLE_ADMIN");
            verify(roleRepository).save(any(Role.class));
        }

        @Test
        @DisplayName("Should throw RoleAlreadyExistsException when name is duplicated")
        void shouldThrowExceptionWhenRoleNameExists() {
            // Given
            CreateRoleRequest request = new CreateRoleRequest("ROLE_ADMIN", "Description");
            given(roleRepository.existsByRoleName(request.roleName())).willReturn(true);

            // When & Then
            assertThatThrownBy(() -> roleService.createRole(request))
                    .isInstanceOf(RoleAlreadyExistsException.class);

            verify(roleRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Tests for getRoleByPublicId")
    class GetRoleTests {

        @Test
        @DisplayName("Should return a role when it exists")
        void shouldReturnRoleWhenExists() {
            // Given
            UUID publicId = UUID.randomUUID();
            Role role = Role.builder().build();
            RoleResponse response = new RoleResponse(publicId, "ROLE_USER", "Desc", Status.ACTIVE, null, null, null);

            given(roleRepository.findByPublicId(publicId)).willReturn(Optional.of(role));
            given(roleMapper.toResponse(role)).willReturn(response);

            // When
            RoleResponse result = roleService.getRoleByPublicId(publicId);

            // Then
            assertThat(result.publicId()).isEqualTo(publicId);
        }

        @Test
        @DisplayName("Should throw RoleNotFoundException when role does not exist")
        void shouldThrowNotFoundException() {
            // Given
            UUID publicId = UUID.randomUUID();
            given(roleRepository.findByPublicId(publicId)).willReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> roleService.getRoleByPublicId(publicId))
                    .isInstanceOf(RoleNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Tests for updateDescription")
    class UpdateDescriptionTests {

        @Test
        @DisplayName("Should update description successfully")
        void shouldUpdateDescriptionSuccessfully() {
            // Given
            UUID publicId = UUID.randomUUID();
            UpdateRoleDescriptionRequest request = new UpdateRoleDescriptionRequest("New valid description");
            Role role = Role.builder()
                    .roleDescription("Old description")
                    .build();

            given(roleRepository.findByPublicId(publicId)).willReturn(Optional.of(role));
            given(roleRepository.save(any(Role.class))).willReturn(role);
            given(roleMapper.toResponse(any(Role.class))).willReturn(mock(RoleResponse.class));

            // When
            roleService.updateDescription(publicId, request);

            // Then
            assertThat(role.getRoleDescription()).isEqualTo("New valid description");
            verify(roleRepository).save(role);
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when description is invalid")
        void shouldThrowExceptionWhenDescriptionIsBlank() {
            // Given
            UUID publicId = UUID.randomUUID();
            UpdateRoleDescriptionRequest request = new UpdateRoleDescriptionRequest(""); // Inválido
            Role role = Role.builder().roleDescription("Old").build();

            given(roleRepository.findByPublicId(publicId)).willReturn(Optional.of(role));

            // When & Then
            assertThatThrownBy(() -> roleService.updateDescription(publicId, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Tests for changeStatus")
    class ChangeStatusTests {

        @Test
        @DisplayName("Should change status successfully")
        void shouldChangeStatusSuccessfully() {
            // Given
            UUID publicId = UUID.randomUUID();
            ChangeRoleStatusRequest request = new ChangeRoleStatusRequest(Status.ACTIVE);
            Role role = Role.builder()
                    .roleStatus(Status.PENDING)
                    .build();

            RoleResponse expectedResponse = new RoleResponse(
                    publicId, "ROLE_ADMIN", "Desc", Status.ACTIVE, null, null, null
            );

            given(roleRepository.findByPublicId(publicId)).willReturn(Optional.of(role));
            given(roleRepository.save(any(Role.class))).willReturn(role);
            given(roleMapper.toResponse(role)).willReturn(expectedResponse);

            // When - Asignamos el retorno a una variable
            RoleResponse actualResponse = roleService.changeStatus(publicId, request);

            // Then - Ahora podemos verificar el contenido del retorno
            assertThat(actualResponse).isNotNull();
            assertThat(actualResponse.roleStatus()).isEqualTo(Status.ACTIVE);
            assertThat(role.getRoleStatus()).isEqualTo(Status.ACTIVE); // Verificamos la entidad mutada
            verify(roleRepository).save(role);
        }

        @Test
        @DisplayName("Should throw IllegalStateException when transition is invalid")
        void shouldThrowExceptionWhenTransitionIsInvalid() {
            // Given
            UUID publicId = UUID.randomUUID();
            ChangeRoleStatusRequest request = new ChangeRoleStatusRequest(Status.PENDING);
            Role role = Role.builder()
                    .roleStatus(Status.INACTIVE)
                    .build();

            given(roleRepository.findByPublicId(publicId)).willReturn(Optional.of(role));

            // When & Then - Capturamos la excepción lanzada
            Throwable thrown = catchThrowable(() -> roleService.changeStatus(publicId, request));

            // Verificamos el tipo y podemos inspeccionar el mensaje
            assertThat(thrown)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Invalid transition");

            verify(roleRepository, never()).save(any());
        }
    }
}
