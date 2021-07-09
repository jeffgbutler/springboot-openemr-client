package com.example.radiologyclient.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.BearerTokenAuthInterceptor;
import com.example.radiologyclient.model.RadiologyPatient;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OpenEMRService {

    private final FhirContext fhirContext;

    public OpenEMRService(FhirContext fhirContext) {
        this.fhirContext = fhirContext;
    }

    public List<RadiologyPatient> findAllPatients(String accessToken) {
        BearerTokenAuthInterceptor authInterceptor = new BearerTokenAuthInterceptor(accessToken);
        IGenericClient genericClient = fhirContext.newRestfulGenericClient("https://openemr.tanzuathome.net/apis/default/fhir");
        genericClient.registerInterceptor(authInterceptor);

        Bundle results = genericClient.search().forResource(Patient.class)
                .returnBundle(Bundle.class)
                .execute();

        return results.getEntry().stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .map(Patient.class::cast)
                .map(this::toPatient)
                .collect(Collectors.toList());
    }

    private RadiologyPatient toPatient(Patient patient) {
        HumanName name = patient.getNameFirstRep();
        Enumerations.AdministrativeGender gender =  patient.getGender();
        String id = patient.getIdElement().getIdPart();

        return new RadiologyPatient(id, name.getGivenAsSingleString(), name.getFamily(), gender.name());
    }
}
