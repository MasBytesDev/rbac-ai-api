package com.masbytes.rbacapi.permission.infrastructure.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masbytes.rbacapi.permission.domain.dto.*;
import com.masbytes.rbacapi.permission.domain.service.PermissionService;
import com.masbytes.rbacapi.shared.domain.enums.Status;
import com.masbytes.rbacapi.shared.infrastructure.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Permission Controller Unit Tests")
@SuppressWarnings("unused")
class PermissionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PermissionService permissionService;

    @InjectMocks
    private PermissionController permissionController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        // Configuramos MockMvc manualmente para inyectar nuestro Handler de excepciones
        mockMvc = MockMvcBuilders.standaloneSetup(permissionController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Nested
    @DisplayName("POST /api/v1/permissions")
    class CreateTests {

        @Test
        @DisplayName("Should return 201 Created when request is valid")
        void create_ShouldReturn201_WhenValid() throws Exception {
            var request = new CreatePermissionRequest("USER_CREATE", "Create users");
            var response = new PermissionResponse(UUID.randomUUID(), "USER_CREATE", "Create users", Status.PENDING, null, null);

            when(permissionService.create(any(CreatePermissionRequest.class))).thenReturn(response);

            mockMvc.perform(post("/api/v1/permissions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.permissionName").value("USER_CREATE"))
                    .andExpect(jsonPath("$.permissionStatus").value("PENDING"));
        }
    }

    @Nested
    @DisplayName("GET /api/v1/permissions/{publicId}")
    class GetTests {

        @Test
        @DisplayName("Should return 200 OK when permission exists")
        void get_ShouldReturn200_WhenExists() throws Exception {
            UUID id = UUID.randomUUID();
            var response = new PermissionResponse(id, "USER_READ", "Read users", Status.ACTIVE, null, null);

            when(permissionService.getByPublicId(id)).thenReturn(response);

            mockMvc.perform(get("/api/v1/permissions/{publicId}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.publicId").value(id.toString()))
                    .andExpect(jsonPath("$.permissionName").value("USER_READ"));
        }
    }
}