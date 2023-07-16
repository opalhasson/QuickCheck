package com.example.quickcheck;

import static android.content.ContentValues.TAG;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PatientsScreenActivity extends AppCompatActivity {
    private TextView userName;
    private TextView unitName;
    private ListView patientsList;
    private PatientAdapter patientAdapter;
    private String email;
    private ImageButton addPatientButton;
    private Button pastPatientsButton;
    private Button presentPatientsButton;
    private Boolean inArch;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patients_screen);

        db = FirebaseFirestore.getInstance();

        initViews();

        // Retrieve the email from the intent
        email = getIntent().getStringExtra("email");
        if (email != null) {
            setUserInfoByEmail();
        }

        inArch = false;
        setListOfPatients();

        pastPatientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inArch = true;
                setListOfPatients();
            }
        });

        presentPatientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inArch = false;
                setListOfPatients();
            }
        });

        addPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the AddPatientActivity
                Intent intent = new Intent(PatientsScreenActivity.this, AddPatientActivity.class);
                intent.putExtra("unit", unitName.getText().toString());
                intent.putExtra("email",email);
                startActivity(intent);
            }
        });
    }

    public void initViews() {
        userName = findViewById(R.id.user_name_text);
        unitName = findViewById(R.id.unit_name_text);
        patientsList = findViewById(R.id.patients_list);
        addPatientButton = findViewById(R.id.add_user_button);
        pastPatientsButton = findViewById(R.id.past_patients_button);
        presentPatientsButton = findViewById(R.id.present_patients_button);
    }

    public void setUserInfoByEmail() {
        CollectionReference usersCollection = db.collection("users");

        // Perform the query to get the user document with the specified email
        usersCollection.whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Check if the query result contains any documents
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Get the first document (assuming unique emails) from the query result
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);

                        // Retrieve the user object from the document snapshot
                        User user = documentSnapshot.toObject(User.class);

                        userName.setText(user.getDegree() + " " + user.getName());
                        unitName.setText(user.getUnit());

                    } else {
                        Toast.makeText(PatientsScreenActivity.this, "User not found.",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Error occurred while querying for the user
                    Log.e(TAG, "Error founding user: " + e.getMessage());
                });
    }

    public void setListOfPatients() {
        CollectionReference patientsCollection = db.collection("patients");
        CollectionReference medicalRecordsCollection = db.collection("medicalRecords");

        patientsCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            ArrayList<Patient> patients = new ArrayList<>();

            // Iterate through the query results and convert each document to a Patient object
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                Patient patient = documentSnapshot.toObject(Patient.class);

                medicalRecordsCollection
                        .whereEqualTo("patientId", patient.getId())
                        .whereEqualTo("unit", unitName.getText().toString())
                        .whereEqualTo("inArch", inArch)
                        .get().addOnSuccessListener(queryDocumentSnapshots1 -> {
                            if (!queryDocumentSnapshots1.isEmpty()) {
                                patients.add(patient);
                                // Notify the adapter that the data set has changed
                                patientAdapter.notifyDataSetChanged();
                            }
                        });
            }

            // Create a new instance of the PatientAdapter and pass in the patients ArrayList
            patientAdapter = new PatientAdapter(PatientsScreenActivity.this, R.layout.list_item_patient, patients,unitName.getText().toString(), email);

            // Set the adapter to the ListView
            patientsList.setAdapter(patientAdapter);

        }).addOnFailureListener(e -> {
            Log.e(TAG, "Error retrieving patients: " + e.getMessage());
        });
    }
}
