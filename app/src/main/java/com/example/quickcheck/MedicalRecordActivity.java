package com.example.quickcheck;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MedicalRecordActivity extends Activity {
    private TextView patient_name_text;
    private TextView id_text;
    private EditText weight_field;
    private EditText height_field;
    private EditText blood_pressure_field;
    private EditText temperature_field;
    private EditText health_status_text;
    private EditText doctor_opinion_text;
    private Spinner transfer_spinner;
    private Button transfer_button;
    private Button back_button;
    private FirebaseFirestore db;
    private String unit;
    private String patientId;
    private String email;
    private Boolean inArch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicalrecord);

        db = FirebaseFirestore.getInstance();

        initViews();

        // Get the patient ID and Unit from the intent
        patientId = getIntent().getStringExtra("patientId");
        unit = getIntent().getStringExtra("unit");
        email = getIntent().getStringExtra("email");

        setTransferSpinner();
        setPatient();
        loadMedicalRecord();

        transfer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transferPatient();
                if (inArch)
                    saveMedicalRecord();
            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMedicalRecord();
            }
        });
    }

    public void initViews() {
        patient_name_text = findViewById(R.id.patient_name_text);
        id_text = findViewById(R.id.id_text);
        weight_field = findViewById(R.id.weight_field);
        height_field = findViewById(R.id.height_field);
        blood_pressure_field = findViewById(R.id.blood_pressure_field);
        temperature_field = findViewById(R.id.temperature_field);
        health_status_text = findViewById(R.id.health_status_text);
        doctor_opinion_text = findViewById(R.id.doctor_opinion_text);
        transfer_spinner = findViewById(R.id.transfer_spinner);
        transfer_button = findViewById(R.id.transfer_button);
        back_button = findViewById(R.id.back_button);
    }

    public void setTransferSpinner() {
        String[] transferOptions = {"Transfer...","Home", "emergency department", "burn unit", "Urology", "Neurology"};
        String[] filteredOptions = Arrays.stream(transferOptions)
                .filter(option -> !option.equals(unit))
                .toArray(String[]::new);
        ArrayAdapter<String> tranferAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filteredOptions);
        tranferAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        transfer_spinner.setAdapter(tranferAdapter);
    }

    public void setPatient() {
        CollectionReference patientsCollection = db.collection("patients");

        patientsCollection
                .whereEqualTo("id", patientId)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);

                        // Retrieve the patient object from the document snapshot
                        Patient patient = documentSnapshot.toObject(Patient.class);

                        patient_name_text.setText(patient.getFirstname() + " " + patient.getLastname());
                        id_text.setText(patient.getId());
                    }
                });

    }

    public void loadMedicalRecord() {
        CollectionReference medicalRecordsCollection = db.collection("medicalRecords");

        medicalRecordsCollection
                .whereEqualTo("patientId", patientId)
                .whereEqualTo("unit", unit)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);

                        // Retrieve the MedicalRecord object from the document snapshot
                        MedicalRecord medicalRecord = documentSnapshot.toObject(MedicalRecord.class);

                        if (medicalRecord.getPatientWeight() != 0)
                            weight_field.setText(String.valueOf(medicalRecord.getPatientWeight()));
                        if (medicalRecord.getPatientHeight() != 0)
                            height_field.setText(String.valueOf(medicalRecord.getPatientHeight()));
                        blood_pressure_field.setText(medicalRecord.getPatientBloodPressure());
                        if (medicalRecord.getPatientHeight() != 0)
                            temperature_field.setText(String.valueOf(medicalRecord.getPatientTemperature()));
                        health_status_text.setText(medicalRecord.getHealthStatus());
                        doctor_opinion_text.setText(medicalRecord.getDoctorOpinion());
                    }
                });
    }

    public void transferPatient() {
        String transfer = transfer_spinner.getSelectedItem().toString();
        if (!transfer.equals("Transfer...")) {
            if (!transfer.equals("Home")) {
                MedicalRecord medicalRecord = new MedicalRecord(patientId, transfer);
                CollectionReference medicalrecordsCollection = db.collection("medicalRecords");
                medicalrecordsCollection.add(medicalRecord);
            }
            inArch = true;
        }
        else {
            inArch = false;
            Toast.makeText(MedicalRecordActivity.this, "Transfer to where?... choose.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void saveMedicalRecord() {
        CollectionReference medicalRecordsCollection = db.collection("medicalRecords");

        medicalRecordsCollection
                    .whereEqualTo("patientId", patientId)
                    .whereEqualTo("unit", unit)
                    .get().addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            String documentId = documentSnapshot.getId();

                            // Create a map to hold the updated field values
                            Map<String, Object> updates = new HashMap<>();

                            String weightText = weight_field.getText().toString().trim();
                            if (!weightText.isEmpty()) {
                                int weight = Integer.parseInt(weightText);
                                updates.put("patientWeight", weight);
                            }

                            String heightText = height_field.getText().toString().trim();
                            if (!heightText.isEmpty()) {
                                int height = Integer.parseInt(heightText);
                                updates.put("patientHeight", height);
                            }

                            updates.put("patientBloodPressure", blood_pressure_field.getText().toString());

                            String temperatureText = temperature_field.getText().toString().trim();
                            if (!temperatureText.isEmpty()) {
                                int temperature = Integer.parseInt(temperatureText);
                                updates.put("patientTemperature", temperature);
                            }

                            updates.put("healthStatus", health_status_text.getText().toString());
                            updates.put("doctorOpinion", doctor_opinion_text.getText().toString());

                            // Update the document with the new field values
                            medicalRecordsCollection.document(documentId).update(updates)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(MedicalRecordActivity.this, "Medical record updated successfully.",
                                                Toast.LENGTH_SHORT).show();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(MedicalRecordActivity.this, "Failed to update medical record.",
                                                Toast.LENGTH_SHORT).show();
                                    });
                        }
                    });

        Intent intent = new Intent(this, PatientsScreenActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
    }
}
