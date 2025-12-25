package com.masbytes.rbacapi.rolepermission.infrastructure.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masbytes.rbacapi.rolepermission.domain.service.RolePermissionService;
import com.masbytes.rbacapi.rolepermission.domain.dto.AssignPermissionRequest;
import com.masbytes.rbacapi.rolepermission.domain.dto.RolePermissionResponse;
import com.masbytes.rbacapi.rolepermission.domain.dto.RoleWithPermissionsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RolePermissionController.class, excludeAutoConfiguration = {
    org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class,
    org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration.class
})
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
@SuppressWarnings("unused")
class RolePermissionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RolePermissionService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/v1/role-permissions - Should return 201 Created")
    void assignPermission_Success() throws Exception {
        // Arrange
        UUID roleId = UUID.randomUUID();
        UUID permId = UUID.randomUUID();
        AssignPermissionRequest request = new AssignPermissionRequest(roleId, permId);

        RolePermissionResponse response = new RolePermissionResponse(
                UUID.randomUUID(), roleId, "ROLE_ADMIN", permId, "USER_READ", "2025-12-23"
        );

        when(service.assignPermissionToRole(any(AssignPermissionRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/role-permissions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roleName").value("ROLE_ADMIN"))
                .andExpect(jsonPath("$.permissionName").value("USER_READ"));
    }

    @Test
    @DisplayName("GET /api/v1/role-permissions/roles/{id} - Should return 200 OK")
    void getRoleWithPermissions_Success() throws Exception {
        // Arrange
        UUID roleId = UUID.randomUUID();
        RoleWithPermissionsResponse response = new RoleWithPermissionsResponse(
                roleId, "ROLE_ADMIN", "ACTIVE", List.of()
        );

        when(service.getRoleWithPermissions(roleId)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/v1/role-permissions/roles/{id}", roleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roleName").value("ROLE_ADMIN"));
    }

    @Test
    @DisplayName("DELETE /api/v1/role-permissions/... - Should return 204 No Content")
    void revokePermission_Success() throws Exception {
        // Arrange
        UUID roleId = UUID.randomUUID();
        UUID permId = UUID.randomUUID();

        // Act & Assert
        mockMvc.perform(delete("/api/v1/role-permissions/roles/{rId}/permissions/{pId}", roleId, permId))
                .andExpect(status().isNoContent());
    }
}
