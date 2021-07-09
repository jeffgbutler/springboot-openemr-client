package com.example.radiologyclient.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.IRestfulClientFactory;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.BearerTokenAuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.context.annotation.RequestScope;

@Configuration
public class FhirConfiguration {
    @Bean
    public FhirContext fhirContext() {
        FhirContext fhirContext = FhirContext.forR4();
        // OpenEMR's capability statement is broken, so don't ever validate
        // anything from them :)
        IRestfulClientFactory factory = fhirContext.getRestfulClientFactory();
        factory.setServerValidationMode(ServerValidationModeEnum.NEVER);
        return fhirContext;
    }

    @Bean
    @RequestScope
    public IGenericClient fhirClient(OAuth2AuthorizedClientService clientService, FhirContext fhirContext) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
                oauthToken.getAuthorizedClientRegistrationId(),
                oauthToken.getName());
        String accessToken = client.getAccessToken().getTokenValue();
        BearerTokenAuthInterceptor authInterceptor = new BearerTokenAuthInterceptor(accessToken);
        IGenericClient genericClient =
                fhirContext.newRestfulGenericClient("https://openemr.tanzuathome.net/apis/default/fhir");
        genericClient.registerInterceptor(authInterceptor);
        return genericClient;
    }
}
