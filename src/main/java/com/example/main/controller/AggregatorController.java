package com.example.main.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class AggregatorController {

    private final RestTemplate rt = new RestTemplate();

    // read from env vars, fallback to localhost for local dev
    private final String vehicleSvc = System.getenv().getOrDefault("VEHICLE_SVC", "http://localhost:8081/api/vehicle");
    private final String customerSvc = System.getenv().getOrDefault("CUSTOMER_SVC", "http://localhost:8082/api/customer");
    private final String recordSvc = System.getenv().getOrDefault("RECORD_SVC", "http://localhost:8083/api/servicerecord");

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String,Object> payload) {
        Map<String,Object> response = new HashMap<>();

        Object customer = payload.get("customer");
        if (customer != null) {
            ResponseEntity<Map> custRes = rt.postForEntity(customerSvc, customer, Map.class);
            response.put("customer", custRes.getBody());
            Object custId = ((Map)custRes.getBody()).get("id");
            Map vehicle = (Map) payload.get("vehicle");
            if (vehicle != null) {
                vehicle.put("ownerId", custId);
                ResponseEntity<Map> vehRes = rt.postForEntity(vehicleSvc, vehicle, Map.class);
                response.put("vehicle", vehRes.getBody());
            }
        }

        Object record = payload.get("record");
        if (record != null) {
            ResponseEntity<Map> recRes = rt.postForEntity(recordSvc, record, Map.class);
            response.put("record", recRes.getBody());
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard() {
        Map<String,Object> out = new HashMap<>();
        out.put("vehicles", rt.getForObject(vehicleSvc.replace("/api/vehicle","/api/vehicles"), Object.class));
        out.put("customers", rt.getForObject(customerSvc.replace("/api/customer","/api/customers"), Object.class));
        out.put("records", rt.getForObject(recordSvc.replace("/api/servicerecord","/api/servicerecords"), Object.class));
        return ResponseEntity.ok(out);
    }
}
