package com.example.main.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AggregatorController.class)
class AggregatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AggregatorController controller;

    @Autowired
    private ObjectMapper objectMapper;

    private RestTemplate restTemplateMock;

    @BeforeEach
    void setup() {
        // Create mock RestTemplate
        restTemplateMock = Mockito.mock(RestTemplate.class);

        // Inject mock into existing controller (NO prod code change)
        ReflectionTestUtils.setField(controller, "rt", restTemplateMock);
    }

    // ---------------- REGISTER API ----------------
    @Test
    void register_success_noRealHttpCall() throws Exception {

        Mockito.when(restTemplateMock.postForEntity(
                        Mockito.anyString(), Mockito.any(), Mockito.eq(Map.class)))
                .thenReturn(ResponseEntity.ok(Map.of("id", "X1")));

        Map<String, Object> payload = new HashMap<>();
        payload.put("customer", Map.of("name", "Digvijay"));
        payload.put("vehicle", Map.of("model", "Nexon"));
        payload.put("record", Map.of("type", "Service"));

        mockMvc.perform(post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customer.id").value("X1"))
                .andExpect(jsonPath("$.vehicle.id").value("X1"))
                .andExpect(jsonPath("$.record.id").value("X1"));
    }

    // ---------------- DASHBOARD API ----------------
    @Test
    void dashboard_success_noRealHttpCall() throws Exception {

        Mockito.when(restTemplateMock.getForObject(
                        Mockito.anyString(), Mockito.eq(Object.class)))
                .thenReturn(List.of(Map.of("id", "1")));

        mockMvc.perform(get("/api/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vehicles", notNullValue()))
                .andExpect(jsonPath("$.customers", notNullValue()))
                .andExpect(jsonPath("$.records", notNullValue()));
    }
}
