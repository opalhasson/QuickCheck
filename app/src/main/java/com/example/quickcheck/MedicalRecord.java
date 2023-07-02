package com.example.quickcheck;

public class MedicalRecord {
    private String patientId;
    private int patientWeight;
    private int patientHeight;
    private String patientBloodPressure;
    private String patientTemperature;
    private String healthStatus;
    private String doctorOpinion;
    private Boolean inArch;
    private String unit;

    public MedicalRecord() {}

    public MedicalRecord(String patientId, String unit) {
        this.patientId = patientId;
        this.unit = unit;
        this.inArch = false;
        this.patientBloodPressure = "";
        this.healthStatus = "";
        this.doctorOpinion = "";
        this.patientWeight = this.patientHeight = 0;
        this.patientTemperature = "";
    }

    public MedicalRecord(String patientId, int patientWeight, int patientHeight, String patientBloodPressure,
                         String patientTemperature, String healthStatus, String doctorOpinion, Boolean inArch, String unit) {
        this.patientId = patientId;
        this.patientWeight = patientWeight;
        this.patientHeight = patientHeight;
        this.patientBloodPressure = patientBloodPressure;
        this.patientTemperature = patientTemperature;
        this.healthStatus = healthStatus;
        this.doctorOpinion = doctorOpinion;
        this.inArch = inArch;
        this.unit = unit;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public int getPatientWeight() {
        return patientWeight;
    }

    public void setPatientWeight(int patientWeight) {
        this.patientWeight = patientWeight;
    }

    public int getPatientHeight() {
        return patientHeight;
    }

    public void setPatientHeight(int patientHeight) {
        this.patientHeight = patientHeight;
    }

    public String getPatientBloodPressure() {
        return patientBloodPressure;
    }

    public void setPatientBloodPressure(String patientBloodPressure) {
        this.patientBloodPressure = patientBloodPressure;
    }

    public String getPatientTemperature() {
        return patientTemperature;
    }

    public void setPatientTemperature(String patientTemperature) {
        this.patientTemperature = patientTemperature;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public String getDoctorOpinion() {
        return doctorOpinion;
    }

    public void setDoctorOpinion(String doctorOpinion) {
        this.doctorOpinion = doctorOpinion;
    }

    public Boolean getInArch() {
        return inArch;
    }

    public void setInArch(Boolean inArch) {
        this.inArch = inArch;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
