package com.example.radiologyclient.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IRestfulClientFactory;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FhirConfiguration {
    @Bean
    public FhirContext fhirContext() {
        FhirContext fhirContext = FhirContext.forR4();
        IRestfulClientFactory factory = fhirContext.getRestfulClientFactory();
        factory.setServerValidationMode(ServerValidationModeEnum.NEVER);
        return fhirContext;
    }
}
