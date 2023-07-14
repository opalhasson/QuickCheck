package com.example.quickcheck;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.app.Activity;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends Activity {

    private EditText nameField;
    private Spinner degreeSpinner;
    private Spinner unitSpinner;
    private EditText emailField;
    private EditText passwordField;
    private Button signButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initViews();
        setDegreeSpinner();
        setUnitSpinner();

        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    public void initViews() {
        nameField = findViewById(R.id.name_field);
        degreeSpinner = findViewById(R.id.degree_spinner);
        unitSpinner = findViewById(R.id.unit_spinner);
        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);
        signButton = findViewById(R.id.sign_button);
    }

    public void setDegreeSpinner() {
        String[] degreeOptions = {"Choose your title", "Mr", "Ms", "Dr", "Prof"};
        ArrayAdapter<String> degreeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, degreeOptions);
        degreeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        degreeSpinner.setAdapter(degreeAdapter);
    }

    public void setUnitSpinner() {
        String[] unitOptions = {"Choose your unit", "emergency department", "burn unit", "Urology", "Neurology"};
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, unitOptions);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unitSpinner.setAdapter(unitAdapter);
    }

    public void signUp() {
        // Get user input from the fields
        String name = nameField.getText().toString();
        String degree = degreeSpinner.getSelectedItem().toString();
        String unit = unitSpinner.getSelectedItem().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        // Perform sign-up process using Firebase Authentication
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {

                            // Create a user object
                            User newUser = new User(email, name, degree, unit);

                            // Get a reference to the Firebase Firestore database
                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            // Assuming you have a collection called "users" in your Firestore database
                            CollectionReference usersCollection = db.collection("users");

                            // Add the user to the "users" collection
                            usersCollection.add(newUser)
                                    .addOnSuccessListener(documentReference -> {
                                        // Patient added successfully
                                        Toast.makeText(SignupActivity.this, "User was add successfully.",
                                                Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(this, MainActivity.class);
                                        startActivity(intent);
                                    })
                                    .addOnFailureListener(e -> {
                                        // Error occurred while adding the patient
                                        Log.e(TAG, "Error adding user: " + e.getMessage());
                                        // Show an error message or perform appropriate error handling
                                    });
                        }
                    } else {
                        // Failed to create a user with email and password
                        Toast.makeText(SignupActivity.this, "Error adding user.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
