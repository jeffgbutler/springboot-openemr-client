package com.example.radiologyclient.http;

import com.example.radiologyclient.model.RadiologyPatient;
import com.example.radiologyclient.service.OpenEMRService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OpenEMRController {
    private final OAuth2AuthorizedClientService clientService;
    private final OpenEMRService openEMRService;

    public OpenEMRController(OAuth2AuthorizedClientService clientService, OpenEMRService openEMRService) {
        this.clientService = clientService;
        this.openEMRService = openEMRService;
    }

    @GetMapping("/patient")
    public ResponseEntity<List<RadiologyPatient>> patient() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
                oauthToken.getAuthorizedClientRegistrationId(),
                oauthToken.getName());
        String accessToken = client.getAccessToken().getTokenValue();

        List<RadiologyPatient> patients = openEMRService.findAllPatients(accessToken);

        return ResponseEntity.ok(patients);
    }
}
