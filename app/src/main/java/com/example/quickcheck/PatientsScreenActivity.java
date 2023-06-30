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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PatientsScreenActivity extends AppCompatActivity {

    private TextView userName;
    private TextView unitName;
    private ListView patientsList;
    private PatientAdapter patientAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patientsscreen);

        userName = findViewById(R.id.user_name_text);
        unitName = findViewById(R.id.unit_name_text);
        patientsList = findViewById(R.id.patients_list);


        // Retrieve the email from the intent
        String email = getIntent().getStringExtra("email");

        // Access the user's information if needed
        if (email != null) {
            // Get a reference to the Firebase Firestore database
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Assuming you have a collection called "patients" in your Firestore database
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

                            // Now you have the user object, you can use it as needed
                        } else {
                            // No user found with the specified email
                            Toast.makeText(PatientsScreenActivity.this, "User not found.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Error occurred while querying for the user
                        Log.e(TAG, "Error founding user: " + e.getMessage());
                    });


        }

        // Set the adapter to the ListView
        //patientsList.setAdapter(patientAdapter);

        // Assuming you have a collection called "patients" in your Firestore database
        CollectionReference patientsCollection = FirebaseFirestore.getInstance().collection("patients");

        // Perform the query to get all patients from the collection
        patientsCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            // Create a new ArrayList to hold the retrieved patients
            ArrayList<Patient> patients = new ArrayList<>();

            // Iterate through the query results and convert each document to a Patient object
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                Patient patient = documentSnapshot.toObject(Patient.class);
                patients.add(patient);
            }
            Log.i(TAG, "onCreate: " + patients.get(0).getLastname() + " " + patients.size());

            // Create a new instance of the PatientAdapter and pass in the patients ArrayList
            patientAdapter = new PatientAdapter(PatientsScreenActivity.this, R.layout.list_item_patient, patients);

            // Set the adapter to the ListView
            patientsList.setAdapter(patientAdapter);

        }).addOnFailureListener(e -> {
            // Error occurred while querying for patients
            Log.e(TAG, "Error retrieving patients: " + e.getMessage());
        });


        // Find the "Add Patient" button in the layout
        Button addPatientButton = findViewById(R.id.add_user_button);

        // Set an OnClickListener to the "Add Patient" button
        addPatientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the AddPatientActivity
                Intent intent = new Intent(PatientsScreenActivity.this, AddPatientActivity.class);
                startActivity(intent);
            }
        });
    }
}
