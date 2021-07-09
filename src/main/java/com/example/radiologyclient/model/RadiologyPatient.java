package com.example.radiologyclient.model;

import lombok.Data;

@Data
public class RadiologyPatient {
    private final String id;
    private final String firstName;
    private final String lastName;
    private final String gender;
}
