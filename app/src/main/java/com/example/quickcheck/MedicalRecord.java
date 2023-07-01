package com.example.quickcheck;

public class MedicalRecord {
    private String patientId;
    private int patientWeight;
    private int patientHeight;
    private String patientBloodPressure;
    private Double patientTemperature;
    private String healthStatus;
    private String doctorOpinion;
    private String inArch;
    private String unit;

    public MedicalRecord() {}

    public MedicalRecord(String patientId, String unit) {
        this.patientId = patientId;
        this.unit = unit;
    }

    public MedicalRecord(String patientId, int patientWeight, int patientHeight, String patientBloodPressure,
                         Double patientTemperature, String healthStatus, String doctorOpinion, String inArch, String unit) {
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

    public Double getPatientTemperature() {
        return patientTemperature;
    }

    public void setPatientTemperature(Double patientTemperature) {
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

    public String getInArch() {
        return inArch;
    }

    public void setInArch(String inArch) {
        this.inArch = inArch;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
