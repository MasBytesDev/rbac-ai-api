package com.masbytes.rbacapi.role.infrastructure.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masbytes.rbacapi.role.domain.service.RoleService;
import com.masbytes.rbacapi.role.domain.dto.*;
import com.masbytes.rbacapi.shared.domain.enums.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
// import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(RoleController.class)
@AutoConfigureMockMvc(addFilters = false) // Desactiva la seguridad para el test unitario
@DisplayName("RoleController Unit Tests")
@SuppressWarnings("unused")
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RoleService roleService;

    @Nested
    @DisplayName("POST /api/v1/roles")
    class CreateRoleTests {

        @Test
        @DisplayName("Should return 201 Created when request is valid")
        void shouldReturn201WhenValid() throws Exception {
            CreateRoleRequest request = new CreateRoleRequest("ROLE_ADMIN", "Administrator Role");
            RoleResponse response = new RoleResponse(UUID.randomUUID(), "ROLE_ADMIN", "Desc", Status.PENDING, null, null, null);

            given(roleService.createRole(any(CreateRoleRequest.class))).willReturn(response);

            mockMvc.perform(post("/api/v1/roles")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.roleName").value("ROLE_ADMIN"));
        }

        @Test
        @DisplayName("Should return 400 Bad Request when validation fails")
        void shouldReturn400WhenInvalid() throws Exception {
            // Nombre vacío para disparar @NotBlank
            CreateRoleRequest request = new CreateRoleRequest("", "Desc");

            mockMvc.perform(post("/api/v1/roles")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"));
        }
    }

    @Nested
    @DisplayName("PATCH /api/v1/roles/{publicId}/status")
    class ChangeStatusTests {

        @Test
        @DisplayName("Should return 200 OK when status change is successful")
        void shouldReturn200WhenStatusUpdated() throws Exception {
            UUID publicId = UUID.randomUUID();
            ChangeRoleStatusRequest request = new ChangeRoleStatusRequest(Status.ACTIVE);
            RoleResponse response = new RoleResponse(publicId, "ROLE_USER", "Desc", Status.ACTIVE, null, null, null);

            given(roleService.changeStatus(eq(publicId), any(ChangeRoleStatusRequest.class)))
                    .willReturn(response);

            mockMvc.perform(patch("/api/v1/roles/{publicId}/status", publicId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.roleStatus").value("ACTIVE"));
        }
    }
}
