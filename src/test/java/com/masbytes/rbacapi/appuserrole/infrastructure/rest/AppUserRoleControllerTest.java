package com.masbytes.rbacapi.appuserrole.infrastructure.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masbytes.rbacapi.appuserrole.domain.service.AppUserRoleService;
import com.masbytes.rbacapi.appuserrole.domain.dto.AppUserRoleResponse;
import com.masbytes.rbacapi.appuserrole.domain.dto.AssignRoleRequest;
import com.masbytes.rbacapi.appuserrole.domain.dto.UpdateUserRoleRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.http.MediaType;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AppUserRoleController.class)
@AutoConfigureMockMvc(addFilters = false) // Desactiva los filtros de seguridad (Spring Security)
@SuppressWarnings("unused")
class AppUserRoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AppUserRoleService appUserRoleService;

    @Test
    @DisplayName("POST /api/v1/app-users/roles - Should return 201 Created")
    void assignRole_ShouldReturnCreated() throws Exception {
        // Arrange
        var request = new AssignRoleRequest(UUID.randomUUID(), UUID.randomUUID());
        var response = new AppUserRoleResponse(
                UUID.randomUUID(), request.userPublicId(), "test@mail.com",
                request.rolePublicId(), "ROLE_TEST", "2025-12-24T12:00:00.000Z"
        );

        when(appUserRoleService.assignRole(any(AssignRoleRequest.class))).thenReturn(response);

        // Act
        ResultActions result = mockMvc.perform(post("/api/v1/app-users/roles")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        // Assert
        result.andExpect(status().isCreated())
              .andExpect(jsonPath("$.userEmail").value("test@mail.com"))
              .andExpect(jsonPath("$.roleName").value("ROLE_TEST"));
    }

    @Test
    @DisplayName("PATCH /api/v1/app-users/{id}/roles - Should return 200 OK")
    void updateRole_ShouldReturnOk() throws Exception {
        // Arrange
        UUID userUuid = UUID.randomUUID();
        var request = new UpdateUserRoleRequest(UUID.randomUUID(), UUID.randomUUID());
        var response = new AppUserRoleResponse(
                UUID.randomUUID(), userUuid, "updated@mail.com",
                request.newRolePublicId(), "ROLE_NEW", "2025-12-24T13:00:00.000Z"
        );

        when(appUserRoleService.updateRole(eq(userUuid), any(UpdateUserRoleRequest.class))).thenReturn(response);

        // Act
        ResultActions result = mockMvc.perform(patch("/api/v1/app-users/{userPublicId}/roles", userUuid)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        // Assert
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.roleName").value("ROLE_NEW"));
    }
}