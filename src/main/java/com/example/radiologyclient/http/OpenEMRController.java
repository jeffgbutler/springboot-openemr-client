package com.example.radiologyclient.http;

import com.example.radiologyclient.model.RadiologyFacility;
import com.example.radiologyclient.model.RadiologyPatient;
import com.example.radiologyclient.service.OpenEMRService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class OpenEMRController {
    private final OpenEMRService openEMRService;

    public OpenEMRController(OpenEMRService openEMRService) {
        this.openEMRService = openEMRService;
    }

    @GetMapping("/patient")
    public ResponseEntity<List<RadiologyPatient>> findAllPatients() {
        List<RadiologyPatient> patients = openEMRService.findAllPatients();

        return ResponseEntity.ok(patients);
    }

    @PostMapping("/practitioner")
    public ResponseEntity<String> insertPractitioners(@RequestParam("file") MultipartFile file) throws IOException {
        openEMRService.insertPractitioners(file);

        return ResponseEntity.ok("OK");
    }

    @GetMapping("/facility")
    public ResponseEntity<List<RadiologyFacility>> findAllFacilities() {
        List<RadiologyFacility> facilities = openEMRService.findAllFacilities();

        return ResponseEntity.ok(facilities);
    }

    @PostMapping("/facility")
    public ResponseEntity<String> insertFacilities(@RequestParam("file") MultipartFile file) throws IOException {
        openEMRService.insertFacilities(file);

        return ResponseEntity.ok("OK");
    }
}
