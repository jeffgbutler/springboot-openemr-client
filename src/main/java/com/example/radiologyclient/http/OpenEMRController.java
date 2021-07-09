package com.example.radiologyclient.http;

import com.example.radiologyclient.model.RadiologyPatient;
import com.example.radiologyclient.service.OpenEMRService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OpenEMRController {
    private final OpenEMRService openEMRService;

    public OpenEMRController(OpenEMRService openEMRService) {
        this.openEMRService = openEMRService;
    }

    @GetMapping("/patient")
    public ResponseEntity<List<RadiologyPatient>> patient() {
        List<RadiologyPatient> patients = openEMRService.findAllPatients();

        return ResponseEntity.ok(patients);
    }
}
