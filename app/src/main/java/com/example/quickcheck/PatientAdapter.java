package com.example.quickcheck;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class PatientAdapter extends ArrayAdapter<Patient> {

    private Context context;
    private int resource;
    private ArrayList<Patient> patients;


    public PatientAdapter(Context context, int resource, ArrayList<Patient> patients) {
        super(context, resource, patients);
        this.context = context;
        this.resource = resource;
        this.patients = patients;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(resource, null);
        }

        // Get the current patient object from the ArrayList
        Patient patient = patients.get(position);

        TextView patientNameTextView = view.findViewById(R.id.patient_name_text);
        TextView patientIdTextView = view.findViewById(R.id.patient_id_text);
        ImageView patientImageView = view.findViewById(R.id.patient_image);

        patientNameTextView.setText(patient.getFirstname() + " " + patient.getLastname());
        patientIdTextView.setText(String.valueOf(patient.getId()));

        // Set click listeners for the patient name and ID
        patientNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the MedicalRecordActivity and pass the patient's ID
                Intent intent = new Intent(context, MedicalRecordActivity.class);
                intent.putExtra("patientId", patient.getId());
                context.startActivity(intent);
            }
        });

        patientIdTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the MedicalRecordActivity and pass the patient's ID
                Intent intent = new Intent(context, MedicalRecordActivity.class);
                intent.putExtra("patientId", patient.getId());
                context.startActivity(intent);
            }
        });

        if (patient.getGender().equalsIgnoreCase("female"))
            patientImageView.setImageResource(R.drawable.ic_female);
        if (patient.getGender().equalsIgnoreCase("other"))
            patientImageView.setImageResource(R.drawable.ic_g);

        return view;
    }
}






