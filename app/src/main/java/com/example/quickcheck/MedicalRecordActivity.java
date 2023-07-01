package com.example.quickcheck;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Arrays;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicalrecord);

        db = FirebaseFirestore.getInstance();

        initViews();

        // Get the patient ID and Unit from the intent
        patientId = getIntent().getStringExtra("patientId");
        unit = getIntent().getStringExtra("unit");

        setTransferSpinner();
        setPatient();
        loadMedicalRecord();

        transfer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                            temperature_field.setText(String.valueOf(medicalRecord.getPatientHeight()));
                        health_status_text.setText(medicalRecord.getHealthStatus());
                        doctor_opinion_text.setText(medicalRecord.getDoctorOpinion());
                    }
                });

    }
}
