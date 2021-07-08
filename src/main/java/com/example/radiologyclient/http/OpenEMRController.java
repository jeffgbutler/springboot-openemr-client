package com.example.radiologyclient.http;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

@RestController
public class OpenEMRController {
    private final WebClient webClient;

    public OpenEMRController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/patient")
    public String patient() {
        String response = this.webClient
                .get()
                .uri("https://openemr.tanzuathome.net/apis/default/fhir/Patient")
                .attributes(clientRegistrationId("openemr"))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return response;
    }
}
