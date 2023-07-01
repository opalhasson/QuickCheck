package com.example.quickcheck;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MedicalRecordActivity extends Activity {
    private TextView patientIdTextView;
    private TextView medicalRecordTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicalrecord);

        // Get the patient ID from the intent
        String patientId = getIntent().getStringExtra("patientId");
        Log.i(TAG, "onCreate: " + patientId);

        // Initialize the views
        //patientIdTextView = findViewById(R.id.patient_id_text);
        //medicalRecordTextView = findViewById(R.id.medical_record_text);

        // Set the patient ID
        //patientIdTextView.setText(patientId);

        // Load and display the medical record data for the patient with the given ID
        //loadMedicalRecord(patientId);
    }

    private void loadMedicalRecord(String patientId) {
        // Here you would implement the logic to fetch the medical record data
        // for the patient with the specified ID from your data source (e.g., Firebase Firestore)

        // Dummy data for demonstration purposes
        String medicalRecordData = "This is the medical record for patient with ID: " + patientId;

        // Set the medical record data in the TextView
        medicalRecordTextView.setText(medicalRecordData);
    }
}
