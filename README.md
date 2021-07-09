# OpenMR Debug

```shell
kubectl vsphere login --server 192.168.139.2 --tanzu-kubernetes-cluster-namespace openemr-ns \
  --tanzu-kubernetes-cluster-name openemr-unified-cluster -u administrator@vsphere.local \
  --insecure-skip-tls-verify
```

```shell
kubectl exec --stdin --tty openemr-6dbfbb7c66-b4p4h -n openemr -- /bin/ash
```

Then, navigate to /var/log/apache2 and look in access.log and error.log

# OpenEMR Capabilities

Oauth2 Metadata: https://openemr.tanzuathome.net/oauth2/default/.well-known/openid-configuration

FHIR Capabilities: https://openemr.tanzuathome.net/apis/default/fhir/metadata

# Configure OpenEMR for API Access

1. Login to OpenEMR here https://openemr.tanzuathome.net
1. Navigate to Administration &gt; Globals &gt; Connectors
1. Set site address to "https://openemr.tanzuathome.net"
1. Enable REST and FHIR REST APIs
1. Save

# Register with OpenEMR UI (Not Working)

Navigate to https://openemr.tanzuathome.net/interface/smart/register-app.php

Register an app with these attributes:

| Attribute | Value |
|---|---|
| App Name | Radiology Client |
| Contact Email | jefbutler@vmware.com |
| App Redirect URI | http://localhost:8080/login/oauth2/code/openemr |
| App Launch URI | http://localhost:8080 |

# Register with Postman

Send a POST Request to https://openemr.tanzuathome.net/oauth2/default/registration

With this body:

```json
{
  "application_type": "private",
  "redirect_uris": [
    "http://localhost:8080/login/oauth2/code/openemr"
  ],
  "post_logout_redirect_uris": [
    "http://localhost:8080"
  ],
  "client_name": "Radiology Client",
  "token_endpoint_auth_method": "client_secret_post",
  "contacts": [
    "jefbutler@vmware.com"
  ],
  "scope": "openid fhirUser profile name offline_access api:oemr api:fhir api:port user/allergy.read user/allergy.write user/appointment.read user/appointment.write user/dental_issue.read user/dental_issue.write user/document.read user/document.write user/drug.read user/encounter.read user/encounter.write user/facility.read user/facility.write user/immunization.read user/insurance.read user/insurance.write user/insurance_company.read user/insurance_company.write user/insurance_type.read user/list.read user/medical_problem.read user/medical_problem.write user/medication.read user/medication.write user/message.write user/patient.read user/patient.write user/practitioner.read user/practitioner.write user/prescription.read user/procedure.read user/soap_note.read user/soap_note.write user/surgery.read user/surgery.write user/vital.read user/vital.write user/AllergyIntolerance.read user/CareTeam.read user/Condition.read user/Coverage.read user/Encounter.read user/Immunization.read user/Location.read user/Medication.read user/MedicationRequest.read user/Observation.read user/Organization.read user/Organization.write user/Patient.read user/Patient.write user/Practitioner.read user/Practitioner.write user/PractitionerRole.read user/Procedure.read patient/encounter.read patient/patient.read patient/AllergyIntolerance.read patient/CareTeam.read patient/Condition.read patient/Encounter.read patient/Immunization.read patient/MedicationRequest.read patient/Observation.read patient/Patient.read patient/Procedure.read"
}
```

Received this response:

```json
{
    "client_id": "3aDrndqX_3JZvLsPQTGCJz3b5Mq0olqRhlKqS16a59g",
    "client_secret": "QCyr2BwSyTCwFTBPfSw7UmOK0o2xmUjfyGGBWoqRH__nbTqxJxTnwcV5qKg2O-selEnCY_4_iohsPel2HaHEkQ",
    "registration_access_token": "UQX_TBuLmZgs2Ii8hBQoI36UBf1GxvtoTNl54Q9HJXc",
    "registration_client_uri": "https://openemr.tanzuathome.net/oauth2/default/client/2gay-1zBHwWXfd1GCkReUQ",
    "client_id_issued_at": 1625764247,
    "client_secret_expires_at": 0,
    "client_role": "user",
    "contacts": [
        "jefbutler@vmware.com"
    ],
    "application_type": "private",
    "client_name": "Radiology Client",
    "redirect_uris": [
        "http://localhost:8080/login/oauth2/code/openemr"
    ],
    "post_logout_redirect_uris": [
        "http://localhost:8080"
    ],
    "token_endpoint_auth_method": "client_secret_post",
    "scope": "openid fhirUser profile name offline_access api:oemr api:fhir api:port user/allergy.read user/allergy.write user/appointment.read user/appointment.write user/dental_issue.read user/dental_issue.write user/document.read user/document.write user/drug.read user/encounter.read user/encounter.write user/facility.read user/facility.write user/immunization.read user/insurance.read user/insurance.write user/insurance_company.read user/insurance_company.write user/insurance_type.read user/list.read user/medical_problem.read user/medical_problem.write user/medication.read user/medication.write user/message.write user/patient.read user/patient.write user/practitioner.read user/practitioner.write user/prescription.read user/procedure.read user/soap_note.read user/soap_note.write user/surgery.read user/surgery.write user/vital.read user/vital.write user/AllergyIntolerance.read user/CareTeam.read user/Condition.read user/Coverage.read user/Encounter.read user/Immunization.read user/Location.read user/Medication.read user/MedicationRequest.read user/Observation.read user/Organization.read user/Organization.write user/Patient.read user/Patient.write user/Practitioner.read user/Practitioner.write user/PractitionerRole.read user/Procedure.read patient/encounter.read patient/patient.read patient/AllergyIntolerance.read patient/CareTeam.read patient/Condition.read patient/Encounter.read patient/Immunization.read patient/MedicationRequest.read patient/Observation.read patient/Patient.read patient/Procedure.read"
}
```
