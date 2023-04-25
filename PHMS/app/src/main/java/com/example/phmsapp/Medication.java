package com.example.phmsapp;

public class Medication {
    private String name;
    private String rxNumber;
    private String dosage;
    private String strength;
    private String frequency;
    private String refills;
    private String expirationDate;
    private String pharmacyPhoneNumber;
    private boolean deleted;

    public Medication(String name, String rxNumber, String dosage, String strength, String frequency,
                      String refills, String expirationDate, String pharmacyPhoneNumber) {
        this.name = name;
        this.rxNumber = rxNumber;
        this.dosage = dosage;
        this.strength = strength;
        this.frequency = frequency;
        this.refills = refills;
        this.expirationDate = expirationDate;
        this.pharmacyPhoneNumber = pharmacyPhoneNumber;
        this.deleted = false;
    }

    // Getter methods for all properties
    public String getName() {
        return name;
    }

    public String getRxNumber() {
        return rxNumber;
    }

    public String getDosage() {
        return dosage;
    }

    public String getStrength() {
        return strength;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getRefills() {
        return refills;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public String getPharmacyPhoneNumber() {
        return pharmacyPhoneNumber;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setRxNumber(String rxNumber) {
        this.rxNumber = rxNumber;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void setRefills(String refills) {
        this.refills = refills;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setPharmacyPhoneNumber(String pharmacyPhoneNumber) {
        this.pharmacyPhoneNumber = pharmacyPhoneNumber;
    }
    public boolean isDeleted() {
        return deleted;
    }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
