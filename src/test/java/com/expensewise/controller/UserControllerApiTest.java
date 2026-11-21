package com.expensewise.controller;

import com.expensewise.dto.request.UserCreateDTO;
import com.expensewise.dto.response.UserResponseDTO;
import com.expensewise.service.UserService;
import com.expensewise.support.ControllerGwtTestSupport;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
    controllers = UserController.class,
    excludeAutoConfiguration = {
        SecurityAutoConfiguration.class,
        OAuth2ResourceServerAutoConfiguration.class
    }
)
class UserControllerApiTest extends ControllerGwtTestSupport {

    @MockBean
    private UserService userService;

    @Test
    void givenExistingUserId_whenGetById_thenReturn200AndUser() throws Exception {
        // Given
        UserResponseDTO response = new UserResponseDTO(
            1L,
            "alice",
            "alice@example.com",
            LocalDateTime.of(2026, 1, 1, 10, 0)
        );
        given(userService.getById(1L)).willReturn(response);

        // When / Then
        whenGet("/api/users/1")
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.username").value("alice"))
            .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void givenValidPayload_whenCreateUser_thenReturn201AndCreatedUser() throws Exception {
        // Given
        UserCreateDTO request = new UserCreateDTO("bob", "bob@example.com");
        UserResponseDTO response = new UserResponseDTO(
            2L,
            "bob",
            "bob@example.com",
            LocalDateTime.of(2026, 1, 2, 11, 0)
        );
        given(userService.create(request)).willReturn(response);

        // When / Then
        whenPostJson("/api/users", request)
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(2))
            .andExpect(jsonPath("$.username").value("bob"))
            .andExpect(jsonPath("$.email").value("bob@example.com"));
    }

    @Test
    void givenInvalidPayload_whenCreateUser_thenReturn400AndDoNotCallService() throws Exception {
        // Given
        String invalidPayload = """
            {
              "username": "",
              "email": "not-an-email"
            }
            """;

        // When / Then
        whenPostRawJson("/api/users", invalidPayload)
            .andExpect(status().isBadRequest());

        thenNoInteractions(userService);
    }
}
