package com.rainett.config;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.rainett.security.SecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Import({SecurityConfig.class,
        SecurityConfigTest.ActuatorController.class,
        SecurityConfigTest.ApiController.class})
@WebMvcTest(controllers = {
        SecurityConfigTest.ActuatorController.class,
        SecurityConfigTest.ApiController.class
})
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Allows actuator endpoints")
    @WithAnonymousUser
    void allowsActuatorEndpoints() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Allows create endpoints")
    @WithAnonymousUser
    void allowsCreateEndpoints() throws Exception {
        mockMvc.perform(post("/api/trainees"))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/trainers"))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Denies non-create endpoints to anonymous users")
    @WithAnonymousUser
    void deniesNonCreateEndpoints() throws Exception {
        mockMvc.perform(get("/api/trainees/username"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Allows non-create endpoints for authorized users")
    @WithMockUser(username = "user")
    void allowsNonCreateEndpoints_whenUserAuthorized() throws Exception {
        mockMvc.perform(get("/api/trainees/username"))
                .andExpect(status().isOk());
    }

    @RestController
    @RequestMapping("/actuator")
    static class ActuatorController {
        @GetMapping("/health")
        public String health() {
            return "UP";
        }
    }

    @RestController
    @RequestMapping("/api")
    static class ApiController {

        @PostMapping("/trainees")
        @ResponseStatus(HttpStatus.CREATED)
        public void createTrainee() {
            // Simulated creation logic
        }

        @PostMapping("/trainers")
        @ResponseStatus(HttpStatus.CREATED)
        public void createTrainer() {
            // Simulated creation logic
        }

        @GetMapping("/trainees/{username}")
        public void getTrainee(@PathVariable String username) {
            // Simulated get logic
        }
    }
}
