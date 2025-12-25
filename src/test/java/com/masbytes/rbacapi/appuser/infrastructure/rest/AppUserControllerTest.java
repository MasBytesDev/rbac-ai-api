package com.masbytes.rbacapi.appuser.infrastructure.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masbytes.rbacapi.appuser.domain.service.AppUserService;
import com.masbytes.rbacapi.appuser.domain.dto.AppUserResponse;
import com.masbytes.rbacapi.appuser.domain.dto.CreateAppUserRequest;
import com.masbytes.rbacapi.appuser.domain.exception.EmailAlreadyExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppUserController.class)
@AutoConfigureMockMvc(addFilters = false) // Desactiva filtros de seguridad para la prueba unitaria
@SuppressWarnings("unused")
class AppUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AppUserService userService;

    @Test
    @DisplayName("POST /api/v1/app-users - Should return 201 Created")
    void createUser_ShouldReturnCreated() throws Exception {
        // Arrange
        UUID publicId = UUID.randomUUID();
        CreateAppUserRequest request = new CreateAppUserRequest("Juan Perez", "juan@example.com", "password123");
        AppUserResponse response = new AppUserResponse(publicId, "Juan Perez", "juan@example.com", "PENDING", "2025-12-24T09:00:00.000Z", null);

        when(userService.createUser(any(CreateAppUserRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/app-users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.publicId").value(publicId.toString()))
                .andExpect(jsonPath("$.email").value("juan@example.com"));
    }

    @Test
    @DisplayName("POST /api/v1/app-users - Should return 409 Conflict when email exists")
    void createUser_ShouldReturnConflict() throws Exception {
        // Arrange
        CreateAppUserRequest request = new CreateAppUserRequest("Juan Perez", "juan@example.com", "password123");

        when(userService.createUser(any(CreateAppUserRequest.class)))
                .thenThrow(new EmailAlreadyExistsException("juan@example.com"));

        // Act & Assert
        mockMvc.perform(post("/api/v1/app-users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("EMAIL_ALREADY_EXISTS"));
    }

    @Test
    @DisplayName("GET /api/v1/app-users/{id} - Should return 200 OK")
    void getByPublicId_ShouldReturnOk() throws Exception {
        // Arrange
        UUID publicId = UUID.randomUUID();
        AppUserResponse response = new AppUserResponse(
                publicId,
                "Juan Perez",
                "juan@example.com",
                "ACTIVE",
                "2025-12-24T09:00:00.000Z",
                null
        );

        when(userService.getByPublicId(publicId)).thenReturn(response);

        // Act & Assert
        // Asignamos a 'result' para quitar la advertencia y poder usar .andDo(print())
        var result = mockMvc.perform(get("/api/v1/app-users/" + publicId))
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print()) // Útil para ver el JSON en consola
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.publicId").value(publicId.toString()))
                .andExpect(jsonPath("$.fullname").value("Juan Perez"));

        assertNotNull(result); // Usamos la variable para que el IDE esté satisfecho
    }
}
