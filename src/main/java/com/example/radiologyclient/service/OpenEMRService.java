package com.example.radiologyclient.service;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.example.radiologyclient.model.RadiologyFacility;
import com.example.radiologyclient.model.RadiologyPatient;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OpenEMRService {

    private final IGenericClient client;
    private final FhirContext fhirContext;

    public OpenEMRService(IGenericClient client, FhirContext fhirContext) {
        this.client = client;
        this.fhirContext = fhirContext;
    }

    public List<RadiologyPatient> findAllPatients() {
        Bundle results = client.search().forResource(Patient.class)
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

    public boolean insertPractitioners(MultipartFile file) throws IOException {
        Bundle bundle = fhirContext.newJsonParser().parseResource(Bundle.class, file.getInputStream());

        // this creates a batch insert of practitioners, but OpenEMR doesn't support this. So we
        // need to insert each practitioner individually

        bundle.getEntry().stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .map(Practitioner.class::cast)
                .forEach(this::insertPractitioner);

        return true;
    }

    private void insertPractitioner(Practitioner practitioner) {
        client.create().resource(practitioner).prettyPrint().encodedJson().execute();
    }

    public List<RadiologyFacility> findAllFacilities() {
        Bundle results = client.search().forResource(Organization.class)
                .returnBundle(Bundle.class)
                .execute();

        return results.getEntry().stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .map(Organization.class::cast)
                .map(this::toFacility)
                .collect(Collectors.toList());
    }

    private RadiologyFacility toFacility(Organization organization) {
        System.out.print(fhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(organization));
        String id = organization.getIdElement().getIdPart();

        return new RadiologyFacility(id);
    }

    public boolean insertFacilities(MultipartFile file) throws IOException {
        Bundle bundle = fhirContext.newJsonParser().parseResource(Bundle.class, file.getInputStream());

        // this creates a batch insert of organizations, but OpenEMR doesn't support this. So we
        // need to insert each organization individually

        bundle.getEntry().stream()
                .map(Bundle.BundleEntryComponent::getResource)
                .map(Organization.class::cast)
                .forEach(this::insertFacility);

        return true;
    }

    private void insertFacility(Organization organization) {
        // add an empty NPI identifier as it is required by OpenEMR
        Identifier identifier = new Identifier();
        identifier.setSystem("http://hl7.org/fhir/sid/us-npi");
        identifier.setValue("9278590143");
        organization.addIdentifier(identifier);

        client.create().resource(organization).prettyPrint().encodedJson().execute();
    }
}
