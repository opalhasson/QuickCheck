package com.example.quickcheck;

import static android.content.ContentValues.TAG;
import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddPatientActivity extends AppCompatActivity {

    private EditText dobField;
    private EditText firstnameField;
    private EditText lastnameField;
    private EditText idField;
    private Calendar calendar;
    private NumberPicker dayPicker;
    private NumberPicker monthPicker;
    private NumberPicker yearPicker;
    private View dialogView;
    private Spinner genderSpinner;
    private Button addButton;
    private String unitname;
    private String email;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpatient);

        db = FirebaseFirestore.getInstance();
        initViews();

        unitname = getIntent().getStringExtra("unit");
        email = getIntent().getStringExtra("email");

        setGenderSpinner();

        dobField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPatient();
            }
        });
    }

    public void initViews() {
        dobField = findViewById(R.id.dob_field);
        calendar = Calendar.getInstance();
        addButton = findViewById(R.id.add_button);
        genderSpinner = findViewById(R.id.gender_spinner);
        firstnameField = findViewById(R.id.first_name_field);
        lastnameField = findViewById(R.id.last_name_field);
        idField = findViewById(R.id.id_field);
    }

    private void showDatePicker() {
        dialogView = LayoutInflater.from(this).inflate(R.layout.custom_date_picker, null);
        dayPicker = dialogView.findViewById(R.id.day_picker);
        monthPicker = dialogView.findViewById(R.id.month_picker);
        yearPicker = dialogView.findViewById(R.id.year_picker);

        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(31);
        dayPicker.setValue(calendar.get(Calendar.DAY_OF_MONTH));

        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        monthPicker.setValue(calendar.get(Calendar.MONTH)); // Month is zero-based

        int currentYear = calendar.get(Calendar.YEAR);
        yearPicker.setMinValue(currentYear - 100);
        yearPicker.setMaxValue(currentYear);
        yearPicker.setValue(currentYear);

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, yearPicker.getValue());
                calendar.set(Calendar.MONTH, monthPicker.getValue());
                calendar.set(Calendar.DAY_OF_MONTH, dayPicker.getValue());
                updateDobField();
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                AddPatientActivity.this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ) {
            @Override
            public void onDateChanged(DatePicker view, int year, int month, int dayOfMonth) {
                // Prevent the dialog from automatically closing when changing the month or year
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            }
        };

        datePickerDialog.setView(dialogView);
        datePickerDialog.show();
    }

    private void updateDobField() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        dobField.setText(sdf.format(calendar.getTime()));
    }

    public void setGenderSpinner() {
        String[] genderOptions = {"Choose your gender","Male", "Female", "Other"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genderOptions);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapter);
    }

    private void addPatient() {
        // Retrieve the input values from the UI fields
        String firstname = firstnameField.getText().toString();
        String lastname = lastnameField.getText().toString();
        String id = idField.getText().toString();
        String dob = dobField.getText().toString();
        String gender = genderSpinner.getSelectedItem().toString();

        // Validate the input fields
        if (firstname.isEmpty() || lastname.isEmpty() || id.isEmpty() ||
                dob.isEmpty() || gender.equals("Choose your gender")) {
            return;
        }

        // Create a Patient object
        Patient patient = new Patient(firstname, lastname, id, dob, gender);

        // Assuming you have a collection called "patients" in your Firestore database
        CollectionReference patientsCollection = db.collection("patients");

        // Add the patient to the "patients" collection
        patientsCollection.add(patient)
                .addOnSuccessListener(documentReference -> {
                    // Patient added successfully, creating medical record for patient
                    MedicalRecord medicalRecord = new MedicalRecord(id, unitname);
                    CollectionReference medicalrecordsCollection = db.collection("medicalRecords");

                    medicalrecordsCollection.add(medicalRecord);

                    Toast.makeText(AddPatientActivity.this, "Patient was add successfully.",
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(this, PatientsScreenActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    // Error occurred while adding the patient
                    Log.e(TAG, "Error adding patient: " + e.getMessage());
                });
    }
}

